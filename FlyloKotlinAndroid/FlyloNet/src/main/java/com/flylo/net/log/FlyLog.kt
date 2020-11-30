package com.flylo.net.log

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.tool.log
 * @ClassName:      FlyLog
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 4:52 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 4:52 PM
 * @UpdateRemark:
 * @Version:
 */
object FlyLog {
    private val printer: Printer = LoggerPrinter()
    fun init(): XLogConfig {
        return printer.init()
    }

    fun getFormatLog(): String {
        return printer.formatLog
    }

    fun d(message: String?, vararg args: Any?) {
        printer.d(message!!, *args as Array<out Any>)
    }

    fun e(message: String, vararg args: Any) {
        printer.e(null, message, *args)
    }

    fun e(
        throwable: Throwable?,
        message: String,
        vararg args: Any
    ) {
        printer.e(throwable, message, *args)
    }

    fun i(message: String?, vararg args: Any?) {
        printer.i(message!!, *args as Array<out Any>)
    }

    fun v(message: String?, vararg args: Any?) {
        printer.v(message!!, *args as Array<out Any>)
    }

    fun w(message: String?, vararg args: Any?) {
        printer.w(message!!, *args as Array<out Any>)
    }

    fun wtf(message: String?, vararg args: Any?) {
        printer.wtf(message!!, *args as Array<out Any>)
    }

    fun json(json: String?) {
        printer.json(json!!)
    }

    fun xml(xml: String?) {
        printer.xml(xml!!)
    }

    fun map(map: Map<*, *>?) {
        printer.map(map!!)
    }

    fun list(list: List<*>?) {
        printer.list(list!!)
    }
}