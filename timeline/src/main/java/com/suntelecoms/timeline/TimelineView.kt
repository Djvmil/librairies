package com.suntelecoms.timeline

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.graphics.drawable.DrawableCompat
import com.djamil.utils.Sequence
import com.suntelecoms.timeline.models.ItemTimeline
import kotlinx.android.synthetic.main.view_first.view.textview
import kotlinx.android.synthetic.main.view_first.view.first_view
import kotlinx.android.synthetic.main.view_last.view.last_view
import kotlinx.android.synthetic.main.view_middle.view.view0
import kotlinx.android.synthetic.main.view_middle.view.view1
import kotlinx.android.synthetic.main.view_middle.view.view2

class TimelineView: LinearLayout {

    //Container
    private lateinit var rootLayout: LinearLayout
    private lateinit var inflater: LayoutInflater
    var separateLength = 4
    var nbTimeLine = 4
    var startNumber = 0
    var unit = "h"
    private var listTimeline: ArrayList<ItemTimeline> = arrayListOf()
    private var position: Int = 0

    constructor(context: Context) : super(context) {
        Log.d(TAG, "TimelineView(context) called")
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Log.d(TAG, "TimelineView(context, attrs) called")

        val attr: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TimelineView, 0, 0)
        try {
            separateLength = attr.getInteger(R.styleable.TimelineView_tl_separateLength, separateLength)
            nbTimeLine = attr.getInteger(R.styleable.TimelineView_tl_nbTimeLine, nbTimeLine)
            startNumber = attr.getInteger(R.styleable.TimelineView_tl_startNumber, startNumber)
            unit = attr.getString(R.styleable.TimelineView_tl_unit).toString()
            unit = if (unit == "null") "h" else unit
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
        Log.d(TAG, "Inflation started from constructor.")
        rootLayout = findViewById(R.id.container)

        loadTimeline()
        //setTint(R.drawable.)
    }


    private fun loadTimeline(){
        var nbTmp = startNumber
        position = startNumber

        while (nbTmp <= (startNumber + nbTimeLine)){
            when {
                startNumber == nbTmp -> {
                    val linearLayout = addViewLine(getViewLine(VIEW_FIRST), true, nbTmp)
                    listTimeline.add(ItemTimeline(id = linearLayout["first_view"]!!.toInt(), idParent = position))

                }
                nbTmp == (startNumber + nbTimeLine) -> {
                    Log.e(TAG, "2e time: $nbTmp")

                    val linearLayout = addViewLine(getViewLine(VIEW_MIDDLE))
                    listTimeline.add(ItemTimeline(id =linearLayout["view0"]!!.toInt(), idParent = position))

                    val linearLayout1 = addViewLine(getViewLine(VIEW_MIDDLE))
                    listTimeline.add(ItemTimeline(id = linearLayout1["view0"]!!.toInt(), idParent = position))

                    val linearLayout2 = addViewLine(getViewLine(VIEW_LAST), true, nbTmp)
                    listTimeline.add(ItemTimeline(id = linearLayout2["last_view"]!!.toInt(), idParent = position))

                }
                else -> {
                    Log.e(TAG, "3e time: $nbTmp")

                    val linearLayout = addViewLine(getViewLine(VIEW_MIDDLE))
                    listTimeline.add(ItemTimeline(id = linearLayout["view0"]!!.toInt(), idParent = position))

                    val linearLayout1 = addViewLine(getViewLine(VIEW_MIDDLE))
                    listTimeline.add(ItemTimeline(id = linearLayout1["view0"]!!.toInt(), idParent = position))

                    val linearLayout2 = addViewLine(getViewLine(VIEW_MIDDLE_LINE), true, nbTmp)
                    Log.e(TAG, "Id View1 loadTimeline: ${linearLayout2["view1"]!!.toInt()}" )
                    listTimeline.add(ItemTimeline(id = linearLayout2["view1"]!!.toInt(), idParent = position))
                    position++
                    listTimeline.add(ItemTimeline(id = linearLayout2["view2"]!!.toInt(), idParent = position))
                }
            }


            if (nbTimeLine == 1){
                Log.e(TAG, "3e time: $nbTmp", )

                val linearLayout = addViewLine(getViewLine(VIEW_MIDDLE))
                listTimeline.add(ItemTimeline(id = linearLayout["view0"]!!.toInt(), idParent = position))

                val linearLayout1 = addViewLine(getViewLine(VIEW_MIDDLE))
                listTimeline.add(ItemTimeline(id = linearLayout1["view0"]!!.toInt(), idParent = position))

                val linearLayout2 = addViewLine(getViewLine(VIEW_MIDDLE_LINE), true, nbTmp)
                listTimeline.add(ItemTimeline(id = linearLayout2["view1"]!!.toInt(), idParent = position))
                position++
                listTimeline.add(ItemTimeline(id = linearLayout2["view2"]!!.toInt(), idParent = position))
            }

            nbTmp++
        }

        Log.e(TAG, "loadTimeline: ${listTimeline}" )
    }

    private fun getViewLine(typeView: Int): LinearLayout {
        when(typeView){
            VIEW_FIRST -> {
                return inflater.inflate(R.layout.view_first, null, true) as LinearLayout
            }
            VIEW_LAST-> {
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

    fun refreshTimeline(list: Map<Int, String> ){
        Log.e(TAG, "refreshTimeline: $list" )
        list.forEach{ map ->

            Log.e(TAG, "refreshTimeline: $map" )
           val listFilter =  listTimeline.filter { map.key == it.idParent }
            if (listFilter.isNotEmpty())
                listFilter.forEach{
                        setTint(rootLayout.findViewById<View>(it.id).background, Color.parseColor(map.value))
                }
        }
    }

    private fun addViewLine(linearLayout: LinearLayout? = null, showTmp: Boolean = false, i:Int = 0): Map<String, Int?> {
        linearLayout?.id = Sequence.nextValue()
        Log.d(TAG, "addViewLine Sequence.nextValue(): ${linearLayout?.id}" )

        if (showTmp)
            linearLayout?.textview?.text = "$i $unit"

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
        private const val TAG = "TimelineView"
        private const val VIEW_FIRST = 1
        private const val VIEW_MIDDLE = 2
        private const val VIEW_MIDDLE_LINE=  3
        private const val VIEW_LAST = 4
    }

}