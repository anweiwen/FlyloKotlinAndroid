package com.flylo.frame.tool.dialog

import android.app.Activity
import android.content.Context
import android.view.View

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.dialog
 * @ClassName:      BaseDialog
 * @Author:         ANWEN
 * @CreateDate:     2020/10/26 10:56 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/26 10:56 PM
 * @UpdateRemark:
 * @Version:
 */
abstract class BaseDialog {

    private var builder: DialogView.Builder? = null
    private var view: View? = null
    private var mContext: Context? = null

    open fun show(context: Context?): View? {
        mContext = context
        if (builder != null) {
            builder!!.dismiss()
        }
        builder = DialogView.Builder(context, layoutId())
        builder!!.show(true)
        view = builder!!.view
        Init(view!!)
        return view
    }

    abstract fun layoutId(): Int
    abstract fun Init(view: View)

    open fun dismiss() {
        if (mContext != null) {
            if (mContext is Activity) {
                val act = mContext as Activity
                if (act.isFinishing) {
                    return
                }
            }
        }
        if (builder != null) {
            builder!!.dismiss()
        }
    }

    private var viewClick: ViewClick? = null

    open fun setViewClick(viewClick: ViewClick?) {
        this.viewClick = viewClick
    }

    interface ViewClick {
        fun onViewClick(v: View?, obj: Any?)
    }

}