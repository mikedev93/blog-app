package com.esteban.blogapp.view.localPosts

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.esteban.blogapp.R
import com.esteban.blogapp.data.LocalArticle
import com.esteban.blogapp.view.remoteNews.LocalArticlesDiffCallback
import kotlinx.android.synthetic.main.item_article.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LocalArticleAdapter (private var context: Context, private var articles: ArrayList<LocalArticle?>, private var listener: OnArticleClickListener) : RecyclerView.Adapter<LocalArticleAdapter.ArticleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            holder.bindViewHolder(context, articles[position], listener)
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val thumbnail = itemView.thumbnail
        val title = itemView.titleTextView
        val author = itemView.authorTextView
        val date = itemView.dateTextView
        val progressBar = itemView.progress

        fun bindViewHolder(context: Context, item: LocalArticle?, listener: OnArticleClickListener) {

            if (item != null) {
                title.text = item.title
                author.text = item.author
                var dateFormatInput: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                var dateFormatOutput: DateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
                if (item.publishedAt != null) {
                    dateFormatInput.timeZone = TimeZone.getDefault()
                    var publishedDate = dateFormatInput.parse(item.publishedAt)
                    date.text = "- " + dateFormatOutput.format(publishedDate)
                } else {
                    date.visibility = View.GONE
                }
                if (item.imageUrl != null) {
                    Glide.with(context)
                        .load(item.imageUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                if (progressBar != null) {
                                    progressBar.visibility = View.GONE
                                }
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                if (progressBar != null) {
                                    progressBar.visibility = View.GONE
                                }
                                return false
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(thumbnail)
                }else if (item.pathToImage != null) {
                    Glide.with(context)
                        .load(Uri.parse(item.pathToImage))
                        .centerCrop()
                        .into(thumbnail)
                    progressBar.visibility = View.GONE
                } else {
                    Glide.with(context)
                        .load(R.drawable.ic_baseline_image_search_24)
                        .centerCrop()
                        .into(thumbnail)
                    progressBar.visibility = View.GONE
                }
            }

            itemView.setOnClickListener{view: View? -> listener.onItemClick(item!!) }
        }
    }

    fun getItemAtPosition(position: Int): LocalArticle {
        return articles[position]!!
    }

    fun updateList(newItems: List<LocalArticle?>) {
        var diffResult = DiffUtil.calculateDiff(LocalArticlesDiffCallback(newItems, this.articles))
        diffResult.dispatchUpdatesTo(this)
        this.articles.clear()
        this.articles.addAll(newItems)
    }

    interface OnArticleClickListener {

        fun onItemClick(item: LocalArticle)

    }
}