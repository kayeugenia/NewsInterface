package com.example.newsinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsinterface.Article
import com.example.newsinterface.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter()
        recyclerView.adapter = newsAdapter

        fetchNewsData()
    }
    private fun fetchNewsData() {
        val apiKey = "1b59e342d6954f2390b98508a9261857"
        val country = "us"
        val apiClient = ApiClient.getClient()
        val apiInterface = apiClient.create(ApiInterfaceHeadline::class.java)
        val call: Call<NewsResponse> = apiInterface.getTopHeadlines(apiKey, country)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call : Call<NewsResponse>?, t : Throwable?) {
                Log.d("HIT_NEWS", "Fail to fetch news")
            }

            override fun onResponse(call: Call<NewsResponse>?, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        val articles: List<Article> = newsResponse.articles
                        newsAdapter.setNewsData(articles)
                    } else {
                        Log.d("HIT_NEWS", "Response body is null")
                    }
                } else {
                    val errorCode = response.code()
                    Log.d("HIT_NEWS", "Error response with status code: $errorCode")
                }
            }

        })
    }
}