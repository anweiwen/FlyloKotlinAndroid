package com.flylo.frame.tool.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.flylo.frame.R

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.dialog
 * @ClassName:      DialogView
 * @Author:         ANWEN
 * @CreateDate:     2020/10/26 10:53 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/26 10:53 PM
 * @UpdateRemark:
 * @Version:
 */
class DialogView : Dialog {

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, theme: Int) : super(context!!, theme) {}

    class Builder
    /**
     *
     * @param context 上下文
     * @param layoutId 弹窗布局layout
     */(private var context: Context?, private val layoutId: Int) {

        var view: View? = null
        var myDialog: DialogView? = null

        fun show(touchOutSide: Boolean): DialogView {
            val inflater = LayoutInflater.from(context)
            myDialog = DialogView(context, R.style.dialog)
            view = inflater.inflate(layoutId, null)
            myDialog!!.setContentView(view!!)
            myDialog!!.setCanceledOnTouchOutside(touchOutSide)
            myDialog!!.show()
            return myDialog!!
        }

        fun dismiss() {
            if (myDialog!!.isShowing) {
                myDialog!!.dismiss()
            }
            context = null
        }

    }
}