package com.flylo.frame.tool.dialog

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import androidx.core.content.ContextCompat

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.dialog
 * @ClassName:      PopupWindowView
 * @Author:         ANWEN
 * @CreateDate:     2020/10/26 11:09 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/26 11:09 PM
 * @UpdateRemark:
 * @Version:
 */
class PopupWindowView : PopupWindow {

    constructor(context: Context?) : super(context)

    constructor(view: View?, matchParent: Int, matchParent1: Int, b: Boolean) : super(view, matchParent, matchParent1, b)

    class Builder
    /**
     * @param context  上下文
     * @param layoutId 弹窗布局layout
     */(private var context: Context?, private val layoutId: Int) {

        var view: View? = null
            private set
        private var style = 0
        private var isAll = true
        private var asDown = false
        private var asTop = false
        var viewBottom = false


        /**
         * 进入退出动画的style
         *
         * @param style
         */
        fun setStyle(style: Int) {
            this.style = style
        }

        /**
         * 属性
         *
         * @param isAll  是否全屏
         * @param asDown 是否可点击
         */
        fun setAttrs(isAll: Boolean, asDown: Boolean, asTop: Boolean) {
            this.isAll = isAll
            this.asDown = asDown
            this.asTop = asTop
        }

        var pop: PopupWindowView? = null
            private set

        @SuppressLint("WrongConstant")
        fun show(touchOutSide: Boolean, downView: View?): PopupWindowView {
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(layoutId, null)
            pop = if (!isAll) {
                PopupWindowView(
                    view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true
                )
            } else {
                if (asDown) {
                    PopupWindowView(
                        view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, true
                    )
                } else {
                    PopupWindowView(
                        view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, true
                    )
                }
            }
            pop!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        context!!,
                        R.color.transparent
                    )
                )
            )
            pop!!.isOutsideTouchable = touchOutSide
            pop!!.animationStyle = style
            pop!!.softInputMode = INPUT_METHOD_NEEDED
            pop!!.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            if (viewBottom) {
                pop!!.showAsDropDown(downView)
            } else {
                if (asDown) {
                    pop!!.showAsDropDown(downView, 0, 0)
                    setBackgroundAlpha(1.0f)
                } else {
                    pop!!.showAtLocation(downView, Gravity.BOTTOM, 0, 0)
                    setBackgroundAlpha(0.5f)
                }
            }
            pop!!.setOnDismissListener {
                setBackgroundAlpha(1.0f)
                if (viewClick != null) {
                    viewClick!!.onDismiss()
                }
            }
            return pop!!
        }

        fun dismiss() {
            if (context != null) {
                if (context is Activity) {
                    val act = context as Activity
                    if (act.isFinishing) {
                        return
                    }
                }
            }
            if (pop != null) {
                if (pop!!.isShowing) {
                    pop!!.dismiss()
                }
            }
            context = null
        }

        fun setBackgroundAlpha(bgAlpha: Float) {
            val lp = (context as Activity?)!!.window
                .attributes
            lp.alpha = bgAlpha
            (context as Activity?)!!.window.attributes = lp
        }

        private var viewClick: ViewClick? = null
        fun setViewClick(viewClick_tmp: ViewClick?) {
            viewClick = viewClick_tmp
        }

        interface ViewClick {
            fun onDismiss()
        }

    }
}