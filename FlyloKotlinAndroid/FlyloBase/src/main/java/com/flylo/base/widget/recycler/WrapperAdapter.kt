package com.flylo.base.widget.recycler

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 *
 * @ProjectName:    FlyloKotlinAndroid
 * @Package:        com.flylo.base.widget.recycler
 * @ClassName:      WrapperAdapter
 * @Author:         ANWEN
 * @CreateDate:     2020/6/14 7:05 PM
 * @UpdateUser:
 * @UpdateDate:     2020/6/14 7:05 PM
 * @UpdateRemark:
 * @Version:
 */
class WrapperAdapter(
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder?>,
    headerContainer: LinearLayout?,
    footerContainer: LinearLayout?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder?>
    private val mHeaderContainer: LinearLayout?
    private val mFooterContainer: LinearLayout?
    private val mObserver: RecyclerView.AdapterDataObserver =
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                this@WrapperAdapter.notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                this@WrapperAdapter.notifyItemRangeChanged(positionStart + 2, itemCount)
            }

            override fun onItemRangeChanged(
                positionStart: Int,
                itemCount: Int,
                payload: Any?
            ) {
                this@WrapperAdapter.notifyItemRangeChanged(positionStart + 2, itemCount, payload)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                this@WrapperAdapter.notifyItemRangeInserted(positionStart + 2, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                this@WrapperAdapter.notifyItemRangeRemoved(positionStart + 2, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                this@WrapperAdapter.notifyDataSetChanged()
            }
        }

    fun getAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder?> {
        return mAdapter
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager: RecyclerView.LayoutManager? = recyclerView.getLayoutManager()
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager: GridLayoutManager = layoutManager
            val spanSizeLookup: GridLayoutManager.SpanSizeLookup =
                gridLayoutManager.getSpanSizeLookup()
            gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val wrapperAdapter = recyclerView.getAdapter() as WrapperAdapter
                    if (isFullSpanType(wrapperAdapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount()
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position - 2)
                    }
                    return 1
                }
            })
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position: Int = holder.getAdapterPosition()
        val type = getItemViewType(position)
        if (isFullSpanType(type)) {
            val layoutParams: ViewGroup.LayoutParams = holder.itemView.getLayoutParams()
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                val lp: StaggeredGridLayoutManager.LayoutParams =
                    layoutParams
                lp.setFullSpan(true)
            }
        }
    }

    private fun isFullSpanType(type: Int): Boolean {
        return type == REFRESH_HEADER || type == HEADER || type == FOOTER || type == LOAD_MORE_FOOTER
    }

    override fun getItemCount(): Int {
        return mAdapter.getItemCount() + 2
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return HEADER
        } else if (0 < position && position < mAdapter.getItemCount() + 1) {
            return mAdapter.getItemViewType(position - 1)
        } else if (position == mAdapter.getItemCount() + 1) {
            return FOOTER
        }
        throw IllegalArgumentException("Wrong type! Position = $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return (if (viewType == HEADER) {
            HeaderContainerViewHolder(mHeaderContainer!!)
        } else if (viewType == FOOTER) {
            FooterContainerViewHolder(mFooterContainer!!)
        } else {
            mAdapter.onCreateViewHolder(parent, viewType)
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (0 < position && position < mAdapter.getItemCount() + 1) {
            mAdapter.onBindViewHolder(holder, position - 1)
        }
    }

    internal class RefreshHeaderContainerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    internal class HeaderContainerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    internal class FooterContainerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    internal class LoadMoreFooterContainerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    companion object {
        protected const val REFRESH_HEADER = Int.MIN_VALUE
        protected const val HEADER = Int.MIN_VALUE + 1
        protected const val FOOTER = Int.MAX_VALUE - 1
        protected const val LOAD_MORE_FOOTER = Int.MAX_VALUE
    }

    init {
        mAdapter = adapter
        mHeaderContainer = headerContainer
        mFooterContainer = footerContainer
        mAdapter.registerAdapterDataObserver(mObserver)
    }

}