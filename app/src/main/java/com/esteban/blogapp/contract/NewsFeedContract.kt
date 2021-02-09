package com.esteban.blogapp.contract

import com.esteban.blogapp.model.Article

interface NewsFeedContract {

    interface NewsFeedView {
        fun updateView()
        fun displayError(message: String?)
    }

    interface NewsFeedPresenter {
        fun getAllArticles(requestParams: Map<String, String>)
        fun showArticles(): ArrayList<Article?>?
        fun getCurrentPage(): Int
        fun setCurrentPage(page: Int)
        fun updateUI()
        fun displayErrorView(message: String?)
    }

    interface NewsFeedModel {
        fun getEveryNewsArticles(requestParams: Map<String, String>, presenter: NewsFeedPresenter)
        fun getArticles(): ArrayList<Article?>?
        fun getTotalResults(): Int?
        fun getCurrentPage(): Int
        fun updatePage(page: Int)
    }
}