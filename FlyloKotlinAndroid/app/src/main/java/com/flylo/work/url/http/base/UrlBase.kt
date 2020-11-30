package com.flylo.work.url.http.base

import com.flylo.net.base.HttpTool

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.work.url.http.base
 * @ClassName:      UrlBase
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 3:54 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 3:54 PM
 * @UpdateRemark:
 * @Version:
 */
object UrlBase {
    fun init() {
        HttpTool.BaseUrl = "http://47.107.81.81:177/guowaishejiao/"
    }
}