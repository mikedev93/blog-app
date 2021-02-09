package com.esteban.blogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esteban.blogapp.model.Article

@Database(entities = [LocalArticle::class], version = 1, exportSchema = false)
abstract class ArticleDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        const val dbName = "article_database"

        @Volatile
        private var INSTANCE: ArticleDatabase? = null

        fun getDatabase(context: Context): ArticleDatabase {
            val tempInstace = INSTANCE
            if (tempInstace != null) return tempInstace
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ArticleDatabase::class.java, dbName).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}