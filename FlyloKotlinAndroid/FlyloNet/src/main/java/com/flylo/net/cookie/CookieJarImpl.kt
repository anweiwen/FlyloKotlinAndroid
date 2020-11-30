package com.flylo.net.cookie

import com.flylo.net.cookie.store.CookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net.cookie
 * @ClassName:      CookieJarImpl
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 11:51 AM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 11:51 AM
 * @UpdateRemark:
 * @Version:
 */
class CookieJarImpl(cookieStore: CookieStore?) : CookieJar {
    private val cookieStore: CookieStore

    @Synchronized
    override fun saveFromResponse(
        url: HttpUrl?,
        cookies: List<Cookie?>?
    ) {
        cookieStore.saveCookie(url, cookies)
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl?): List<Cookie?>? {
        return cookieStore.loadCookie(url)
    }

    fun getCookieStore(): CookieStore {
        return cookieStore
    }

    init {
        requireNotNull(cookieStore) { "cookieStore can not be null!" }
        this.cookieStore = cookieStore
    }
}