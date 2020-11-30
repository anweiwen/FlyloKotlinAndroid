package com.flylo.net

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      AddCookiesInterceptor
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:08 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:08 PM
 * @UpdateRemark:
 * @Version:
 */
class AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences: HashSet<String> =
            NetUtils.getContext()!!.getSharedPreferences(
                "config",
                Context.MODE_PRIVATE
            ).getStringSet("cookie", null) as HashSet<String>
        if (preferences != null) {
            for (cookie in preferences) {
                builder.addHeader("Cookie", cookie)
                Log.v(
                    "OkHttp",
                    "Adding Header: $cookie"
                ) // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }
        }
        return chain.proceed(builder.build())
    }
}