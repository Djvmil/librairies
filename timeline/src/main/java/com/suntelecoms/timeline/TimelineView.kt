package com.suntelecoms.timeline

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.graphics.drawable.DrawableCompat
import com.suntelecoms.timeline.models.ItemTimeline
import com.suntelecoms.timeline.models.Sequence
import kotlinx.android.synthetic.main.view_first.view.*
import kotlinx.android.synthetic.main.view_first.view.textview
import kotlinx.android.synthetic.main.view_last.view.last_view
import kotlinx.android.synthetic.main.view_middle.view.view0
import kotlinx.android.synthetic.main.view_middle.view.view1
import kotlinx.android.synthetic.main.view_middle.view.view2
import java.text.SimpleDateFormat
import java.util.*

class TimelineView: LinearLayout {

    //Container
    private lateinit var rootLayout: LinearLayout
    private lateinit var inflater: LayoutInflater
    var separateLength = 4
    var nbTimeLine = 13
    var startNumber = 8
    var endNumber = 21
    var minNbTimeline = 9
    var margeNbTimeline = 2
    var unit:String = "h"
    var hour = 8
    private var listTimeline: ArrayList<ItemTimeline> = arrayListOf()
    private var position: Int = 0

    constructor(context: Context) : super(context) {
//        Log.d(TAG, "TimelineView(context) called")
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        Log.d(TAG, "TimelineView(context, attrs) called")

        val attr: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TimelineView, 0, 0)
        try {

            reloadTimeline()
//            separateLength = attr.getInteger(R.styleable.TimelineView_tl_separateLength, separateLength)
//            grisPassHour = attr.getBoolean(R.styleable.TimelineView_tl_separateLength, separateLength)
//            nbTimeLine = attr.getInteger(R.styleable.TimelineView_tl_nbTimeLine, nbTimeLine)
//            startNumber = attr.getInteger(R.styleable.TimelineView_tl_startNumber, startNumber)
//            unit = attr.getString(R.styleable.TimelineView_tl_unit)
//            unit = if (unit == "null") "h" else unit
//            defaultColor = attr.getColor(R.styleable.TimelineView_tl_default_color, R.color.gray_btn_bg_color)

        } finally {
            attr.recycle()
            init()
        }
    }


    /**
     * init
     */
    private fun init() {
        //Load RootView from xml
        View.inflate(context, R.layout.timeline_content, this)
        inflater = LayoutInflater.from(context)
//        Log.d(TAG, "Inflation started from constructor.")
        rootLayout = findViewById(R.id.container)

        loadTimeline()
    }

    private fun loadTimeline(){

        hour = dateToString(now, "HH").toInt()
        var nbTmp = startNumber
        position = startNumber
        var order = 0


        while (nbTmp <= (startNumber + nbTimeLine)){
            when {
                startNumber == nbTmp -> {
                    val linearLayout = addViewLine(getViewLine(VIEW_FIRST, nbTmp), true, "$nbTmp$unit")
                    listTimeline.add(ItemTimeline(id = linearLayout["first_view"]!!.toInt(), idParent = position, order = order))
                    order++

                }
                nbTmp == (startNumber + nbTimeLine) -> {

                    val linearLayout = addViewLine(getViewLine(VIEW_MIDDLE, nbTmp))
                    listTimeline.add(ItemTimeline(id = linearLayout["view0"]!!.toInt(), idParent = position, order = order))
                    order++

                    val linearLayout1 = addViewLine(getViewLine(VIEW_MIDDLE, nbTmp))
                    listTimeline.add(ItemTimeline(id = linearLayout1["view0"]!!.toInt(), idParent = position, order = order))
                    order++

                    val linearLayout2 = addViewLine(getViewLine(VIEW_LAST, nbTmp), true, "$nbTmp$unit")
                    listTimeline.add(ItemTimeline(id = linearLayout2["last_view"]!!.toInt(), idParent = position, order = order))
                    order++

                }
                else -> {

                    val linearLayout = addViewLine(getViewLine(VIEW_MIDDLE, nbTmp))
                    listTimeline.add(ItemTimeline(id = linearLayout["view0"]!!.toInt(), idParent = position, order = order))
                    order++

                    val linearLayout1 = addViewLine(getViewLine(VIEW_MIDDLE, nbTmp))
                    listTimeline.add(ItemTimeline(id = linearLayout1["view0"]!!.toInt(), idParent = position, order = order))
                    order++

                    val linearLayout2 = addViewLine(getViewLine(VIEW_MIDDLE_LINE, nbTmp), true, "$nbTmp")
                    listTimeline.add(ItemTimeline(id = linearLayout2["view1"]!!.toInt(), idParent = position, order = order))
                    position++
                    order = 0

                    listTimeline.add(ItemTimeline(id = linearLayout2["view2"]!!.toInt(), idParent = position, order = order))
                    order++
                }
            }

            if (nbTimeLine == 1){

                val linearLayout = addViewLine(getViewLine(VIEW_MIDDLE, nbTmp))
                listTimeline.add(ItemTimeline(id = linearLayout["view0"]!!.toInt(), idParent = position, order = order))
                order++

                val linearLayout1 = addViewLine(getViewLine(VIEW_MIDDLE, nbTmp))
                listTimeline.add(ItemTimeline(id = linearLayout1["view0"]!!.toInt(), idParent = position, order = order))
                order++

                val linearLayout2 = addViewLine(getViewLine(VIEW_MIDDLE_LINE, nbTmp), true, "$nbTmp$unit")
                listTimeline.add(ItemTimeline(id = linearLayout2["view1"]!!.toInt(), idParent = position, order = order))
                position++
                order = 0

                listTimeline.add(ItemTimeline(id = linearLayout2["view2"]!!.toInt(), idParent = position, order = order))
                order++
            }

            nbTmp++
        }

        try {
            listTimeline.forEach{
                setTint(rootLayout.findViewById<View>(it.id).background, Color.parseColor("#02f023"))
            }

        }catch (ex: Exception){
            Log.e(TAG, "loadTimeline: Exception" )
            ex.printStackTrace()
        }

    }

    private fun reloadTimeline() {
        getDateNow.invoke()
        Log.e(TAG, "reloadTimeline: date hour = $now" )
        hour = dateToString(now, "HH").toInt()
        Log.e(TAG, "reloadTimeline: startNumber = $startNumber" )
        Log.e(TAG, "reloadTimeline: nbTimeLine = $nbTimeLine" )
        Log.e(TAG, "reloadTimeline: hour = $hour" )

//        if (hour >= 8 && hour > startNumber + margeNbTimeline){
//            startNumber = hour - margeNbTimeline

        if (hour > 8 && hour in 8 until 21 && startNumber in 8 until 21){

            startNumber = if ((hour - startNumber) < margeNbTimeline)
                hour - 1
            else
                hour - margeNbTimeline


            Log.e(TAG, "reloadTimeline: startNumber0 = $startNumber" )

            val num = startNumber + nbTimeLine
            val numTl = 21 - startNumber

            if(num > 21){
                val value = num - 21
                if (numTl < 9){
                    startNumber -= (9 - numTl)
                    nbTimeLine = 9
                }
                else
                    nbTimeLine -= value
            }
            Log.e(TAG, "reloadTimeline1: numTl = $numTl" )
            Log.e(TAG, "reloadTimeline1: num = $num" )
        } else
            nbTimeLine = 13


        Log.e(TAG, "reloadTimeline1: startNumber = $startNumber" )
        Log.e(TAG, "reloadTimeline1: nbTimeLine = $nbTimeLine" )
    }

    private fun getViewLine(typeView: Int, nbTmp: Int): LinearLayout {
        when(typeView){
            VIEW_FIRST -> {
                return inflater.inflate(R.layout.view_first, null, true) as LinearLayout
            }
            VIEW_LAST -> {
                return inflater.inflate(R.layout.view_last, null, true) as LinearLayout
            }
            VIEW_MIDDLE -> {
                val viewMiddle = inflater.inflate(R.layout.view_middle, null, true) as LinearLayout
                viewMiddle.findViewById<View>(R.id.view0).visibility = VISIBLE
                viewMiddle.findViewById<LinearLayout>(R.id.middle_layout).visibility = GONE

                return viewMiddle
            }
            VIEW_MIDDLE_LINE -> {
                val viewMiddleLine = inflater.inflate(R.layout.view_middle, null, true) as LinearLayout
                viewMiddleLine.findViewById<View>(R.id.view0).visibility = GONE
                viewMiddleLine.findViewById<LinearLayout>(R.id.middle_layout).visibility = VISIBLE

                return viewMiddleLine

            } else -> {
            return inflater.inflate(R.layout.view_last, null, true) as LinearLayout
        }
        }
    }

    fun refreshTimeline(list: Map<Int, ItemTimeline>){
        list.forEach{ map ->
            val listFilter =  listTimeline.filter { map.key == it.idParent }
            if (listFilter.isNotEmpty())
                listFilter.forEach{
                    if (it.order >= map.value.startBlock && it.order <= map.value.endBlock){
                        setTint(rootLayout.findViewById<View>(it.id).background, Color.parseColor(map.value.colorString))
                    }
                }
        }

    }

    private fun addViewLine(linearLayout: LinearLayout? = null, showTmp: Boolean = false, i: String = "0"): Map<String, Int?> {

        linearLayout?.id = Sequence.nextValue()

        if (showTmp)
            linearLayout?.textview?.text = i

        val ids = mapOf("first_view" to Sequence.nextValue(),
                "last_view" to Sequence.nextValue(),
                "view0" to Sequence.nextValue(),
                "view1" to Sequence.nextValue(),
                "view2" to Sequence.nextValue())

        linearLayout?.first_view?.id = ids["first_view"]!!.toInt()
        linearLayout?.last_view?.id  = ids["last_view"]!!.toInt()
        linearLayout?.view0?.id      = ids["view0"]!!.toInt()
        linearLayout?.view1?.id      = ids["view1"]!!.toInt()
        linearLayout?.view2?.id      = ids["view2"]!!.toInt()

        rootLayout.addView(linearLayout)

        return ids
    }

    private fun setTint(d: Drawable, color: Int): Drawable {
        val wrappedDrawable: Drawable = DrawableCompat.wrap(d)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    companion object{
        @JvmStatic
        fun dateToString(date: Date?, format: String?, locale: Locale? = Locale.getDefault()): String {
            return try {
                if (date != null) {
                    SimpleDateFormat(format, locale).format(date).toString()
                } else {
                    ""
                }
            } catch (e: Exception) {
                ""
            }
        }

        @kotlin.jvm.JvmField
        var now = Date()
        var getDateNow: () -> Unit = {}

        private const val TAG = "TimelineView"
        private const val VIEW_FIRST = 1
        private const val VIEW_MIDDLE = 2
        private const val VIEW_MIDDLE_LINE=  3
        private const val VIEW_LAST = 4
    }

}
