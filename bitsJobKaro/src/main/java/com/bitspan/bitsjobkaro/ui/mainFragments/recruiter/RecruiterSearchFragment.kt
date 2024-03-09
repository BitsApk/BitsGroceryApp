package com.bitspan.bitsjobkaro.ui.mainFragments.recruiter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.sidesheet.SideSheetDialog
import com.google.gson.GsonBuilder
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.enums.RecFragInfoEnum
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList.ageList
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList.engList
import com.bitspan.bitsjobkaro.data.dataList.PreDefinedList.qualList
import com.bitspan.bitsjobkaro.data.models.recruiter.RecSearchEmployeReq
import com.bitspan.bitsjobkaro.data.models.recruiter.RecruiterEmpData
import com.bitspan.bitsjobkaro.data.models.recruiter.ShortListData
import com.bitspan.bitsjobkaro.databinding.FragmentRecruiterSearchBinding
import com.bitspan.bitsjobkaro.databinding.SideSheetRecSearchBinding
import com.bitspan.bitsjobkaro.presentation.adapters.ChatTypes
import com.bitspan.bitsjobkaro.presentation.adapters.EmpListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.ChatViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterManageViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecruiterSearchFragment : Fragment() {


    private lateinit var binding: FragmentRecruiterSearchBinding
    private lateinit var sideSheetBinding: SideSheetRecSearchBinding
    private lateinit var sideSheet: SideSheetDialog
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var recEmpSearchReq: RecSearchEmployeReq
    private lateinit var empListAdapter: EmpListAdapter
    private var shortList: MutableList<ShortListData> = mutableListOf()
    private val recVM: RecruiterViewModel by viewModels()
    private val recNewVM: RecruiterNewViewModel by viewModels()
    private val recPostViewModel: RecruiterManageViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        sideSheet = SideSheetDialog(mContext)
        binding = FragmentRecruiterSearchBinding.inflate(inflater, container, false)
        sideSheetBinding = SideSheetRecSearchBinding.inflate(LayoutInflater.from(mContext))
        sideSheet.setContentView(sideSheetBinding.root)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
//        binding.searchEdTxt.requestFocus()
        CommonUiFunctions.handleKeyBoardState(mContext, binding.searchEdTxt)

        recEmpSearchReq = RecSearchEmployeReq(
            recId = userId.toInt()
        )

        getAllShortlisted()

        // Setting chip group
        setChipData(PreDefinedList.formattedExpList, binding.searchExpChipG)
        setChipData(PreDefinedList.jobTypeList, binding.searchJobTypeChipG)
        setChipData(PreDefinedList.empTimeType, binding.searchJobCatChipG)

        // Setting radio group
        setRadioData(qualList, sideSheetBinding.filterQualRadioG)
        setRadioData(ageList, sideSheetBinding.filterAgeRadioG)
        setRadioData(engList, sideSheetBinding.filterEngRadioG)

        val editText  = sideSheetBinding.filterSkillEdTxt.editText
        editText?.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == IME_ACTION_DONE || p1 == IME_ACTION_NEXT) {
                    if (p0?.text.isNullOrBlank()) {
                        sideSheetBinding.filterSkillEdTxt.error = "Enter valid skill"
                    } else {
                        addSingleChip(p0?.text.toString().trim(), sideSheetBinding.filterChipGroup)
                        editText.setText("")
                    }
                }
                return true
            }
        })

        binding.filterSearchImg.setOnClickListener {
            sideSheet.show()
        }

        sideSheetBinding.filterQualField.setOnClickListener {
            setQualRadio()
        }

        sideSheetBinding.filterAgeField.setOnClickListener {
            setAgeRadio()
        }

        sideSheetBinding.filterEngField.setOnClickListener {
            setEngRadio()
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchBtn.setOnClickListener {
            CommonUiFunctions.hideKeyBoard(mContext, binding.searchEdTxt)
            binding.progBar.visibility = View.VISIBLE
            binding.searchBtn.isEnabled = false
            recEmpSearchReq.searchJobTitle = if (binding.searchEdTxt.text.toString().isBlank()) "" else binding.searchEdTxt.text.toString().trim()
            var skillString = ""
            for (index in 0 until sideSheetBinding.filterChipGroup.childCount) {
                val chip = sideSheetBinding.filterChipGroup.getChildAt(index) as Chip
                skillString += if (index != sideSheetBinding.filterChipGroup.childCount - 1) "${chip.text}," else chip.text
            }
            recEmpSearchReq.skills = skillString
            getRecSearch()
        }

        binding.searchExpChipG.setOnCheckedStateChangeListener {group, checkedIdList: MutableList<Int> ->
            val chip = group.findViewById(checkedIdList[0]) as Chip
            recEmpSearchReq.exp = CommonDataFunctions.postEmpExperience(chip.text.toString())
        }
        binding.searchJobTypeChipG.setOnCheckedStateChangeListener {group, checkedIdList: MutableList<Int> ->
            val chip = group.findViewById(checkedIdList[0]) as Chip
            recEmpSearchReq.jType = CommonDataFunctions.postEmpJobTimeType(chip.text.toString())
        }
        binding.searchJobCatChipG.setOnCheckedStateChangeListener {group, checkedIdList: MutableList<Int> ->
            val chip = group.findViewById(checkedIdList[0]) as Chip
            recEmpSearchReq.eTYpe = CommonDataFunctions.postEmployeeEmpType(chip.text.toString())
        }

        sideSheetBinding.filterQualRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            recEmpSearchReq.education = CommonDataFunctions.postEmpQualif(radioButton.text.toString())
        }

        sideSheetBinding.filterAgeRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            recEmpSearchReq.age = CommonDataFunctions.postEmpAge(radioButton.text.toString())
        }

        sideSheetBinding.filterEngRadioG.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<RadioButton>(i)
            recEmpSearchReq.english = CommonDataFunctions.postEmpEng(radioButton.text.toString())
        }

//        val arrayAdapter = ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, PreDefinedList.jobPostTempList)
//        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_multichoice)
//        binding.spinner.adapter = arrayAdapter
//
//        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }
    }

    private fun getAllShortlisted() {
        viewLifecycleOwner.lifecycleScope.launch {
            recVM.getAllShortlisted(userId.toInt()).let {
                if (it.isSuccessful && it.body() != null) {
                    if (it.body()!!.status == 200) {
                        shortList = it.body()!!.shortList!!
                    }
                } else {
                    CommonUiFunctions.showErrorMsgDialog(
                        mContext,
                        "Error in fetching employess detail, please check internet"
                    ) {

                    }
                    binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setEmpListAdapter() {
        binding.searchEmpRecView.adapter = empListAdapter
    }

    fun callShortListApi(empId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            recPostViewModel.callShortListApi(empId, userId.toInt()).let {
            }
        }
    }

    private fun reqResume(empId: Int, list: List<RecruiterEmpData>, pos: Int) {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                chatViewModel.sendMess(
                    empId = empId,
                    mess = ChatTypes.recReqRes,
                    recId = userId.toInt(),
                    senderType = "recruiter"
                ).let {
                    list[pos].showProg = false
                    empListAdapter.notifyItemChanged(pos)
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == "200") {
                            Toast.makeText(
                                mContext,
                                "Resume requested",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext, "Some technical error in requesting resume"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            CommonUiFunctions.showErrorMsgDialog(
                mContext,
                "Some error in requesting resume, Check your internet"
            ) {
                findNavController().popBackStack()
            }
        }

    }

    private fun getRecSearch() {
        val gson = GsonBuilder().create()
        val req = gson.toJson(recEmpSearchReq)
        Log.d("Rishabh", "req search $req")
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recNewVM.recSearchEmployee(recEmpSearchReq).let {
                    binding.progBar.visibility = View.GONE
                    binding.searchBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.statusCode == 200) {
                            binding.noDataLay.clNodataFound.visibility = View.GONE
                            val empList = it.body()!!.list ?: listOf()
                            empListAdapter = EmpListAdapter(
                                empList,
                                RecFragInfoEnum.RecSearchEmp,
                                mContext,
                                shortList
                            ) { empId, type, pos ->
                                when (type) {
                                    1 -> callShortListApi(empId)
                                    2 -> reqResume(empId, empList, pos)
                                }
                            }
                            setEmpListAdapter()
                            binding.noDataLay.clNodataFound.visibility = View.GONE

                        } else {
                            binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(
                            mContext,
                            "Some error in searching with filters, Check your internet"
                        ) {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.searchBtn.isEnabled = true
                binding.noDataLay.clNodataFound.visibility = View.VISIBLE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Some error in searching with filters, Check your internet"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setEngRadio() {
        sideSheetBinding.apply {
            filterEngRadioG.visibility = if (filterEngRadioG.isVisible) {
                filterEngField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterEngField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }

    private fun setAgeRadio() {
        sideSheetBinding.apply {
            filterAgeRadioG.visibility = if (filterAgeRadioG.isVisible) {
                filterAgeField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterAgeField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }

    private fun setQualRadio() {
        sideSheetBinding.apply {
            filterQualRadioG.visibility = if (filterQualRadioG.isVisible) {
                filterQualField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_up_arrow,0)
                View.GONE
            } else {
                filterQualField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_down_arrow,0)
                View.VISIBLE
            }
        }
    }

    private fun setChipData(chipList: List<String>, chipGroup: ChipGroup) {
        for (text in chipList) {
            val chip = layoutInflater.inflate(R.layout.item_chip_rec_search, chipGroup, false) as Chip
            chip.text = text
            chipGroup.addView(chip)
        }
        val chip = chipGroup.getChildAt(0) as Chip
        chip.isChecked = true

    }

    private fun addSingleChip(text: String, chipGroup: ChipGroup) {
        val chip = layoutInflater.inflate(R.layout.skill_chip, chipGroup, false) as Chip
        chip.text = text
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
        }
        chipGroup.addView(chip)
    }

    private fun setRadioData(radioList: List<String>, radioGroup: RadioGroup) {
        for (text in radioList) {
            val rad = layoutInflater.inflate(R.layout.item_radio_button_rec_search, radioGroup, false) as RadioButton
            rad.text = text
            radioGroup.addView(rad)
        }
        val first: RadioButton = radioGroup.getChildAt(0) as RadioButton
        first.isChecked = true
    }

}
//
//sideSheetBinding.filterSkillEdTxt.editText?.setOnKeyListener { p0, p1, p2 ->
//    if (p2?.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER ) {
//        Log.d(LOG_TAG, "Entered key")
////                val chip = ChipDrawable.createFromResource(mContext, R.xml.skill_chip)
////                chip.setBounds(0, 0, chip.intrinsicWidth, chip.intrinsicHeight)
////                val span = ImageSpan(chip)
////                val text = editText!!.text
////                text.setSpan(span, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    } else if (p1 == IME_ACTION_DONE) {
//        Log.d(LOG_TAG, "enter p223 Entered key")
//    } else if (p1 == KeyEvent.KEYCODE_ENTER) {
//        Log.d(LOG_TAG, "enter p1 Entered key")
//    } else {
//        Log.d(LOG_TAG, "not entered")
//    }
//    false
//}
