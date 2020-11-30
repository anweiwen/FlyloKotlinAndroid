package com.flylo.frame.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flylo.frame.listener.ItemViewOnClickListener
import com.flylo.frame.listener.ItemViewOnLongClickListener
import com.flylo.frame.utils.StringUtils
import java.util.ArrayList

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.frame.base
 * @ClassName:      BaseRecyclerAdapter
 * @Author:         ANWEN
 * @CreateDate:     2020/6/27 4:26 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/27 4:26 PM
 * @UpdateRemark:
 * @Version:
 */
abstract class BaseRecyclerAdapter<T : Any, VH : RecyclerView.ViewHolder>(list: List<T>) :
    RecyclerView.Adapter<VH>() {

    open lateinit var context: Context

    var datas: List<T> = ArrayList()

    init {
        this.datas = list
    }

    abstract fun layoutId(viewType: Int): Int

    fun getView(viewGroup: ViewGroup, viewType: Int): View {
        context = viewGroup.context
        var view = LayoutInflater.from(context).inflate(layoutId(viewType), viewGroup, false)
        return view
    }

    override fun getItemCount(): Int = datas.size

    lateinit var itemViewOnClickListener: ItemViewOnClickListener<T>
    lateinit var itemViewOnLongClickListener: ItemViewOnLongClickListener<T>


    open fun getStr(str: String?): String {
        if (StringUtils.isEmpty(str)) {
            return ""
        }
        return str!!
    }

    open fun getStr(str: String?, text: String?): String? {
        if (!StringUtils.isEmpty(str)) {
            return str
        } else {
            return text
        }
    }
}