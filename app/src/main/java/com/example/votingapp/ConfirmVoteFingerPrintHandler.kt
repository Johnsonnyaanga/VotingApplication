package com.example.votingapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class ConfirmVoteFingerPrintHandler(private val context: Context): FingerprintManager.AuthenticationCallback() {
    fun startAuth(
            fingerprintManager: FingerprintManager?,
            cryptoObject: FingerprintManager.CryptoObject?
    ) {
        val cancellationSignal = CancellationSignal()
        fingerprintManager?.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        update("There was an Auth Error. $errString", false)
    }

    override fun onAuthenticationFailed() {
        update("Auth Failed. ", false)
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
        update("Error: $helpString", false)
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        update("Vote confirmed.", true)
        Handler().postDelayed({
            val intent = Intent(context as ConfrimvoteFingerPrint,VoterDashboard::class.java)
            ContextCompat.startActivity(context, intent, null)
        },200)



    }

    private fun update(s: String, b: Boolean) {
        val paraLabel = (context as Activity).findViewById<View>(R.id.paraLabel) as TextView
        val imageView = context.findViewById<View>(R.id.fingerprintImage) as ImageView
        paraLabel.text = s




        if (b == false) {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_primary_variant))
        } else {
            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.design_default_color_secondary_variant))
            imageView.setImageResource(R.drawable.ic_done)
        }
    }
}