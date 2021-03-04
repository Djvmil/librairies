package com.djamil.takephoto.listener

/**
 * @ref https://github.com/Dhaval2404/ImagePicker
 */
internal interface ResultListener<T> {

    fun onResult(t: T?)
}
