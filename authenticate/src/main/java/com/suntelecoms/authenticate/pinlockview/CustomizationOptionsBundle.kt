package com.suntelecoms.authenticate.pinlockview

import android.graphics.drawable.Drawable

/**
 * The customization options for the buttons in [PinLockView]
 * passed to the [PinLockAdapter] to decorate the individual views
 *
 *
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
data class CustomizationOptionsBundle(
        var textColor:Int = 0,
        var textSize:Int = 0,
        var buttonSize:Int = 0,
        var buttonBackgroundDrawable: Drawable? = null,
        var deleteButtonDrawable: Drawable? = null,
        var deleteButtonWidthSize:Int = 0,
        var deleteButtonHeightSize:Int = 0,
        var isShowDeleteButton: Boolean = false,
        var deleteButtonPressesColor:Int = 0
)