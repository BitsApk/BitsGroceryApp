package com.bitspan.bitsjobkaro.ui.subFragment.empEditProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitspan.bitsjobkaro.CommonDataFunctions
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant
import com.bitspan.bitsjobkaro.data.models.employee.AddCurrentCompanyReq
import com.bitspan.bitsjobkaro.data.models.employee.AddPreviousCompanyReq
import com.bitspan.bitsjobkaro.data.models.employee.AllPreCom
import com.bitspan.bitsjobkaro.data.models.employee.CurrDetail
import com.bitspan.bitsjobkaro.databinding.FragmentEditEmpProffDetailBinding
import com.bitspan.bitsjobkaro.presentation.adapters.EmpPreviousComListAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.EmpProffessionalViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.ui.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditEmpProffDetailFragment : Fragment() {

    private lateinit var binding: FragmentEditEmpProffDetailBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val otherVM: OtherViewModel by viewModels()
    private lateinit var currentComDetails: CurrDetail
    private lateinit var preComDetails: AllPreCom
    private lateinit var dialogHelper : DialogHelper
    private val empProfViewModel: EmpProffessionalViewModel by activityViewModels()
    private var isCurrentlyWorking = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentEditEmpProffDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogHelper = DialogHelper(mContext)
        CommonUiFunctions.apply {
            changeStatusBarColor(mActivity, R.color.edit_profile)
            bottomNavBarVisibility(mActivity, View.GONE)
        }

        setNoticePerioudSpinner()
        getAllProfessDetails()

        binding.apply {
            eEditYesBtn.setOnClickListener {
                isCurrentlyWorking = true
                setCurrentComData()
                setNoticeVisibility(true)
            }
            eEditNoBtn.setOnClickListener {
                isCurrentlyWorking = false
                setOldComData()
                setNoticeVisibility(false)
            }
            eEditFromEdTxt.editText?.setOnClickListener {
                dialogHelper.showCalendar {date  ->
                    binding.eEditFromEdTxt.editText?.setText(date)
                }
            }
            eEditToEdTxt.editText?.setOnClickListener {
                dialogHelper.showCalendar {date  ->
                    binding.eEditToEdTxt.editText?.setText(date)
                }
            }
            eEditUpdateBtn.setOnClickListener {
                if (checkFields()) {
                    addCurrentCompany()
                }
            }
        }
    }


    private fun setNoticePerioudSpinner() {
        val noticeAdapter = ArrayAdapter.createFromResource(mContext, R.array.empNoticePerList, R.layout.item_drop_down)
        (binding.eEditNoticeEdTxt.editText as? AutoCompleteTextView)?.setAdapter(noticeAdapter)

        binding.eEditAddPrevCompBtn.setOnClickListener {
            dialogHelper.showAddPrevComp { name, yourDesig, from, to ->
               addPrevCompany(name,yourDesig,from,to)
            }
        }
    }

    private fun addCurrentCompany() {
        binding.progBar.visibility = View.VISIBLE

        val req = AddCurrentCompanyReq()
        binding.apply {
            req.empId = Constant.userId.toInt()
            req.currComName = eEditCompEdTxt.editText?.text.toString()
            req.currComDesig = eEditComDesigEdTxt.editText?.text.toString()
            req.currDFrom = eEditFromEdTxt.editText?.text.toString()
            req.currDTo = eEditToEdTxt.editText?.text.toString()
            req.formid = currentComDetails.id
            req.currNotice = if (isCurrentlyWorking) CommonDataFunctions.postNoticePeriod(eEditNoticeEdTxt.editText?.text.toString()) else null
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.addCurrentProfDetails(req).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Current Company Details Updated", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Something went wrong") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Something went wrong") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Something went wrong"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun addPrevCompany(name: String, desig: String, from: String, to: String) {
        binding.progBar.visibility = View.VISIBLE

        val req = AddPreviousCompanyReq()
        binding.apply {
            req.empId = Constant.userId.toInt()
            req.preComName = name
            req.preComDesig = desig
            req.preDFrom =from
            req.preDTo = to
//            req.formid = preComDetails.id
        }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.savePreviousComDetails(req).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Previous Company Details Updated", Toast.LENGTH_SHORT).show()
                            getAllProfessDetails()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Something went wrong") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Something went wrong") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Something went wrong"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun deletePrevCompany(id: String, pos: Int) {
        binding.progBar.visibility = View.VISIBLE

        val req = AddPreviousCompanyReq()
        req.id = id

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.deletePreDetails(req).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, it.body()?.message?:"Data Deleted Successfully", Toast.LENGTH_SHORT).show()
//                            binding.eEditPrevRecView.adapter?.notifyItemRemoved(pos)
                            getAllProfessDetails()
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Something went wrong") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Something went wrong") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Something went wrong"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun getAllProfessDetails() {
        binding.progBar.visibility = View.VISIBLE

        val req = AddCurrentCompanyReq()
        req.empId = Constant.userId.toInt()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.getAllProfessDetails(req).let {
                    binding.progBar.visibility = View.GONE
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            currentComDetails = it.body()?.currDetails?.getOrNull(0) ?: CurrDetail()
                            preComDetails = it.body()?.allPreCom?.getOrNull(0) ?: AllPreCom()
                            setCurrentComData()
                            binding.eEditPrevRecView.adapter = EmpPreviousComListAdapter(it.body()?.allPreCom?: listOf()){id,pos->
                                deletePrevCompany(id,pos)
                            }
                        } else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, it.body()!!.message ?: "Something went wrong") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Something went wrong") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                CommonUiFunctions.showErrorMsgDialog(
                    mContext,
                    "Something went wrong"
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setCurrentComData(){
        binding.apply {
            val fromDate = currentComDetails.currDFrom?.split(" ")?.get(0)
            val toDate = currentComDetails.currDTo?.split(" ")?.get(0)
            eEditCompEdTxt.editText?.setText(currentComDetails.currComName)
            eEditComDesigEdTxt.editText?.setText(currentComDetails.currComDesig)
            eEditFromEdTxt.editText?.setText(fromDate)
            eEditToEdTxt.editText?.setText(toDate)
           if (currentComDetails.currNotice?.isNotEmpty() == true) eEditNoticeEdTxt.editText?.setText(CommonDataFunctions.getNoticePeriod(currentComDetails.currNotice?.toInt()))
            setNoticePerioudSpinner()
        }
    }

    private fun setOldComData(){
        binding.apply {
            val fromDate = currentComDetails.oldDFrom?.split(" ")?.get(0)
            val toDate = currentComDetails.oldDTo?.split(" ")?.get(0)
            eEditCompEdTxt.editText?.setText(currentComDetails.oldComName)
            eEditComDesigEdTxt.editText?.setText(currentComDetails.oldComDesig)
            eEditFromEdTxt.editText?.setText(fromDate)
            eEditToEdTxt.editText?.setText(toDate)
        }
    }
    private fun checkFields(): Boolean {
        val name = binding.eEditCompEdTxt.editText?.text.toString()
        val desig = binding.eEditComDesigEdTxt.editText?.text.toString()
        val from = binding.eEditFromEdTxt.editText?.text.toString()
        val to = binding.eEditToEdTxt.editText?.text.toString()
        val notice = binding.eEditNoticeEdTxt.editText?.text.toString()

        val check = if (name.isBlank()) {
            resetErrorAndFields(name = "Name can't be empty")
            false
        } else if (desig.isBlank()) {
            resetErrorAndFields(desig = "Enter your designation")
            false
        } else if (from.isBlank()) {
            resetErrorAndFields(from = "Choose your starting date")
            false
        } else if (to.isBlank()) {
            resetErrorAndFields(to = "Choose your end date")
            false
        }
        else if (isCurrentlyWorking){
            if (notice.isBlank()) {
                resetErrorAndFields(notice = "Enter your current notice period")
                false
            }else true
        }
        else true
        return check
    }

    private fun resetErrorAndFields(
        name: String? = null,
        desig: String? = null,
        from: String? = null,
        to: String? = null,
        notice: String? = null
    ) {
        binding.eEditCompEdTxt.error = name
        binding.eEditComDesigEdTxt.error = desig
        binding.eEditFromEdTxt.error = from
        binding.eEditToEdTxt.error = to
        binding.eEditNoticeEdTxt.error = notice
    }


    private fun setNoticeVisibility(ifYes: Boolean) {
        setButtonBackground(ifYes)
        val visible = if (ifYes) {
            binding.eEditCompField.text = getString(R.string.current_company_nameh)
            View.VISIBLE
        } else {
            binding.eEditCompField.text = getString(R.string.prev_company_nameh)
            View.GONE
        }
        binding.eEditNoticeField.visibility = visible
        binding.eEditNoticeEdTxt.visibility = visible
    }

    private fun setButtonBackground(ifYes: Boolean) {
        if (ifYes) {
            binding.eEditYesBtn.apply {
                setTextColor(ContextCompat.getColor(mContext, R.color.white))
                setBackgroundResource(R.drawable.bg_button_login)
            }
            binding.eEditNoBtn.apply {
                setTextColor(ContextCompat.getColor(mContext, R.color.text_heading))
                setBackgroundResource(R.drawable.bg_corner_solid_grey)
            }
        } else {
            binding.eEditYesBtn.apply {
                setTextColor(ContextCompat.getColor(mContext, R.color.text_heading))
                setBackgroundResource(R.drawable.bg_corner_solid_grey)
            }
            binding.eEditNoBtn.apply {
                setTextColor(ContextCompat.getColor(mContext, R.color.white))
                setBackgroundResource(R.drawable.bg_button_login)
            }
        }
    }

}