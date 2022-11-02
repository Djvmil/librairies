package com.djamil.takephoto.facedetector

data class Frame(
    @Suppress("ArrayInDataClass") val data: ByteArray?,
    val rotation: Int,
    val size: SizeUtil?,
    val format: Int,
    val lensFacing: LensFacing
)