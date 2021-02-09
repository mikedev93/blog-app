package com.esteban.blogapp.repos

import android.util.Log
import com.esteban.blogapp.api.APIConstants
import com.esteban.blogapp.api.NewsAPI
import com.esteban.blogapp.api.NewsServices
import com.esteban.blogapp.contract.NewsFeedContract
import com.esteban.blogapp.model.Article
import com.esteban.blogapp.model.SuccessfulResponse
import com.esteban.blogapp.utils.DebugUtil
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFeedRepos : NewsFeedContract.NewsFeedModel {

    private var apiClient: NewsServices? = null
    private var articles: ArrayList<Article?>? = null
    private var totalResults: Int? = null
    private var currentPage: Int? = null

    init {
        apiClient = NewsAPI.client.create(NewsServices::class.java)
    }

    override fun getArticles(): ArrayList<Article?>? {
        return articles
    }

    override fun getTotalResults(): Int? {
        return totalResults
    }

    override fun getCurrentPage(): Int {
        return currentPage!!
    }

    override fun updatePage(page: Int) {
        currentPage = page
    }

    override fun getEveryNewsArticles(requestParams: Map<String, String>, presenter: NewsFeedContract.NewsFeedPresenter) {
        val call = apiClient?.getEveryNewsArticles(APIConstants.API_KEY, requestParams)
        call?.enqueue(object : Callback<SuccessfulResponse> {
            override fun onFailure(call: Call<SuccessfulResponse>, t: Throwable) {
                presenter.displayErrorView(t.message)
            }

            override fun onResponse(call: Call<SuccessfulResponse>, response: Response<SuccessfulResponse>) {
                if (response.isSuccessful) {
                    if ("ok" == response.body()?.status) {
                        DebugUtil.logResponse("Response", Gson().toJson(response.body()))
                        totalResults = response.body()?.totalResults
                        if (articles == null) {
                            articles = response.body()?.articles
                        } else {
                            response.body()?.articles?.let { articles?.addAll(it) }
                            currentPage = getCurrentPage() + 1
                        }
                        presenter.updateUI()
                    } else {
                        presenter.displayErrorView(response.body()?.message)
                    }
                } else {
                    DebugUtil.logResponse("Response", Gson().toJson(response.body()))
                    presenter.displayErrorView("Service error")
                }
            }
        })
    }
}