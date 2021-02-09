package com.esteban.blogapp.model

import com.google.gson.annotations.SerializedName

class Source(
    @SerializedName("id")
    val id: Any? = null,

    @SerializedName("name")
    val name: String
)