package com.flylo.net

import com.google.gson.JsonElement

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      HttpResultListener
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:09 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:09 PM
 * @UpdateRemark:
 * @Version:
 */
open interface HttpResultListener {
    fun onStart(urlId: Int)
    fun onEnd(urlId: Int)
    fun onNetWorkError(urlId: Int)
    fun onSuccess(urlId: Int, value: JsonElement?)
    fun onError(urlId: Int, e: Throwable?)
}