package com.esteban.blogapp.model

import com.google.gson.annotations.SerializedName

class SuccessfulResponse (

    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("articles")
    val articles: ArrayList<Article?>?,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String
)