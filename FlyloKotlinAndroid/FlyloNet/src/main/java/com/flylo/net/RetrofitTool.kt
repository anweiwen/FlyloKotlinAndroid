package com.flylo.net

import com.google.gson.JsonElement
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      RetrofitTool
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:11 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:11 PM
 * @UpdateRemark:
 * @Version:
 */
object RetrofitTool {
    /**
     * 线程调度
     */
    open fun <T> compose(
        urlId: Int,
        lifecycle: LifecycleTransformer<T>?,
        listener: HttpResultListener?
    ): ObservableTransformer<T, T>? {
        return ObservableTransformer { observable ->
            observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(Consumer { // 可添加网络连接判断等
                    if (!NetUtils.isNetworkAvailable(NetUtils.getContext()!!)) {
                        listener?.onNetWorkError(urlId)
                        return@Consumer
                    }
                    listener?.onStart(urlId)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle)
        }
    }

    fun POST(
        urlId: Int,
        observable: Observable<JsonElement>,
        rxLifecycle: LifecycleProvider<*>,
        listener: HttpResultListener?
    ) {
        val transformer: ObservableTransformer<JsonElement?, JsonElement?>? =
            compose(urlId, rxLifecycle.bindToLifecycle(), listener)
        observable.compose(transformer).subscribe(object : BaseObserver(urlId) {
            override fun onHandlerError(e: Throwable?, urlId: Int) {
                if (listener != null) {
                    listener.onEnd(urlId)
                    listener.onError(urlId, e)
                }
            }

            override fun onHandlerSuccess(value: JsonElement?, urlId: Int) {
                if (listener != null) {
                    listener.onEnd(urlId)
                    listener.onSuccess(urlId, value)
                }
            }
        })
    }
}