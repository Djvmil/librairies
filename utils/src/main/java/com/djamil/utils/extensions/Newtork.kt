package com.djamil.utils.extensions

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

fun String.toRequestBody(): RequestBody {
    return RequestBody.create("id/plain".toMediaTypeOrNull(), this)
}

fun Int.toRequestBody(): RequestBody {
    return RequestBody.create("id/plain".toMediaTypeOrNull(), this.toString())
}

fun Double.toRequestBody(): RequestBody {
    return RequestBody.create("id/plain".toMediaTypeOrNull(), this.toString())
}
