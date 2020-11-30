package com.flylo.frame.tool.shared

import android.content.Context
import android.content.SharedPreferences

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.shared
 * @ClassName:      SharedPreferencesTool
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 4:59 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 4:59 PM
 * @UpdateRemark:
 * @Version:
 */
class SharedPreferencesTool(context: Context, name: String?) {
    private val sp: SharedPreferences
    private val editor: SharedPreferences.Editor

    /**
     * 保存信息 包含提交
     * @param key key
     * @param value 值
     */
    fun put(key: String?, value: Any) {
        val type = value.javaClass.simpleName
        if ("String" == type) {
            editor.putString(key, value as String)
        } else if ("Integer" == type) {
            editor.putInt(key, (value as Int))
        } else if ("Boolean" == type) {
            editor.putBoolean(key, (value as Boolean))
        } else if ("Float" == type) {
            editor.putFloat(key, (value as Float))
        } else if ("Long" == type) {
            editor.putLong(key, (value as Long))
        }
        editor.commit()
    }

    /**
     * 装载数据
     */
    fun putString(key: String?, value: String?): SharedPreferences.Editor {
        return editor.putString(key, value)
    }

    fun putInt(key: String?, value: Int): SharedPreferences.Editor {
        return editor.putInt(key, value)
    }

    fun putLong(key: String?, value: Long): SharedPreferences.Editor {
        return editor.putLong(key, value)
    }

    fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
        return editor.putFloat(key, value)
    }

    fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
        return editor.putBoolean(key, value)
    }

    /**
     * 获取指定Key的信息
     * @param key key
     * @param defaultValue 默认值
     * @return
     */
    operator fun get(key: String?, defaultValue: Any): Any? {
        val type = defaultValue.javaClass.simpleName
        if ("String" == type) {
            return sp.getString(key, defaultValue as String)
        } else if ("Integer" == type) {
            return sp.getInt(key, (defaultValue as Int))
        } else if ("Boolean" == type) {
            return sp.getBoolean(key, (defaultValue as Boolean))
        } else if ("Float" == type) {
            return sp.getFloat(key, (defaultValue as Float))
        } else if ("Long" == type) {
            return sp.getLong(key, (defaultValue as Long))
        }
        return null
    }

    /**
     * 删除指定key的内容
     */
    fun remove(key: String?): SharedPreferences.Editor {
        return editor.remove(key)
    }

    /**
     * 清空所有内容
     */
    fun clear(): SharedPreferences.Editor {
        return editor.clear()
    }

    /**
     * 提交数据
     */
    fun commit(): Boolean {
        return editor.commit()
    }

    init {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        editor = sp.edit()
    }
}