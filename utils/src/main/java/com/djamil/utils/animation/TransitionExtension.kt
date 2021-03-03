package com.djamil.utils.animation

import androidx.transition.Transition


inline fun Transition.addListener(
    func: __TransitionAnimationListener.() -> Unit) {
    val listener = __TransitionAnimationListener()
    listener.func()
    addListener(listener)
}