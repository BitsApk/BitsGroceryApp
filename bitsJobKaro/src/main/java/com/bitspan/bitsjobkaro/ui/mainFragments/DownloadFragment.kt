package com.bitspan.bitsjobkaro.ui.mainFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bitspan.bitsjobkaro.databinding.FragmentDownloadBinding
import com.bitspan.bitsjobkaro.presentation.viewmodels.CommonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val downArgs: DownloadFragmentArgs by navArgs()
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private var docUrl:String =""
    private var docName:String = ""
    private val commonVM: CommonViewModel by viewModels()
    private var uri: Uri? = null
    private var firstTime: Boolean = false
    private var observeFirstTym = true

//    private lateinit var dialogHelper: DialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firstTime = true
    }

    private val openDirectoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Directory access granted, start downloading

                    commonVM.getStorageLoc().removeObservers(viewLifecycleOwner)
                    saveSelectedDirectoryUri(uri)
                    takePersistableUriPermission(uri)
                    downloadPdf(uri)
                }
            } else {
                Toast.makeText(mContext,"Failed to access the Downloads directory.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mContext = requireContext()
        mActivity = requireActivity()
        binding = FragmentDownloadBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        dialogHelper = DialogHelper(mContext)
        docName = downArgs.docName
        docUrl = downArgs.docUrl

        if (firstTime) {
            requestDownloadsAccess()
            firstTime = false
        }
    }


    private fun takePersistableUriPermission(uri: Uri) {
        val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        mActivity.contentResolver.takePersistableUriPermission(uri, takeFlags)
    }

    private fun hasPersistableUriPermission(uri: Uri): Boolean {
        val persistedUriPermissions = mActivity.contentResolver.persistedUriPermissions
        return persistedUriPermissions.any { it.uri == uri }
    }

    private fun saveSelectedDirectoryUri(directoryUri: Uri) {
        commonVM.saveStorageLoc(directoryUri.toString())
    }

    private fun requestDownloadsAccess() {
        binding.progBar.visibility = View.VISIBLE
        commonVM.getStorageLoc().observe(viewLifecycleOwner) {
            if (it?.isEmpty() != true) uri = Uri.parse(it)
            if (observeFirstTym) {
                observeFirstTym = false
                if (uri != null) {
                    if (hasPersistableUriPermission(uri!!)) {
                        downloadPdf(uri!!)
                    }
                    else {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                        openDirectoryLauncher.launch(intent)
                    }
                } else {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                    openDirectoryLauncher.launch(intent)

                }
            }

        }
    }

    private fun downloadPdf(directoryUri: Uri) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            var outputStream: FileOutputStream? = null

            try {
                val url = URL(docUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    withContext(Dispatchers.Main) {
                        binding.progBar.visibility = View.GONE
                        Toast.makeText(
                            mContext,
                            "Failed to download PDF.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    findNavController().popBackStack()
                }

                val inputStream = connection.inputStream
                val documentFile = DocumentFile.fromTreeUri(mContext, directoryUri)
                val fileName = docName
                Log.d("Rishabh", "File name $fileName")
                val outputFile = documentFile?.uri?.let {
                    DocumentsContract.createDocument(
                        mActivity.contentResolver,
                        it,
                        getMimeType(fileName),
                        fileName
                    )?.let {u ->
                        DocumentFile.fromSingleUri(
                            mContext,
                            u
                        )
                    }
                }
                if (outputFile != null) {
                    outputStream = mActivity.contentResolver.openOutputStream(outputFile.uri) as FileOutputStream?
                    outputStream?.let { outStream ->
                        val buffer = ByteArray(4096)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outStream.write(buffer, 0, bytesRead)
                        }
                        outStream.close()

                        withContext(Dispatchers.Main) {
                            binding.progBar.visibility = View.GONE
                            Toast.makeText(
                                mContext,
                                "Document downloaded successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }
                    }
                }else {
                    withContext(Dispatchers.Main) {
                        binding.progBar.visibility = View.GONE
                        Toast.makeText(
                            mContext,
                            "Failed to access the Downloads directory.",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    binding.progBar.visibility = View.GONE
                    Toast.makeText(
                        mContext,
                        "Failed to download document.",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
            } finally {
                try {
                    outputStream?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                connection?.disconnect()
            }
        }
    }

    fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "")
        return when (extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "pdf" -> "application/pdf"
            else -> "application/octet-stream" // Default MIME type for unknown file types
        }
    }
}