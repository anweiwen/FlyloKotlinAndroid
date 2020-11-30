package com.flylo.frame.mgr

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.flylo.frame.tool.log.FlyLog
import com.flylo.frame.utils.Utils

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.mgr
 * @ClassName:      BaseApplication
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 2:40 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 2:40 PM
 * @UpdateRemark:
 * @Version:
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}