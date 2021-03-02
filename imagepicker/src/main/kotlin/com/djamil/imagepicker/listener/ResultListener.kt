package com.djamil.imagepicker.listener

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 4/15/20
 */
internal interface ResultListener<T> {

    fun onResult(t: T?)
}
