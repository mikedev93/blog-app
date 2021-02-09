package com.esteban.blogapp.view.remoteNews

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.esteban.blogapp.R
import com.esteban.blogapp.model.Article
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.progress_bar_loading.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ArticleAdapter(private var context: Context, private var articles: ArrayList<Article?>, private var listener: OnArticleClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == RecyclerConstant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
            ArticleViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.progress_bar_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == RecyclerConstant.VIEW_TYPE_ITEM) {
            (holder as ArticleViewHolder).bindViewHolder(context, articles[position], listener)
        } else {
            (holder as LoadingViewHolder).relativeLayout.visibility = View.VISIBLE
            holder.progressBar.visibility = View.VISIBLE
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar
        val relativeLayout: RelativeLayout
        init {
            progressBar = itemView.progressbar
            relativeLayout = itemView.root
        }
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val thumbnail = itemView.thumbnail
        val title = itemView.titleTextView
        val author = itemView.authorTextView
        val date = itemView.dateTextView
        val progressBar = itemView.progress

        fun bindViewHolder(context: Context, item: Article?, listener: OnArticleClickListener) {

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
                if (item.urlToImage != null) {
                    Glide.with(context)
                        .load(item.urlToImage)
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

    fun getItemAtPosition(position: Int): Article {
        return articles[position]!!
    }

    fun addLoadingView() {
        Handler().post {
            articles.add(null)
            notifyItemInserted(articles.size - 1)
        }
    }

    fun removeLoadingView() {
        if (articles.size != 0) {
            if (articles[articles.size - 1] == null) {
                articles.removeAt(articles.size - 1)
                notifyItemRemoved(articles.size)
            }
        }
    }

    fun updateList(newItems: ArrayList<Article?>) {
        var diffResult = DiffUtil.calculateDiff(ArticlesDiffCallback(newItems, this.articles))
        diffResult.dispatchUpdatesTo(this)
        this.articles.clear()
        this.articles.addAll(newItems)
    }

    interface OnArticleClickListener {

        fun onItemClick(item: Article)

    }
}