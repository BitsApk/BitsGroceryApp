package com.bitspanindia.groceryapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.bitspanindia.groceryapp.data.model.PaymentResponseModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var postData:String


    var response: PaymentResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        webView = findViewById(R.id.webView)
        configureWebView()
        loadPaymentPage()

    }

    private fun loadPaymentPage() {
//        val paymentPageUrl = resources.getString(R.string.payment_page_url)

        // For testing using 1 rupees
        val paymentPageUrl = resources.getString(R.string.testing_pay_url)


        if (intent.extras!=null){

            val orderId = intent.getStringExtra("ORDER_ID")
            val mid = intent.getStringExtra("MID")
            val webCallbackUrl = intent.getStringExtra("WEB_CALLBACK_URL")
            val convCharge = intent.getStringExtra("CONV_CHARGE")
            val mainAmount = intent.getStringExtra("MAIN_AMOUNT")
            val txtAmount = intent.getStringExtra("TXN_AMOUNT")
            val custId = intent.getStringExtra("CUST_ID")

            postData = "ORDER_ID=$orderId&MID=$mid&WEB_CALLBACK_URL=${this.resources.getString(R.string.payment_live_url)}&CONV_CHARGE=$convCharge&MAIN_AMOUNT=$mainAmount&TXN_AMOUNT=$txtAmount&CUST_ID=$custId"

            Log.e("TAG", "loadPaymentPagepaydata: $postData" )

        }

//        webView.postUrl(paymentPageUrl, EncodingUtils.getBytes(postData, "utf-8"))
        webView.postUrl(paymentPageUrl, postData.toByteArray(Charsets.UTF_8))

    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url != null && url.contains("pay24Response_android")) {
                    val uri = Uri.parse(url)
                    val infoValue:String? = uri.getQueryParameter("info")

                    Log.e("TAG", "url2: dcodedJsonResponse ${decodeHex(infoValue?:"")}" )

                    getAllStringValuesFromJson(decodeHex(infoValue?:""),infoValue)
//                    getAllStringValuesFromJson(decodeHexUrl(infoValue), infoValue)

                }

            }

        }
    }

    private fun getAllStringValuesFromJson(jsonString: String?, infoValue: String?) {
        try {
            response = Gson().fromJson(jsonString, PaymentResponseModel::class.java)


            val intent = Intent()
            intent.putExtra("orderId", response?.ORDER_ID)
            intent.putExtra("orderNo", response?.orderno)
            intent.putExtra("txnAmount", response?.TXN_AMOUNT)
            intent.putExtra("transId", response?.CUST_ID)
            intent.putExtra("status_code", response?.res_code)
            intent.putExtra("amount", response?.MAIN_AMOUNT)
            intent.putExtra("vpaid", response?.vpaid)
            intent.putExtra("status", response?.status)
            intent.putExtra("info",infoValue)
            intent.putExtra("json",jsonString)
            intent.putExtra("convCharge", response?.CONV_CHARGE)
            intent.putExtra("custRefNum",response?.cust_ref_no)
            setResult(Activity.RESULT_OK, intent)

        } catch (e: Exception) {
            Toast.makeText(this, "Invalid Upi Id", Toast.LENGTH_SHORT).show()
        }

        finish()

    }

    fun decodeHex(hex: String): String {
        val result = ByteArray(hex.length / 2)
        for (i in hex.indices step 2) {
            val firstDigit = Character.digit(hex[i], 16)
            val secondDigit = Character.digit(hex[i + 1], 16)
            if (firstDigit == -1 || secondDigit == -1) {
                throw IllegalArgumentException("Invalid hex string")
            }
            val byteValue = (firstDigit shl 4) + secondDigit
            result[i / 2] = byteValue.toByte()
        }
        return String(result, Charsets.UTF_8)
    }

}