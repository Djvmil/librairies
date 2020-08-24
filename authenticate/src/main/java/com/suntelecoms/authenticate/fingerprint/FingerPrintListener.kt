package com.suntelecoms.authenticate.fingerprint

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
interface FingerPrintListener {
    fun onSuccess()
    fun onFailed()
    fun onError(errorString: CharSequence?)
    fun onHelp(helpString: CharSequence?)
}