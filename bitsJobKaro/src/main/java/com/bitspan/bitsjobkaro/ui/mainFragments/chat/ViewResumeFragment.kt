package com.bitspan.bitsjobkaro.ui.mainFragments.chat

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.bitspan.bitsjobkaro.CommonUiFunctions
import com.bitspan.bitsjobkaro.databinding.FragmentChatBinding
import com.bitspan.bitsjobkaro.databinding.FragmentViewResumeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ViewResumeFragment : Fragment() {

    private lateinit var mActivity: FragmentActivity
    private lateinit var mContext: Context
    private lateinit var binding: FragmentViewResumeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mActivity = requireActivity()
        mContext = requireContext()
        CommonUiFunctions.bottomNavBarVisibility(mActivity, View.GONE)
        binding = FragmentViewResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pdfUrl = "https://jobkaro.in/jd/jdem/canidate_documents/resume/17001191442024722469.pdf"

        downloadAndDisplayPdf(pdfUrl)
        // Load PDF from URL
    }

    private fun downloadAndDisplayPdf(pdfUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Use Glide to load the PDF directly into the PDFView
                Glide.with(mContext)
                    .asFile()
                    .load(pdfUrl)
                    .into(object : CustomTarget<File>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Not needed for this example
                            Toast.makeText(
                                requireContext(),
                                "Failed to load asdf PDF",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            // Handle the error
                            // You can log or display a toast message
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to load PDF",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                        override fun onResourceReady(
                            resource: File,
                            transition: Transition<in File>?
                        ) {
                            displayPdf(resource)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exception
            }
        }
    }

    private fun displayPdf(pdfFile: File) {
        Log.d("Rishabh", "PDF File Path: ${pdfFile.absolutePath}")
        // Pdf viewer fuctions doc has set in his default val   ues we can check there as well
        binding.pdfView.fromFile(pdfFile)
            .enableSwipe(true) // By def = True,  it allows to swiping between pages if set false we are not able to swipe neither horiz, nor vert
             // By def = false, allow to make pages horizontal if true else vertical by default
//            .pageSnap(true)
//            .autoSpacing(true) // By def = false, It add white space to fit page to screen like if page is small then to fit phone screen add extra white space
            // By def = false, allow swiping like viewpager if true, if false then pdf swipe like recycler view
//            .fitEachPage(true)
            .load()
    }

}