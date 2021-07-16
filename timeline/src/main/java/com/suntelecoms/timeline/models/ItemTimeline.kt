package com.suntelecoms.timeline.models

import androidx.annotation.ColorRes
import com.suntelecoms.timeline.R

class ItemTimeline (
        var id: Int,
        @ColorRes private var  color: Int = R.color.main_disabled_color,
        var idParent: Int
        ){
        override fun toString(): String {
                return "ItemTimeline(id=$id, color=$color, idParent=$idParent)"
        }
}