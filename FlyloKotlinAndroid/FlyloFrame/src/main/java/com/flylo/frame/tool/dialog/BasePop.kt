package com.flylo.frame.tool.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.flylo.frame.R

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.dialog
 * @ClassName:      BasePop
 * @Author:         ANWEN
 * @CreateDate:     2020/10/26 11:12 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/26 11:12 PM
 * @UpdateRemark:
 * @Version:
 */
abstract class BasePop {

    private var builder: PopupWindowView.Builder? = null
    private var view: View? = null
    private var mContext: Context? = null

    open fun show(context: Context?, downView: View?): View? {
        mContext = context
        if (builder != null) {
            builder!!.dismiss()
        }
        builder = PopupWindowView.Builder(context, layoutId())
        builder!!.setStyle(R.style.Pop_Bottom_Anim)
        builder!!.show(true, downView)
        view = builder!!.view
        Init(view!!)
        return view
    }

    abstract fun layoutId(): Int
    abstract fun Init(view: View)

    private var viewClick: ViewClick? = null

    open fun setViewClick(viewClick: ViewClick?) {
        this.viewClick = viewClick
    }

    interface ViewClick {
        fun onViewClick(v: View?, obj: Any?)
    }

}