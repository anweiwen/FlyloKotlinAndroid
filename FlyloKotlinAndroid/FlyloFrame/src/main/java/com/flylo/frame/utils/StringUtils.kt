package com.flylo.frame.utils

import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.utils
 * @ClassName:      StringUtils
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 4:28 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 4:28 PM
 * @UpdateRemark:
 * @Version:
 */
object StringUtils {
    /**
     * 判断字符串是否为null
     *
     * @param s 字符串
     * @return
     */
    fun isEmpty(s: String?): Boolean {
        return if (s == null || s.length == 0 || s == "null") {
            true
        } else false
    }

    /**
     * 保留两位小数
     * @param d
     * @return
     */
    fun reserve2(d: Double): String {
        if (d == 0.0) {
            return "0.00"
        }
        val df = DecimalFormat("#0.00")
        return df.format(d)
    }

    var p_10086 = Pattern
        .compile("^((13[4-9])|(147)|(15[0-2,7-9])|(16[0-9])|(17[0-9])|(18[2-4,7-8])|(19[0-9]))\\d{8}|(1705)\\d{7}$")
    var p_10010 = Pattern
        .compile("^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$")
    var p_10001 = Pattern
        .compile("^((133)|(153)|(177)|(173)|(18[0,1,9])|(19[0,1,9]))\\d{8}$")
    var p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$")

    /**
     * 判断手机格式是否正确
     *
     * @param mobile 手机号码
     * @return 是/否
     */
    fun isChinaMobile(mobile: String?): Boolean {
        val m_10086 = p_10086.matcher(mobile)
        val m_10010 = p_10010.matcher(mobile)
        val m_10001 = p_10001.matcher(mobile)
        val m = p.matcher(mobile)
        return if (m_10086.matches() || m_10010.matches() || m_10001.matches() || m.matches()) {
            true
        } else false
    }

    fun mobile(areaCode: String, mobile: String): Boolean {
        var result = true
        if (isEmpty(mobile)) {
            result = false
        } else {
            if (areaCode == "86") { // 中国号码
                if (isChinaMobile(mobile)) {
                } else {
                    result = false
                }
            } else { // 国际号码
                if (mobile.length > 6 && mobile.length < 12) {
                } else {
                    result = false
                }
            }
        }
        return result
    }
}