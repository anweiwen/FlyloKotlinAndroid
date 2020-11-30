package com.flylo.work.url.http.api

import com.flylo.net.base.BaseEnum

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.work.url.http.api
 * @ClassName:      AccountApi
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 1:59 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 1:59 PM
 * @UpdateRemark:
 * @Version:
 */
internal enum class MineEnum(id: Int, url: String) : BaseEnum {
    UserInfo(2, "api/top/channelList") // 我的
    ;

    private var id = 0
    private var url = ""
    init {
        this.id = id
        this.url = url
    }

    override fun getId(): Int {
        return id
    }

    override fun getUrl(): String {
        return url
    }
}