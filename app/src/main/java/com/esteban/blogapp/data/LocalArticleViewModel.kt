package com.esteban.blogapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalArticleViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<LocalArticle>>
    private val repository: ArticleRepository

    init {
        val articleDao = ArticleDatabase.getDatabase(application).articleDao()
        repository = ArticleRepository(articleDao)
        readAllData = repository.readAllData
    }

    fun addArticle(localArticle: LocalArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addArticle(localArticle)
        }
    }

    fun updateArticle(localArticle: LocalArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateArticle(localArticle)
        }
    }

    fun deleteArticle(localArticle: LocalArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticle(localArticle)
        }
    }
}