package com.djamil.suntelecom.utilities.animation

import android.view.animation.Animation

inline fun Animation.setAnimationListener(
    func: __AnimationListener.() -> Unit) {
    val listener = __AnimationListener()
    listener.func()
    setAnimationListener(listener)
}