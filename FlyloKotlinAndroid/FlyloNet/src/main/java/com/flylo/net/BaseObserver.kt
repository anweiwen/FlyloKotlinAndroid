package com.flylo.net

import com.google.gson.JsonElement
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      BaseObserver
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:08 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:08 PM
 * @UpdateRemark:
 * @Version:
 */
abstract class BaseObserver protected constructor(urlId: Int) :
    Observer<JsonElement?> {
    private val urlId: Int
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(t: JsonElement) {
        onHandlerSuccess(t, urlId)
    }

    override fun onError(e: Throwable) {
        onHandlerError(e, urlId)
    }

    override fun onComplete() {}
    protected abstract fun onHandlerSuccess(t: JsonElement?, urlId: Int)
    protected abstract fun onHandlerError(e: Throwable?, urlId: Int)

    init {
        this.urlId = urlId
    }
}