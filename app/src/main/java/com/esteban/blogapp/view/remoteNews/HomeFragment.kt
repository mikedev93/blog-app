package com.esteban.blogapp.view.remoteNews

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteban.blogapp.R
import com.esteban.blogapp.api.APIConstants
import com.esteban.blogapp.contract.NewsFeedContract
import com.esteban.blogapp.model.Article
import com.esteban.blogapp.presenter.NewsFeedPresenter
import com.esteban.blogapp.utils.SnackbarUtil
import com.esteban.blogapp.view.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment(), NewsFeedContract.NewsFeedView, ArticleAdapter.OnArticleClickListener {

    private val TAG = HomeFragment::class.java.simpleName

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mLayoutManager:RecyclerView.LayoutManager
    private lateinit var scrollListener:RecyclerViewFetchMoreScroll

    private var homeActivity: HomeActivity? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerAdapter: ArticleAdapter? = null
    private var articlesList = ArrayList<Article?>()
    private var newsPresenter: NewsFeedPresenter? = null
    private var lastVisiblePosition: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        setHasOptionsMenu(true)
        activity?.title = resources.getText(R.string.title_home)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.actionbar_menu, menu)
        var searchItem = menu.findItem(R.id.search_btn)
        var searchManager =requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(homeActivity?.componentName))
            searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.length > 2) {
                        var newList = ArrayList(articlesList.filter { it?.title!!.toLowerCase().contains(newText.toLowerCase()) })
                        recyclerAdapter?.updateList(newList)
                        scrollListener.setSearching()
                    } else if (newText.isEmpty()) {
                        recyclerAdapter?.updateList(articlesList)
                        scrollListener.doneSearching()
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsPresenter = NewsFeedPresenter(this)
        recyclerView = view.findViewById(R.id.recycler_articles)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerAdapter = articlesList?.let { ArticleAdapter(requireContext(), it, this) }
        recyclerView!!.adapter = recyclerAdapter
        mLayoutManager = LinearLayoutManager(requireContext())
        setScrollListener()

        homeActivity = context as HomeActivity
        homeActivity?.showLoading()
        recyclerView!!.visibility = View.GONE
        val params: MutableMap<String, String> = HashMap()
        params[APIConstants.KEYWORD] = "watches"
        params[APIConstants.LANGUAGE] = "en"
        params[APIConstants.PAGE] = "1"
        params[APIConstants.SORT_BY] = "relevancy"
        newsPresenter?.setCurrentPage(1)
        newsPresenter?.getAllArticles(params)
    }

    override fun updateView() {
        articlesList = this.newsPresenter?.showArticles()!!
        recyclerAdapter?.removeLoadingView()
        recyclerAdapter?.updateList(articlesList)
        scrollListener.setLoaded()
        recyclerView!!.visibility = View.VISIBLE
        homeActivity?.hideLoading()
    }

    private fun getMoreData() {
        homeActivity?.showLoading()
        val start = recyclerAdapter?.itemCount!!
        val end = start + 20
        val params: MutableMap<String, String> = HashMap()
        params[APIConstants.KEYWORD] = "watches"
        params[APIConstants.LANGUAGE] = "en"
        var newPage = newsPresenter?.getCurrentPage()!! + 1
        params[APIConstants.PAGE] = newPage.toString()
        params[APIConstants.SORT_BY] = "relevancy"
        Handler().postDelayed(Runnable {
            newsPresenter?.getAllArticles(params)
        }, 3000)
    }

    private fun setScrollListener() {
        scrollListener = RecyclerViewFetchMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : RecyclerViewFetchMoreScroll.OnLoadMoreListener {
            override fun onLoadMore() {
                getMoreData()
            }
        })
        recyclerView?.addOnScrollListener(scrollListener)
    }

    override fun displayError(message: String?) {
        homeActivity?.hideLoading()
        scrollListener.setLoaded()
        recyclerAdapter?.removeLoadingView()
        val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.nav_view)!!
        SnackbarUtil.displayError(requireContext(), bottomNavigationView, message!!)
    }

    override fun onItemClick(item: Article) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item?.url)))
    }
}