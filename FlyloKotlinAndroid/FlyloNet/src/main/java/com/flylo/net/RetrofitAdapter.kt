package com.flylo.net

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.net
 * @ClassName:      RetrofitAdapter
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 12:09 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 12:09 PM
 * @UpdateRemark:
 * @Version:
 */
class RetrofitAdapter : JsonDeserializer<JsonElement?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): JsonElement? {
        return if (json.isJsonObject) {
            json
        } else null
    }
}