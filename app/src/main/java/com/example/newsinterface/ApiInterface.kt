package com.example.newsinterface
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterfaceHeadline {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String
    ): Call<NewsResponse>
}