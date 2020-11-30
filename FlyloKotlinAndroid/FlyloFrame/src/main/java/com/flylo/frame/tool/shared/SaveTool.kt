package com.flylo.frame.tool.shared

import android.content.Context
import com.flylo.frame.utils.StringUtils.isEmpty

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.shared
 * @ClassName:      SaveTool
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 5:49 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 5:49 PM
 * @UpdateRemark:
 * @Version:
 */
class SaveTool(context: Context) {
    private var tool: SharedPreferencesTool? = null
    private val name = "info"
    private val VERSION = "VERSION" // 版本号
    private val USER = "USER" // 用户信息
    private val AREACODE = "AREACODE" // 区号
    private val ACCOUNT = "ACCOUNT" // 账号
    private val PASSWORD = "PASSWORD" // 密码
    private val SEARCH = "SEARCH" // 搜索历史
    private val LANGUAGE = "LANGUAGE" // 语言
    fun putVersion(version: Int) {
        tool!!.put(VERSION, version)
    }

    fun getVersion(): Int {
        return tool!![VERSION, 0] as Int
    }

    fun putUser(user: String?) {
        tool!!.put(USER, user!!)
    }

    fun getUser(): String? {
        return tool!![USER, ""] as String?
    }

    fun putSearch(value: String?) {
        tool!!.put(SEARCH, value!!)
    }

    fun getSearch(): String? {
        return tool!![SEARCH, ""] as String?
    }

    fun putLanguage(language: Int) {
        tool!!.put(LANGUAGE, language)
    }

    fun getLanguage(): Int {
        return tool!![LANGUAGE, 0] as Int
    }

    fun puttKeyValue(key: String?, value: String?) {
        tool!!.put(key, value!!)
    }

    fun getKey(key: String?): String? {
        return tool!![key, ""] as String?
    }

    fun putAccount(account: String?, password: String?) {
        if (!isEmpty(account)) {
            tool!!.put(ACCOUNT, account!!)
        }
        if (!isEmpty(password)) {
            tool!!.put(PASSWORD, password!!)
        }
    }

    fun getAreaCode(): String? {
        return tool!![AREACODE, ""] as String?
    }

    fun getAccount(): String? {
        return tool!![ACCOUNT, ""] as String?
    }

    fun getPassword(): String? {
        return tool!![PASSWORD, ""] as String?
    }

    fun clear() {
        tool!!.put("USER", "")
        tool!!.put("PASSWORD", "")
        tool!!.remove(USER)
        tool!!.remove(PASSWORD)
    }

    fun clearSearch() {
        tool!!.put(SEARCH, "")
        tool!!.remove(SEARCH)
    }

    init {
        if (tool == null) {
            tool = SharedPreferencesTool(context.applicationContext, name)
        }
    }
}