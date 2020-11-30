package com.flylo.net.base

import com.google.gson.JsonElement
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.work.url.http
 * @ClassName:      BaseApi
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 2:11 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 2:11 PM
 * @UpdateRemark:
 * @Version:
 */
interface BaseInterface {

    @FormUrlEncoded
    @POST("*")
    fun post(
        @FieldMap params: Map<String, String?>
    ): Observable<JsonElement>

    @GET("*")
    fun get(
        @QueryMap params: Map<String, String?>
    ): Observable<JsonElement>

    @Multipart
    @POST("*")
    fun mult(
        @PartMap params: @JvmSuppressWildcards Map<String, RequestBody?>
    ): Observable<JsonElement>

}