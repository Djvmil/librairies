package com.suntelecoms.authenticate.pinlockview

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suntelecoms.authenticate.R
import com.suntelecoms.authenticate.pinlockview.PinLockAdapter.OnDeleteClickListener
import com.suntelecoms.authenticate.pinlockview.PinLockAdapter.OnNumberClickListener

/**
 * Represents a numeric lock view which can used to taken numbers as input.
 * The length of the input can be customized using [PinLockView.setPinLength], the default value being 4
 *
 *
 * It can also be used as dial pad for taking number inputs.
 * Optionally, [IndicatorDots] can be attached to this view to indicate the length of the input taken
 *
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
class PinLockView : RecyclerView {
    private var mPin = ""
    private var mPinLength = 0
    private var mHorizontalSpacing = 0
    private var mVerticalSpacing = 0
    private var mTextColor = 0
    private var mDeleteButtonPressedColor = 0
    private var mTextSize = 0
    private var mButtonSize = 0
    private var mDeleteButtonWidthSize = 0
    private var mDeleteButtonHeightSize = 0
    private var mButtonBackgroundDrawable: Drawable? = null
    private var mDeleteButtonDrawable: Drawable? = null
    private var mShowDeleteButton = false
    private var mIndicatorDots: IndicatorDots? = null
    private var mAdapter: PinLockAdapter? = null
    private var mPinLockListener: PinLockListener? = null
    private var mCustomizationOptionsBundle: CustomizationOptionsBundle? = null

    private lateinit var mCustomKeySet: IntArray

    private val mOnNumberClickListener: OnNumberClickListener = object : OnNumberClickListener {
        override fun onNumberClicked(keyValue: Int) {
            if (mPin.length < pinLength) {
                mPin = mPin + keyValue.toString()
                if (isIndicatorDotsAttached) {
                    mIndicatorDots!!.updateDot(mPin.length)
                }
                if (mPin.length == 1) {
                    mAdapter!!.pinLength = mPin.length
                    mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
                }
                if (mPinLockListener != null) {
                    if (mPin.length == mPinLength) {
                        mPinLockListener!!.onComplete(mPin)
                    } else {
                        mPinLockListener!!.onPinChange(mPin.length, mPin)
                    }
                }
            } else {
                if (!isShowDeleteButton) {
                    resetPinLockView()
                    mPin = mPin + keyValue.toString()
                    if (isIndicatorDotsAttached) {
                        mIndicatorDots!!.updateDot(mPin.length)
                    }
                    if (mPinLockListener != null) {
                        mPinLockListener!!.onPinChange(mPin.length, mPin)
                    }
                } else {
                    if (mPinLockListener != null) {
                        mPinLockListener!!.onComplete(mPin)
                    }
                }
            }
        }
    }
    private val mOnDeleteClickListener: OnDeleteClickListener = object : OnDeleteClickListener {
        override fun onDeleteClicked() {
            if (mPin.length > 0) {
                mPin = mPin.substring(0, mPin.length - 1)
                if (isIndicatorDotsAttached) {
                    mIndicatorDots!!.updateDot(mPin.length)
                }
                if (mPin.length == 0) {
                    mAdapter!!.pinLength = mPin.length
                    mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
                }
                if (mPinLockListener != null) {
                    if (mPin.length == 0) {
                        mPinLockListener!!.onEmpty()
                        clearInternalPin()
                    } else {
                        mPinLockListener!!.onPinChange(mPin.length, mPin)
                    }
                }
            } else {
                if (mPinLockListener != null) {
                    mPinLockListener!!.onEmpty()
                }
            }
        }

        override fun onDeleteLongClicked() {
            resetPinLockView()
            if (mPinLockListener != null) {
                mPinLockListener!!.onEmpty()
            }
        }
    }

    constructor(context: Context?) : super(context!!) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attributeSet: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PinLockView)
        try {
            mPinLength = typedArray.getInt(R.styleable.PinLockView_pinLength, DEFAULT_PIN_LENGTH)
            mHorizontalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadHorizontalSpacing, ResourceUtils.getDimensionInPx(context, R.dimen.default_horizontal_spacing)).toInt()
            mVerticalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadVerticalSpacing, ResourceUtils.getDimensionInPx(context, R.dimen.default_vertical_spacing)).toInt()
            mTextColor = typedArray.getColor(R.styleable.PinLockView_keypadTextColor, ResourceUtils.getColor(context, R.color.text_numberpressed))
            mTextSize = typedArray.getDimension(R.styleable.PinLockView_keypadTextSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_text_size)).toInt()
            mButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_button_size)).toInt()
            mDeleteButtonWidthSize = typedArray.getDimension(R.styleable.PinLockView_keypadDeleteButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_delete_button_size_width)).toInt()
            mDeleteButtonHeightSize = typedArray.getDimension(R.styleable.PinLockView_keypadDeleteButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_delete_button_size_height)).toInt()
            mButtonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadButtonBackgroundDrawable)
            mDeleteButtonDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadDeleteButtonDrawable)
            mShowDeleteButton = typedArray.getBoolean(R.styleable.PinLockView_keypadShowDeleteButton, true)
            mDeleteButtonPressedColor = typedArray.getColor(R.styleable.PinLockView_keypadDeleteButtonPressedColor, ResourceUtils.getColor(context, R.color.text_numberpressed))
        } finally {
            typedArray.recycle()
        }
        mCustomizationOptionsBundle = CustomizationOptionsBundle()
        mCustomizationOptionsBundle!!.textColor = mTextColor
        mCustomizationOptionsBundle!!.textSize = mTextSize
        mCustomizationOptionsBundle!!.buttonSize = mButtonSize
        mCustomizationOptionsBundle!!.buttonBackgroundDrawable = mButtonBackgroundDrawable
        mCustomizationOptionsBundle!!.deleteButtonDrawable = mDeleteButtonDrawable
        mCustomizationOptionsBundle!!.deleteButtonWidthSize = mDeleteButtonWidthSize
        mCustomizationOptionsBundle!!.deleteButtonHeightSize = mDeleteButtonHeightSize
        mCustomizationOptionsBundle!!.isShowDeleteButton = mShowDeleteButton
        mCustomizationOptionsBundle!!.deleteButtonPressesColor = mDeleteButtonPressedColor
        initView()
    }

    private fun initView() {
        layoutManager = GridLayoutManager(context, 3)
        mAdapter = PinLockAdapter()
        mAdapter!!.onItemClickListener = mOnNumberClickListener
        mAdapter!!.onDeleteClickListener = mOnDeleteClickListener
        mAdapter!!.customizationOptions = mCustomizationOptionsBundle
        adapter = mAdapter
        addItemDecoration(ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3, false))
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    fun setTypeFace(typeFace: Typeface?) {
        mAdapter!!.setTypeFace(typeFace)
    }

    /**
     * Sets a [PinLockListener] to the to listen to pin update events
     *
     * @param pinLockListener the listener
     */
    fun setPinLockListener(pinLockListener: PinLockListener?) {
        mPinLockListener = pinLockListener
    }

    /**
     * Get the length of the current pin length
     *
     * @return the length of the pin
     */
    /**
     * Sets the pin length dynamically
     *
     * @param pinLength the pin length
     */
    var pinLength: Int
        get() = mPinLength
        set(pinLength) {
            mPinLength = pinLength
            if (isIndicatorDotsAttached) {
                mIndicatorDots!!.pinLength = pinLength
            }
        }

    /**
     * Get the text color in the buttons
     *
     * @return the text color
     */
    /**
     * Set the text color of the buttons dynamically
     *
     * @param textColor the text color
     */
    var textColor: Int
        get() = mTextColor
        set(textColor) {
            mTextColor = textColor
            mCustomizationOptionsBundle!!.textColor = textColor
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the size of the text in the buttons
     *
     * @return the size of the text in pixels
     */
    /**
     * Set the size of text in pixels
     *
     * @param textSize the text size in pixels
     */
    var textSize: Int
        get() = mTextSize
        set(textSize) {
            mTextSize = textSize
            mCustomizationOptionsBundle!!.textSize = textSize
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the size of the pin buttons
     *
     * @return the size of the button in pixels
     */
    /**
     * Set the size of the pin buttons dynamically
     *
     * @param buttonSize the button size
     */
    var buttonSize: Int
        get() = mButtonSize
        set(buttonSize) {
            mButtonSize = buttonSize
            mCustomizationOptionsBundle!!.buttonSize = buttonSize
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the current background drawable of the buttons, can be null
     *
     * @return the background drawable
     */
    /**
     * Set the background drawable of the buttons dynamically
     *
     * @param buttonBackgroundDrawable the background drawable
     */
    var buttonBackgroundDrawable: Drawable?
        get() = mButtonBackgroundDrawable
        set(buttonBackgroundDrawable) {
            mButtonBackgroundDrawable = buttonBackgroundDrawable
            mCustomizationOptionsBundle!!.buttonBackgroundDrawable = buttonBackgroundDrawable
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the drawable of the delete button
     *
     * @return the delete button drawable
     */
    /**
     * Set the drawable of the delete button dynamically
     *
     * @param deleteBackgroundDrawable the delete button drawable
     */
    var deleteButtonDrawable: Drawable?
        get() = mDeleteButtonDrawable
        set(deleteBackgroundDrawable) {
            mDeleteButtonDrawable = deleteBackgroundDrawable
            mCustomizationOptionsBundle!!.deleteButtonDrawable = deleteBackgroundDrawable
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the delete button width size in pixels
     *
     * @return size in pixels
     */
    /**
     * Set the size of the delete button width in pixels
     *
     * @param deleteButtonWidthSize size in pixels
     */
    var deleteButtonWidthSize: Int
        get() = mDeleteButtonWidthSize
        set(deleteButtonWidthSize) {
            mDeleteButtonWidthSize = deleteButtonWidthSize
            mCustomizationOptionsBundle!!.deleteButtonWidthSize = deleteButtonWidthSize
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the delete button size height in pixels
     *
     * @return size in pixels
     */
    /**
     * Set the size of the delete button height in pixels
     *
     * @param deleteButtonHeightSize size in pixels
     */
    var deleteButtonHeightSize: Int
        get() = mDeleteButtonHeightSize
        set(deleteButtonHeightSize) {
            mDeleteButtonHeightSize = deleteButtonHeightSize
            mCustomizationOptionsBundle!!.deleteButtonWidthSize = deleteButtonHeightSize
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Is the delete button shown
     *
     * @return returns true if shown, false otherwise
     */
    /**
     * Dynamically set if the delete button should be shown
     *
     * @param showDeleteButton true if the delete button should be shown, false otherwise
     */
    var isShowDeleteButton: Boolean
        get() = mShowDeleteButton
        set(showDeleteButton) {
            mShowDeleteButton = showDeleteButton
            mCustomizationOptionsBundle!!.isShowDeleteButton = showDeleteButton
            mAdapter!!.notifyDataSetChanged()
        }

    /**
     * Get the delete button pressed/focused state color
     *
     * @return color of the button
     */
    /**
     * Set the pressed/focused state color of the delete button
     *
     * @param deleteButtonPressedColor the color of the delete button
     */
    var deleteButtonPressedColor: Int
        get() = mDeleteButtonPressedColor
        set(deleteButtonPressedColor) {
            mDeleteButtonPressedColor = deleteButtonPressedColor
            mCustomizationOptionsBundle!!.deleteButtonPressesColor = deleteButtonPressedColor
            mAdapter!!.notifyDataSetChanged()
        }

    var customKeySet: IntArray
        get() = mCustomKeySet
        set(customKeySet) {
            mCustomKeySet = customKeySet
            if (mAdapter != null) {
                mAdapter!!.keyValues = customKeySet
            }
        }

    fun enableLayoutShuffling() {
        mCustomKeySet = ShuffleArrayUtils.shuffle(DEFAULT_KEY_SET)
        if (mAdapter != null) {
            mAdapter!!.keyValues = mCustomKeySet
        }
    }

    private fun clearInternalPin() {
        mPin = ""
    }

    /**
     * Resets the [PinLockView], clearing the entered pin
     * and resetting the [IndicatorDots] if attached
     */
    fun resetPinLockView() {
        clearInternalPin()
        mAdapter!!.pinLength = mPin.length
        mAdapter!!.notifyItemChanged(mAdapter!!.itemCount - 1)
        if (mIndicatorDots != null) {
            mIndicatorDots!!.updateDot(mPin.length)
        }
    }

    /**
     * Returns true if [IndicatorDots] are attached to [PinLockView]
     *
     * @return true if attached, false otherwise
     */
    val isIndicatorDotsAttached: Boolean
        get() = mIndicatorDots != null

    /**
     * Attaches [IndicatorDots] to [PinLockView]
     *
     * @param mIndicatorDots the view to attach
     */
    fun attachIndicatorDots(mIndicatorDots: IndicatorDots?) {
        this.mIndicatorDots = mIndicatorDots
    }

    companion object {
        private const val DEFAULT_PIN_LENGTH = 4
        private val DEFAULT_KEY_SET = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    }
}