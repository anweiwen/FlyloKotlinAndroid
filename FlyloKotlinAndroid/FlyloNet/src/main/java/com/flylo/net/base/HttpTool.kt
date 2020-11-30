package com.flylo.net.base

import com.flylo.frame.url.listener.HttpRequestListener
import com.flylo.net.HttpResultListener
import com.flylo.net.RetrofitFactory
import com.flylo.net.RetrofitTool.POST
import com.flylo.work.url.http.listener.HttpResultRequestListner
import com.google.gson.JsonElement
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import okhttp3.RequestBody

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.work.url.http
 * @ClassName:      HttpTool
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:47 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:47 PM
 * @UpdateRemark:
 * @Version:
 */
class HttpTool : HttpRequestListener {

    companion object {
        var BaseUrl = ""
    }

    private var rxLifecycle: LifecycleProvider<*>? = null
    private var listener: HttpResultListener? = null

    private var bean: ApiBean? =
        ApiBean()

    private fun HttpTool(){}

    /**
     *
     * @param rxLifecycle 对应的组件 RxFragment
     * @param listener 请求回调接口
     */
    constructor(rxLifecycle: RxFragment, listener: HttpResultListener) {
        this.rxLifecycle = rxLifecycle
        this.listener = listener
        addHeader()
    }

    /**
     *
     * @param rxLifecycle 对应的组件 RxAppCompatActivity
     * @param listener 请求回调接口
     */
    constructor(rxLifecycle: RxAppCompatActivity, listener: HttpResultListener){
        this.rxLifecycle = rxLifecycle
        this.listener = listener
        addHeader()
    }

    fun addHeader(){
        //RetrofitFactory.header!!["token"] = ""
    }

    /**
     * 开始请求
     * @param urlId
     * @param observable
     */
    override
    fun doPost(urlId: Int, observable: Observable<JsonElement>) {
        POST(urlId, observable, rxLifecycle!!, HttpResultRequestListner(listener))
    }

    fun getGet(baseEnum: BaseEnum, params: HashMap<String, String?>) {
        var inter = RetrofitFactory.getInstance(BaseUrl +baseEnum.getUrl(), BaseInterface::class.java)
        var observable = inter?.get(params)
        doPost(baseEnum.getId(), observable)
    }

    fun getPost(baseEnum: BaseEnum, params: HashMap<String, String?>) {
        var inter = RetrofitFactory.getInstance(BaseUrl +baseEnum.getUrl(), BaseInterface::class.java)
        var observable = inter?.post(params)
        doPost(baseEnum.getId(), observable)
    }

    fun getMult(baseEnum: BaseEnum, params: HashMap<String, RequestBody?>) {
        var inter = RetrofitFactory.getInstance(BaseUrl +baseEnum.getUrl(), BaseInterface::class.java)
        var observable = inter?.mult(params)
        doPost(baseEnum.getId(), observable)
    }
}

class ApiBean{
    var observable: Observable<JsonElement>? = null
    var urlId: Int = 0

    fun setValue(observable: Observable<JsonElement>,
                 urlId: Int){
        this.observable = observable
        this.urlId = urlId
    }
}