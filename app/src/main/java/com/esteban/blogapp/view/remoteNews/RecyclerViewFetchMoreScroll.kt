package com.esteban.blogapp.view.remoteNews

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewFetchMoreScroll : RecyclerView.OnScrollListener {

    private val TAG = RecyclerViewFetchMoreScroll::class.java.simpleName

    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var isLoading: Boolean = false
    private var isSearching: Boolean = false
    private var mLayoutManager: RecyclerView.LayoutManager

    fun setLoaded() {
        isLoading = false
    }

    fun getLoaded(): Boolean {
        return isLoading
    }

    fun setSearching() {
        isSearching = true
    }

    fun doneSearching() {
        isSearching = false
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    constructor(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!getLoaded() && !isSearching && !recyclerView.canScrollVertically(1)) {
            Log.d(TAG, "call LoadMore?= true")
            mOnLoadMoreListener.onLoadMore()
            isLoading = true
        }

    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}