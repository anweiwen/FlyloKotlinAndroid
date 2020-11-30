package com.flylo.work.url.http.listener

import android.app.Activity
import com.flylo.net.HttpResultListener
import com.flylo.net.log.FlyLog
import com.google.gson.JsonElement

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.work.url.http.listener
 * @ClassName:      HttpResultRequestListner
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:48 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:48 PM
 * @UpdateRemark:
 * @Version:
 */
class HttpResultRequestListner(listener: HttpResultListener?) :
    HttpResultListener {

    private val listener: HttpResultListener?

    override fun onStart(urlId: Int) {
        listener?.onStart(urlId)
    }

    override fun onEnd(urlId: Int) {
        listener?.onEnd(urlId)
    }

    override fun onNetWorkError(urlId: Int) {
        listener?.onNetWorkError(urlId)
    }

    override fun onSuccess(urlId: Int, value: JsonElement?) {
        FlyLog.d("urlId:" + urlId)
        FlyLog.json(value.toString())
        listener?.onSuccess(urlId, value)
    }

    override fun onError(urlId: Int, e: Throwable?) {
        FlyLog.i("$urlId:$e")
        listener?.onError(urlId, e)
    }

    init {
        this.listener = listener
    }
}