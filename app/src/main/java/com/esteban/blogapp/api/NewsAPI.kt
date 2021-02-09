package com.esteban.blogapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsAPI {

    companion object {

        private var retrofit: Retrofit? = null

        val client: Retrofit
            get() {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(APIConstants.BASE_URL)
                        .build()
                }
                return retrofit!!
            }
    }
}