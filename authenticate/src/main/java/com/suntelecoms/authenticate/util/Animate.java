package com.suntelecoms.authenticate.util;

import android.annotation.TargetApi;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
public class Animate {

    @TargetApi(Build.VERSION_CODES.M)
    public static void animate(AppCompatImageView view, AnimatedVectorDrawable scanFingerprint) {
        view.setImageDrawable(scanFingerprint);
        scanFingerprint.start();
    }
}
