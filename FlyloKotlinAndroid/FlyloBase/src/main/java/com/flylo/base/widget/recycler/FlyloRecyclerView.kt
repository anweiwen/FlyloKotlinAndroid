package com.flylo.base.widget.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.base.widget.recycler
 * @ClassName:      FlyloRecyclerView
 * @Author:         ANWEN
 * @CreateDate:     2020/6/14 7:02 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/14 7:02 PM
 * @UpdateRemark:
 * @Version:
 */
class FlyloRecyclerView @JvmOverloads constructor(
    context: Context?,
    @Nullable attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    RecyclerView(context!!, attrs, defStyle) {
    private var mHeaderViewContainer: LinearLayout? = null
    private var mFooterViewContainer: LinearLayout? = null
    private fun init() {
        setLinear()
    }

    fun setGridSize(gridSize: Int) {
        setLayoutManager(GridLayoutManager(getContext(), gridSize))
        setHasFixedSize(true)
    }

    fun setLinear(): LinearLayoutManager {
        val mgr = LinearLayoutManager(getContext())
        setLayoutManager(mgr)
        return mgr
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
    }

    fun getHeaderContainer(): LinearLayout? {
        ensureHeaderViewContainer()
        return mHeaderViewContainer
    }

    fun getFooterContainer(): LinearLayout? {
        ensureFooterViewContainer()
        return mFooterViewContainer
    }

    fun addHeaderView(headerView: View?) {
        ensureHeaderViewContainer()
        mHeaderViewContainer!!.addView(headerView)
        if (adapter != null) {
            adapter!!.notifyItemChanged(0)
        }
    }

    fun addFooterView(footerView: View?) {
        ensureFooterViewContainer()
        mFooterViewContainer!!.addView(footerView)
        if (adapter != null) {
            adapter!!.notifyItemChanged(adapter!!.getItemCount() - 1)
        }
    }

    override fun setAdapter(adapter: Adapter<RecyclerView.ViewHolder?>?) {
        ensureHeaderViewContainer()
        ensureFooterViewContainer()
        super.setAdapter(WrapperAdapter(adapter!!, mHeaderViewContainer, mFooterViewContainer))
    }

    private fun ensureHeaderViewContainer() {
        if (mHeaderViewContainer == null) {
            mHeaderViewContainer = LinearLayout(getContext())
            mHeaderViewContainer!!.orientation = LinearLayout.VERTICAL
            mHeaderViewContainer!!.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun ensureFooterViewContainer() {
        if (mFooterViewContainer == null) {
            mFooterViewContainer = LinearLayout(getContext())
            mFooterViewContainer!!.orientation = LinearLayout.VERTICAL
            mFooterViewContainer!!.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    fun <T : View?> getHeaderView(@IdRes viewId: Int): View? {
        return if (mHeaderViewContainer != null) {
            mHeaderViewContainer!!.findViewById(viewId)
        } else null
    }

    fun <T : View?> getFooterView(@IdRes viewId: Int): View? {
        return if (mFooterViewContainer != null) {
            mFooterViewContainer!!.findViewById(viewId)
        } else null
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (onScollChangedListener != null) {
            onScollChangedListener!!.onScrollChanged(this, l, t, oldl, oldt)
        }
    }

    private var onScollChangedListener: OnScollChangedListener? = null
    fun setOnScollChangedListener(onScollChangedListener: OnScollChangedListener?) {
        this.onScollChangedListener = onScollChangedListener
    }

    interface OnScollChangedListener {
        fun onScrollChanged(
            recyclerView: FlyloRecyclerView?,
            x: Int,
            y: Int,
            oldx: Int,
            oldy: Int
        )
    }

    init {
        init()
    }
}