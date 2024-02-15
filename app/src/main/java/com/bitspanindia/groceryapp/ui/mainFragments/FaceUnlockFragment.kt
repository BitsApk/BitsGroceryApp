package com.bitspanindia.groceryapp.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bitspanindia.groceryapp.R
import com.bitspanindia.groceryapp.databinding.FragmentFaceUnlockBinding
import com.bitspanindia.groceryapp.databinding.FragmentHomeBinding

class FaceUnlockFragment : Fragment() {

    val TAG = "Rishabh"

    private lateinit var binding: FragmentFaceUnlockBinding
    private lateinit var mContext: Context
    private lateinit var mActivity: FragmentActivity
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaceUnlockBinding.inflate(inflater, container, false)

        mContext = requireContext()
        mActivity = requireActivity()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        biometricPrompt = createBiometricPrompt()

        binding.btn.setOnClickListener {
            val promptInfo = createPromptInfo()
            biometricPrompt.authenticate(promptInfo)
            if (BiometricManager.from(mContext)
                    .canAuthenticate(DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                Log.d("Rishabh", "Can't auth")
//                loginWithPassword()
            }
        }

    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(mContext)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "$errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
//                    loginWithPassword() // Because in this app, the negative button allows the user to enter an account password. This is completely optional and your app doesn’t have to do it.
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "Authentication was successful")
                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show()
                // Proceed with viewing the private encrypted message.
//                showEncryptedMessage(result.cryptoObject)
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_info_title))
            .setSubtitle(getString(R.string.prompt_info_subtitle))
            // Authenticate without requiring the user to press a "confirm"
            // button after satisfying the biometric check
            .setConfirmationRequired(false)
            .setNegativeButtonText(getString(R.string.prompt_info_use_app_password))
            .build()

        return promptInfo
    }

}