package com.suntelecoms.authenticate.pinlockview;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 8/22/20
 */
@IntDef({IndicatorType.FIXED, IndicatorType.FILL, IndicatorType.FILL_WITH_ANIMATION})
@Retention(RetentionPolicy.SOURCE)
public @interface IndicatorType {
    int FIXED = 0;
    int FILL = 1;
    int FILL_WITH_ANIMATION = 2;
}
