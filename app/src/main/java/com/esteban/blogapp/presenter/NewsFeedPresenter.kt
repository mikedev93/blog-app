package com.esteban.blogapp.presenter

import com.esteban.blogapp.contract.NewsFeedContract
import com.esteban.blogapp.repos.NewsFeedRepos

class NewsFeedPresenter(newsFeedView: NewsFeedContract.NewsFeedView): NewsFeedContract.NewsFeedPresenter {

    private var view: NewsFeedContract.NewsFeedView = newsFeedView
    private var model: NewsFeedContract.NewsFeedModel = NewsFeedRepos()

    override fun showArticles() = model.getArticles()

    override fun updateUI() {
        view.updateView()
    }

    override fun getCurrentPage(): Int {
        return  model.getCurrentPage()
    }

    override fun setCurrentPage(page: Int) {
        model.updatePage(page)
    }

    override fun getAllArticles(requestParams: Map<String, String>) {
        model.getEveryNewsArticles(requestParams, this)
    }

    override fun displayErrorView(message: String?) {
        view.displayError(message)
    }
}