package com.flylo.base.widget

import android.content.Context
import com.opensource.svgaplayer.SVGAImageView

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.base.widget
 * @ClassName:      SVGAView
 * @Author:         ANWEN
 * @CreateDate:     2020/10/28 7:44 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/28 7:44 PM
 * @UpdateRemark:
 * @Version:
 */
class SVGAView{
    fun show(context: Context){
        var a :SVGAImageView = SVGAImageView(context)
        println("a:${a}")
    }
}