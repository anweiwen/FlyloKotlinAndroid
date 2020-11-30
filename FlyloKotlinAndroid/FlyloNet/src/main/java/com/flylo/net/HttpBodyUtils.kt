package com.flylo.net

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      HttpBodyUtils
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:09 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:09 PM
 * @UpdateRemark:
 * @Version:
 */
object HttpBodyUtils {
    fun getTextBody(text: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), text)
    }

    fun getTextBodyData(text: String?): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), text)
    }

    fun getFilePart(key: String?, file: File): MultipartBody.Part? {
        if (!file.isFile) {
            return null
        }
        val requestBody =
            RequestBody.create(MediaType.parse("application/octet-stream"), file)
        return MultipartBody.Part.createFormData(key, file.name, requestBody)
    }

    fun getFileBody(file: File?): RequestBody {
        return RequestBody.create(MediaType.parse("application/octet-stream"), file)
    }

    fun getFileKey(key: String, file: File): String {
        return key + "\"; filename=\"" + file.name + ""
    }
}
