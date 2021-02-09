package com.esteban.blogapp.view.remoteNews

import androidx.recyclerview.widget.DiffUtil
import com.esteban.blogapp.data.LocalArticle
import com.esteban.blogapp.model.Article
import com.esteban.blogapp.utils.DebugUtil
import com.google.gson.Gson
import java.lang.Exception

class ArticlesDiffCallback(private var newArticles: ArrayList<Article?>, private var oldArticles: ArrayList<Article?>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldArticles.size
    }

    override fun getNewListSize(): Int {
        return newArticles.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldArticles[oldItemPosition]?.description == newArticles[newItemPosition]?.description
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        var oldItem = oldArticles[oldItemPosition]
        var newItem = newArticles[newItemPosition]
        try {
            oldArticles[oldItemPosition]?.equals(newArticles[newItemPosition])!!
        } catch (e: Exception) {
            e.printStackTrace()
            DebugUtil.logResponse("ArticlesDiffCallback - OI", Gson().toJson(oldItem))
            DebugUtil.logResponse("ArticlesDiffCallback - NI", Gson().toJson(newItem))
            return false
        }
        return oldArticles[oldItemPosition]?.equals(newArticles[newItemPosition])!!
    }
}

class LocalArticlesDiffCallback(private var newArticles: List<LocalArticle?>, private var oldArticles: List<LocalArticle?>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldArticles.size
    }

    override fun getNewListSize(): Int {
        return newArticles.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldArticles[oldItemPosition]?.content == newArticles[newItemPosition]?.content
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        var oldItem = oldArticles[oldItemPosition]
        var newItem = newArticles[newItemPosition]
        try {
            oldArticles[oldItemPosition]?.equals(newArticles[newItemPosition])!!
        } catch (e: Exception) {
            e.printStackTrace()
            DebugUtil.logResponse("ArticlesDiffCallback - OI", Gson().toJson(oldItem))
            DebugUtil.logResponse("ArticlesDiffCallback - NI", Gson().toJson(newItem))
            return false
        }
        return oldArticles[oldItemPosition]?.equals(newArticles[newItemPosition])!!
    }
}