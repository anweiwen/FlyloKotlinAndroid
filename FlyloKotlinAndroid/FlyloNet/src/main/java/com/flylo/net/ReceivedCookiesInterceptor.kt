package com.flylo.net

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      ReceivedCookiesInterceptor
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:09 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:09 PM
 * @UpdateRemark:
 * @Version:
 */
class ReceivedCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies = HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            val config: SharedPreferences.Editor =
                NetUtils.getContext()!!.getSharedPreferences("config", Context.MODE_PRIVATE)
                    .edit()
            config.putStringSet("cookie", cookies)
            config.commit()
        }
        return originalResponse
    }
}
