package com.flylo.photo.tool

import android.content.Context
import imageloader.libin.com.images.loader.ImageLoader

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.photo.tool
 * @ClassName:      PhotoInit
 * @Author:         ANWEN
 * @CreateDate:     2020/10/24 11:55 PM
 * @UpdateUser:
 * @UpdateDate:     2020/10/24 11:55 PM
 * @UpdateRemark:
 * @Version:
 */
class PhotoInit {

    companion object{
        fun Init(context: Context){
            ImageLoader.init(context);
        }

        fun onTrimMemory(level: Int){
            ImageLoader.trimMemory(level)
        }

        fun onLowMemory(){
            ImageLoader.clearAllMemoryCaches()
        }
    }
}