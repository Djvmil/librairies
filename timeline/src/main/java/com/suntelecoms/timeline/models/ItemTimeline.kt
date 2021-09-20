package com.suntelecoms.timeline.models

import androidx.annotation.ColorRes
import com.suntelecoms.timeline.R

class ItemTimeline (
        var id: Int,
        @ColorRes private var  color: Int = R.color.gray_btn_bg_color,
        var idParent: Int,
        var order: Int,
        var startBlock: Int = 0,
        var colorString: String = "#02f023",
        var endBlock: Int = 3

){
        override fun toString(): String {
                return "ItemTimeline(id=$id, color=$color, colorString=$colorString, idParent=$idParent, order=$order)"
        }
}