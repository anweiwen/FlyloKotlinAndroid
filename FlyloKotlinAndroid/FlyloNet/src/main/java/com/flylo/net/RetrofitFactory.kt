package com.flylo.net

import android.text.TextUtils
import com.flylo.net.cookie.CookieJarImpl
import com.flylo.net.cookie.store.PersistentCookieStore
import com.flylo.net.log.FlyLog
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      RetrofitFactory
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:10 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:10 PM
 * @UpdateRemark:
 * @Version:
 */
object RetrofitFactory {
    private const val TIMEOUT: Long = 30

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    private var okHttpClient: OkHttpClient? = null
    open var header: HashMap<String, String>? =
        HashMap()

    fun initOkHttpClick() {
        okHttpClient = OkHttpClient.Builder() // 添加通用的Header
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                // 替换为自己的token
                if (header != null) {
                    val keySets =
                        header!!.keys
                    for (key in keySets) {
                        val value = header!![key]
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            builder.addHeader(key, value)
                        }
                    }
                }
                chain.proceed(builder.build())
            } /*
                这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
                出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
                 */
            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                if (FlyLog.init().setTag("Flylo").isDebug()) {
                    println("Flylo log:$message")
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .cookieJar(CookieJarImpl(PersistentCookieStore(NetUtils.getContext()!!))) //                .addInterceptor(new AddCookiesInterceptor()) //这部分
            //                .addInterceptor(new ReceivedCookiesInterceptor()) //这部分
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    fun <T> getInstance(baseUrl: String, service: Class<T>?): T {
        if (okHttpClient == null) {
            initOkHttpClick()
        }
        var url = baseUrl
        if (!baseUrl.endsWith("/")){
            url = "${baseUrl}/"
        }
        return Retrofit.Builder()
            .baseUrl(url) // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create(buildGson())) // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(service)
    }

    private fun buildGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .registerTypeAdapter(JsonElement::class.java, RetrofitAdapter())
            .create()
    }
}