package com.flylo.frame.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.NonNull

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      Utils
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:12 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:12 PM
 * @UpdateRemark:
 * @Version:
 */
class Utils private constructor() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        fun init(@NonNull context: Context) {
            Companion.context = context.applicationContext
        }

        /**
         * 获取ApplicationContext
         *
         * @return ApplicationContext
         */
        fun getContext(): Context? {
            if (context != null) {
                return context
            }
            throw NullPointerException("should be initialized in application")
        }

        /**
         * 判断网络是否可用
         */
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isAvailable
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}