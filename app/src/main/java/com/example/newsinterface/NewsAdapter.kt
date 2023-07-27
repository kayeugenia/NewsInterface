package com.example.newsinterface

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var newsData: List<Article> = emptyList()
    companion object {
        private const val FIRST_ARTICLE_VIEW_TYPE = 0
        private const val REGULAR_ARTICLE_VIEW_TYPE = 1
        private const val DIVIDER_VIEW_TYPE = 2
    }
    fun setNewsData(newsData: List<Article>) {
        this.newsData = newsData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FIRST_ARTICLE_VIEW_TYPE -> {
                val firstArticleView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.first_news_layout, parent, false)
                FirstArticleViewHolder(firstArticleView)
            }
            DIVIDER_VIEW_TYPE -> {
                val dividerView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.divider_layout, parent, false)
                DividerViewHolder(dividerView)
            }
            else -> {
                val regularArticleView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.all_news_layout, parent, false)
                NewsViewHolder(regularArticleView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FirstArticleViewHolder -> {
                val article = newsData[position]
                holder.bindFirstArticle(article)
            }
            is NewsViewHolder -> {
                val article = newsData[position]
                holder.bind(article)
            }
            is DividerViewHolder -> {
                // No need to bind data for the divider view since it's just a static view
            }
        }
    }

    override fun getItemCount(): Int = newsData.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> FIRST_ARTICLE_VIEW_TYPE
            1 -> DIVIDER_VIEW_TYPE
            else -> REGULAR_ARTICLE_VIEW_TYPE
        }
    }

    inner class FirstArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun formatDate(dateString: String): String? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            return try {
                val date: Date = inputFormat.parse(dateString)
                outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                dateString // Return original string if parsing fails
            }
        }

        // Add references to views in the first article layout
        private val titleTextView: TextView = itemView.findViewById(R.id.firstTitleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.firstAuthorTextView)
        private val publishedAtTextView: TextView = itemView.findViewById(R.id.firstPublishedAtTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.firstImageView)
        private val cardView: CardView = itemView.findViewById(R.id.firstCardView)

        fun bindFirstArticle(article: Article) {
            titleTextView.text = article.title
            authorTextView.text = article.author
            publishedAtTextView.text = formatDate(article.publishedAt ?: "N/A")
            Log.d("Article", "URL to Image first news: ${article.urlToImage}")
            Glide.with(itemView)
                .load(article.urlToImage)
                .apply(RequestOptions().override(Target.SIZE_ORIGINAL, 550))
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(imageView)

            cardView.setOnClickListener {
                // Get the article URL
                val articleUrl = article.url

                // Check if the URL is not null or empty
                if (!articleUrl.isNullOrEmpty()) {
                    // Create an Intent to open the article URL in a web browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun formatDate(dateString: String): String? {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            return try {
                val date: Date = inputFormat.parse(dateString)
                outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                dateString // Return original string if parsing fails
            }
        }

        // Add references to views in the regular article layout
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val publishedAtTextView: TextView = itemView.findViewById(R.id.publishedAtTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val cardView: CardView = itemView.findViewById(R.id.cardView)

        fun bind(article: Article) {
            titleTextView.text = article.title
            authorTextView.text = article.author
            publishedAtTextView.text = formatDate(article.publishedAt ?: "N/A")
            Log.d("Article", "URL to Image: ${article.urlToImage}")
            Glide.with(itemView)
                .load(article.urlToImage)
                .centerCrop()
                .error(R.drawable.placeholder)
                .into(imageView)

            cardView.setOnClickListener {
                // Get the article URL
                val articleUrl = article.url

                // Check if the URL is not null or empty
                if (!articleUrl.isNullOrEmpty()) {
                    // Create an Intent to open the article URL in a web browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    inner class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Nothing to bind for the divider layout, as it's just static text
    }
}
