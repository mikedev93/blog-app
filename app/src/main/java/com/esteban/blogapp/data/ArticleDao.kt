package com.esteban.blogapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArticle(localArticle: LocalArticle)

    @Update
    suspend fun updateArticle(localArticle: LocalArticle)

    @Delete
    suspend fun deleteArticle(localArticle: LocalArticle)

    @Query("DELETE FROM article_table")
    suspend fun deleteAllArticles()

    @Query("SELECT * FROM article_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<LocalArticle>>
}