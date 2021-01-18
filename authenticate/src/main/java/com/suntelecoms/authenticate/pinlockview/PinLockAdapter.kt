package com.suntelecoms.authenticate.pinlockview

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.suntelecoms.authenticate.R

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
class PinLockAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var customizationOptions: CustomizationOptionsBundle? = null
    var onItemClickListener: OnNumberClickListener? = null
    var onDeleteClickListener: OnDeleteClickListener? = null
    var pinLength = 0
    private val BUTTON_ANIMATION_DURATION = 150
    private var mKeyValues: IntArray
    private var mTypeface: Typeface? = null
    fun setTypeFace(typeFace: Typeface?) {
        mTypeface = typeFace
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        viewHolder = if (viewType == VIEW_TYPE_NUMBER) {
            val view = inflater.inflate(R.layout.layout_number_item, parent, false)
            NumberViewHolder(view, mTypeface)
        } else {
            val view = inflater.inflate(R.layout.layout_delete_item, parent, false)
            DeleteViewHolder(view)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_NUMBER) {
            val vh1 = holder as NumberViewHolder
            configureNumberButtonHolder(vh1, position)
        } else if (holder.itemViewType == VIEW_TYPE_DELETE) {
            val vh2 = holder as DeleteViewHolder
            configureDeleteButtonHolder(vh2)
        }
    }

    private fun configureNumberButtonHolder(holder: NumberViewHolder?, position: Int) {
        if (holder != null) {
            if (position == 9) {
                holder.mNumberButton.visibility = View.GONE
            } else {
                holder.mNumberButton.text = mKeyValues[position].toString()
                holder.mNumberButton.visibility = View.VISIBLE
                holder.mNumberButton.tag = mKeyValues[position]
            }
            if (customizationOptions != null) {
                holder.mNumberButton.setTextColor(customizationOptions!!.textColor)
                if (customizationOptions!!.buttonBackgroundDrawable != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        holder.mNumberButton.setBackgroundDrawable(
                                customizationOptions!!.buttonBackgroundDrawable)
                    } else {
                        holder.mNumberButton.background = customizationOptions!!.buttonBackgroundDrawable
                    }
                }
                holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        customizationOptions!!.textSize.toFloat())
                val params = LinearLayout.LayoutParams(
                        customizationOptions!!.buttonSize,
                        customizationOptions!!.buttonSize)
                holder.mNumberButton.layoutParams = params
            }
        }
    }

    private fun configureDeleteButtonHolder(holder: DeleteViewHolder?) {
        if (holder != null) {
            if (customizationOptions!!.isShowDeleteButton && pinLength > 0) {
                holder.mButtonImage.visibility = View.VISIBLE
                if (customizationOptions!!.deleteButtonDrawable != null) {
                    holder.mButtonImage.setImageDrawable(customizationOptions!!.deleteButtonDrawable)
                }
                holder.mButtonImage.setColorFilter(customizationOptions!!.textColor,
                        PorterDuff.Mode.SRC_ATOP)
                val params = LinearLayout.LayoutParams(
                        customizationOptions!!.deleteButtonWidthSize,
                        customizationOptions!!.deleteButtonHeightSize)
                holder.mButtonImage.layoutParams = params
            }
        }
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            VIEW_TYPE_DELETE
        } else VIEW_TYPE_NUMBER
    }

    var keyValues: IntArray
        get() = mKeyValues
        set(keyValues) {
            mKeyValues = getAdjustKeyValues(keyValues)
            notifyDataSetChanged()
        }

    private fun getAdjustKeyValues(keyValues: IntArray): IntArray {
        val adjustedKeyValues = IntArray(keyValues.size + 1)
        for (i in keyValues.indices) {
            if (i < 9) {
                adjustedKeyValues[i] = keyValues[i]
            } else {
                adjustedKeyValues[i] = -1
                adjustedKeyValues[i + 1] = keyValues[i]
            }
        }
        return adjustedKeyValues
    }

    interface OnNumberClickListener {
        fun onNumberClicked(keyValue: Int)
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked()
        fun onDeleteLongClicked()
    }

    inner class NumberViewHolder(itemView: View, font: Typeface?) : RecyclerView.ViewHolder(itemView) {
        var mNumberButton: Button

        init {
            mNumberButton = itemView.findViewById<View>(R.id.button) as Button
            if (font != null) {
                mNumberButton.typeface = font
            }
            mNumberButton.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (onItemClickListener != null) {
                        onItemClickListener!!.onNumberClicked(v.tag as Int)
                    }
                }
            })
            mNumberButton.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mNumberButton.startAnimation(scale())
                }
                false
            }
        }
    }

    inner class DeleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mDeleteButton: LinearLayout
        var mButtonImage: ImageView

        init {
            mDeleteButton = itemView.findViewById<View>(R.id.button) as LinearLayout
            mButtonImage = itemView.findViewById<View>(R.id.buttonImage) as ImageView
            if (customizationOptions!!.isShowDeleteButton && pinLength > 0) {
                mDeleteButton.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {
                        if (onDeleteClickListener != null) {
                            onDeleteClickListener!!.onDeleteClicked()
                        }
                    }
                })
                mDeleteButton.setOnLongClickListener(object : OnLongClickListener {
                    override fun onLongClick(v: View): Boolean {
                        if (onDeleteClickListener != null) {
                            onDeleteClickListener!!.onDeleteLongClicked()
                        }
                        return true
                    }
                })
                mDeleteButton.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        mDeleteButton.startAnimation(scale())
                    }
                    false
                }
            }
        }
    }

    private fun scale(): Animation {
        val scaleAnimation = ScaleAnimation(.75f, 1f, .75f, 1f,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
        scaleAnimation.duration = BUTTON_ANIMATION_DURATION.toLong()
        scaleAnimation.fillAfter = true
        return scaleAnimation
    }

    companion object {
        private const val VIEW_TYPE_NUMBER = 0
        private const val VIEW_TYPE_DELETE = 1
    }

    init {
        mKeyValues = getAdjustKeyValues(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))
    }
}