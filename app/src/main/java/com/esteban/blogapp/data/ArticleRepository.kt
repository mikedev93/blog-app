package com.esteban.blogapp.data

import androidx.lifecycle.LiveData

class ArticleRepository(private val articleDao: ArticleDao) {

    val readAllData: LiveData<List<LocalArticle>> = articleDao.readAllData()

    suspend fun addArticle(localArticle: LocalArticle) {
        articleDao.addArticle(localArticle)
    }

    suspend fun updateArticle(localArticle: LocalArticle) {
        articleDao.updateArticle(localArticle)
    }

    suspend fun deleteArticle(localArticle: LocalArticle) {
        articleDao.deleteArticle(localArticle)
    }
}