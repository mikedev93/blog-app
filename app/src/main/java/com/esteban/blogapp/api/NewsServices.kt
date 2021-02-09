package com.esteban.blogapp.api

import com.esteban.blogapp.model.SuccessfulResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface NewsServices {

    @GET("top-headlines")
    fun getTopNewsArticles(@Header("X-Api-Key") header: String, @QueryMap queryMap: Map<String, String>): Call<SuccessfulResponse>

    @GET("everything")
    fun getEveryNewsArticles(@Header("X-Api-Key") header: String, @QueryMap queryMap: Map<String, String>): Call<SuccessfulResponse>
}