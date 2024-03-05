package com.bitspan.bitsjobkaro.ui.mainFragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.R
import com.bitspan.bitsjobkaro.data.constants.Constant.userId
import com.bitspan.bitsjobkaro.data.constants.Constant.userType
import com.bitspan.bitsjobkaro.databinding.FragmentUploadImageBinding
import com.bitspan.bitsjobkaro.presentation.adapters.UploadImageAdapter
import com.bitspan.bitsjobkaro.presentation.viewmodels.OtherViewModel
import com.bitspan.bitsjobkaro.presentation.viewmodels.recruiter.RecruiterNewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UploadImageFragment : Fragment() {

    private lateinit var binding: FragmentUploadImageBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private val uploadImageArgs: UploadImageFragmentArgs by navArgs()
    private val otherVM: OtherViewModel by viewModels()
    private val recNewVM: RecruiterNewViewModel by viewModels()
    private lateinit var uploadAdapter: UploadImageAdapter
    private var imageSelectedPos = -1
    private var imagePath: String? = null

    private val profileContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            imagePath = CommonUiFunctions.getRealPathFromURI(it, mContext)
            loadImage(imagePath!!)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImage(uploadImageArgs.defaultImageUrl)


        val imageList = resources.obtainTypedArray(R.array.uploadImageList)
        uploadAdapter = UploadImageAdapter(imageList) {
            imageSelectedPos = it
            imagePath = null
            binding.rProfMainImg.setImageDrawable(imageList.getDrawable(imageSelectedPos))
        }

        binding.imageRecView.adapter = uploadAdapter


        binding.rProfDesEditImg.setOnClickListener {
            profileContract.launch("image/*")
        }

        binding.saveBtn.setOnClickListener {
            binding.progBar.visibility = View.VISIBLE
            binding.saveBtn.isEnabled = false
            if (imagePath == null) {
                val file = getFile(imageList.getResourceId(imageSelectedPos, R.drawable.avataar_first))
                if (userType) {
                    updateProfile(file)
                } else {
                    updateRecLogo(file)
                }
            } else {
                if (userType) {
                    updateProfile(null)
                } else {
                    updateRecLogo(null)
                }
            }
        }

    }


    private fun updateProfile(file: File?) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                otherVM.postEmpProfileImage(
                    uploadPic = if (file != null) CommonUiFunctions.multiPartDrawable(file, "uploadPic")
                    else CommonUiFunctions.multiPartImage(imagePath!!, "uploadPic"),
                    empId = CommonUiFunctions.multiPartText(userId)
                ).let {
                    binding.progBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Profile image updated", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to upload profile image, please try again") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to upload profile image, please try again later") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error, Unable to upload profile image, please try again later") {
                    findNavController().popBackStack()
                }
            }
        }
    }


    private fun updateRecLogo(file: File?) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                recNewVM.postRecProfileLogo(
                    c_logo = if (file != null) CommonUiFunctions.multiPartDrawable(file, "c_logo")
                    else CommonUiFunctions.multiPartImage(imagePath!!, "c_logo"),
                    recId = CommonUiFunctions.multiPartText(userId)
                ).let {
                    binding.progBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                    if (it.isSuccessful && it.body() != null) {
                        if (it.body()!!.status == 200) {
                            Toast.makeText(mContext, "Logo image updated", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }
                        else {
                            CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to upload logo image, please try again") {
                            }
                        }
                    } else {
                        CommonUiFunctions.showErrorMsgDialog(mContext, "Unable to upload logo image, please try again later") {
                            findNavController().popBackStack()
                        }
                    }
                }
            } catch (e: Exception) {
                binding.progBar.visibility = View.GONE
                binding.saveBtn.isEnabled = true
                CommonUiFunctions.showErrorMsgDialog(mContext, "Some technical error, Unable to upload logo image, please try again later") {
                    findNavController().popBackStack()
                }
            }
        }
    }



    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.avataar_placeholder)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image in both memory and disk
            .into(binding.rProfMainImg)
    }

    private fun getFile(drawable: Int): File {
        val drawableBitmap = BitmapFactory.decodeResource(resources, drawable)
        val file = File(context?.cacheDir, "${drawable}.png")
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        drawableBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return file
    }

}