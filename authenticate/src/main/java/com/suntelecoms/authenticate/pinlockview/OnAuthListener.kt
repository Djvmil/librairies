package com.suntelecoms.authenticate.pinlockview

/**
 *
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
interface OnAuthListener {

    fun onSuccess(pin: String?, success: Boolean)

    fun onError(msg: String?)
}