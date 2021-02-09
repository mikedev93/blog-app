package com.esteban.blogapp.view.localPosts

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esteban.blogapp.R
import com.esteban.blogapp.data.LocalArticle
import com.esteban.blogapp.data.LocalArticleViewModel
import com.esteban.blogapp.view.HomeActivity
import com.esteban.blogapp.view.localPosts.add.AddArticleFragment
import com.esteban.blogapp.view.localPosts.edit.UpdateDeleteArticleFragment
import kotlinx.android.synthetic.main.fragment_local_posts.*

class LocalPostsFragment : Fragment(), LocalArticleAdapter.OnArticleClickListener {

    private lateinit var homeActivity: HomeActivity
    private lateinit var localArticleViewModel: LocalArticleViewModel

    private var recyclerView: RecyclerView? = null
    private var recyclerAdapter: LocalArticleAdapter? = null
    private var articlesList: List<LocalArticle>? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            homeActivity.title = resources.getText(R.string.title_home)
            homeActivity.fragmentManager.beginTransaction().hide(homeActivity.activeFragment).show(homeActivity.fragmentHome)
            homeActivity.activeFragment = homeActivity.fragmentHome
            this.remove()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = resources.getText(R.string.title_dashboard)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_local_posts, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.actionbar_menu_local, menu)
        var searchItem = menu.findItem(R.id.local_search_btn)
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
                        var newList = ArrayList(articlesList!!.filter { it.title!!.toLowerCase().contains(newText.toLowerCase()) })
                        recyclerAdapter?.updateList(newList)
                    } else if (newText.isEmpty()) {
                        recyclerAdapter?.updateList(ArrayList(articlesList))
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeActivity = context as HomeActivity
        recyclerView = view.findViewById(R.id.recycler_local_articles)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerAdapter = LocalArticleAdapter(requireContext(), ArrayList(),this)
        recyclerView!!.adapter = recyclerAdapter

        localArticleViewModel = ViewModelProvider(this).get(LocalArticleViewModel::class.java)
        localArticleViewModel.readAllData.observe(viewLifecycleOwner, Observer { articles ->
            articlesList = articles
            if (!articles.isNullOrEmpty()) {
                recyclerView?.visibility = View.VISIBLE
                rl_no_items.visibility = View.GONE
            } else {
                recyclerView?.visibility = View.GONE
                rl_no_items.visibility = View.VISIBLE
            }
            recyclerAdapter?.updateList(articles)
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_article_btn -> {
                var fragment = AddArticleFragment()
                homeActivity.activeFragment = fragment
                homeActivity.fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .add(R.id.main_container, fragment, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commit()
                return true
            }
            R.id.local_search_btn -> {

                return true
            }
            else -> return false
        }
    }

    override fun onItemClick(item: LocalArticle) {
        var fragment = UpdateDeleteArticleFragment.newInstance(item)
        homeActivity.activeFragment = fragment
        homeActivity.fragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .add(R.id.main_container, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }
}