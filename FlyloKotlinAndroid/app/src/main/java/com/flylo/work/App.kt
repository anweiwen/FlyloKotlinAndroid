package com.flylo.work

import com.flylo.frame.mgr.BaseApplication
import com.flylo.net.NetUtils
import com.flylo.net.log.FlyLog
import com.flylo.photo.tool.PhotoInit
import com.flylo.work.url.http.base.UrlBase

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.work
 * @ClassName:      App
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 3:56 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 3:56 PM
 * @UpdateRemark:
 * @Version:
 */
class App : BaseApplication(){

    override fun onCreate() {
        super.onCreate()
        UrlBase.init()
        NetUtils.init(this)
        PhotoInit.Init(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        PhotoInit.onTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        PhotoInit.onLowMemory()
    }
}