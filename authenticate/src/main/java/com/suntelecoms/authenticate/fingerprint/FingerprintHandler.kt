package com.suntelecoms.authenticate.fingerprint

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.core.app.ActivityCompat

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandler(private val context: Context) : FingerprintManager.AuthenticationCallback() {
    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//
    private var cancellationSignal: CancellationSignal? = null
    private var mfingerPrintListener: FingerPrintListener? = null

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//
    fun startAuth(manager: FingerprintManager, cryptoObject: FingerprintManager.CryptoObject?) {
        cancellationSignal = CancellationSignal()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    fun setFingerPrintListener(mfingerPrintListener: FingerPrintListener?) {
        this.mfingerPrintListener = mfingerPrintListener
    }

    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//
    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        mfingerPrintListener!!.onError(errString)
    }

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//
    override fun onAuthenticationFailed() {
        mfingerPrintListener!!.onFailed()
    }

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        mfingerPrintListener!!.onHelp(helpString)
    }

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        mfingerPrintListener!!.onSuccess()
    }

}