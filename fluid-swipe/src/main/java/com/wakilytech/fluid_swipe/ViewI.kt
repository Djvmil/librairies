package com.wakilytech.fluid_swipe

import android.graphics.Bitmap

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/4/20
 */

interface ViewI {
    fun getBitmap(direction: Int) : Bitmap?
    fun getBitmapAt(position: Int) : Bitmap?
    fun getCount(): Int
    fun redraw()
    fun switchPage(direction: Int)
    fun getCurrentItem(): Int
    fun blockInput(block: Boolean)
}