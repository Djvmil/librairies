package com.djamil.takephoto.facedetector

class SizeUtil {
    var width = 0
    var height = 0

    constructor() {}
    constructor(w: Int, h: Int) {
        width = w
        height = h
    }

    constructor(p: SizeUtil) {
        width = p.width
        height = p.height
    }

    operator fun set(w: Int, h: Int) {
        width = w
        height = h
    }

    fun set(d: SizeUtil) {
        width = d.width
        height = d.height
    }

    fun equals(w: Int, h: Int): Boolean {
        return width == w && height == h
    }

    override fun equals(o: Any?): Boolean {
        return o is SizeUtil && (o === this || equals(o.width, o.height))
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}