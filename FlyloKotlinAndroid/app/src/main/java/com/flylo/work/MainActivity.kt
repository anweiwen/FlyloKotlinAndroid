package com.flylo.work

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.flylo.net.HttpResultListener
import com.flylo.net.base.HttpTool
import com.flylo.work.url.http.api.AccountEnum
import com.flylo.work.url.http.api.MineEnum
import com.google.gson.JsonElement
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

class MainActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InitHttp()

    }

    var httpTool: HttpTool? = null

    fun InitHttp() {
        httpTool = HttpTool(this, listener!!)

        var params: HashMap<String, String?> = HashMap()
        params["sessionId"] = "b54af40a8a7d48388037956bbb4c88bf"
        httpTool!!.getGet(MineEnum.UserInfo, params)
        httpTool!!.getPost(AccountEnum.Login, params)
    }

    val listener = object : HttpResultListener {

        override fun onStart(urlId: Int) {
        }

        override fun onEnd(urlId: Int) {
        }

        override fun onNetWorkError(urlId: Int) {

        }

        override fun onSuccess(urlId: Int, value: JsonElement?) {
            when(urlId){
                MineEnum.UserInfo.getId()->{
                    println("hello:${value}")
                }
                AccountEnum.Login.getId()->{
                    println("hello:${value}")
                }
            }
        }

        override fun onError(urlId: Int, e: Throwable?) {

        }
    }
}