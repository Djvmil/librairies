package com.suntelecoms.authenticate.util

import android.annotation.TargetApi
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import androidx.appcompat.widget.AppCompatImageView

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
object Animate {
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.M)
    fun animate(view: AppCompatImageView, scanFingerprint: AnimatedVectorDrawable) {
        view.setImageDrawable(scanFingerprint)
        scanFingerprint.start()
    }
}