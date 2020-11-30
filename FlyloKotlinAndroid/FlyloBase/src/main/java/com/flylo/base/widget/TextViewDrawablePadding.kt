package com.flylo.base.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.flylo.base.R

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.base.widget
 * @ClassName:      TextViewDrawablePadding
 * @Author:         ANWEN
 * @CreateDate:     2020/6/14 6:58 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/14 6:58 PM
 * @UpdateRemark:
 * @Version:
 */
class TextViewDrawablePadding : AppCompatTextView {

    private var padding = 0f
    fun setPadding(padding: Float) {
        this.padding = padding
        invalidate()
    }

    constructor(context: Context?) : super(context!!) {}
    constructor(
        context: Context?,
        attrs: AttributeSet
    ) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val ta: TypedArray =
            getContext().obtainStyledAttributes(attrs, R.styleable.TextViewDrawablePadding, 0, 0)
        padding = ta.getDimension(R.styleable.TextViewDrawablePadding_text_drawable_padding, 0f)
        setCompoundDrawablePadding(padding.toInt())
    }
}