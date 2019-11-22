package com.andrey.fbstest.network

import com.andrey.fbstest.model.repository.RepoItems
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Controller {
    private val BASE_URL = "https://api.github.com/"
    private var retrofit: Retrofit

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    }

    fun getRepositoryList(query: String, page:String): Call<RepoItems> {
        val cityApi = retrofit.create(GitApi::class.java)
        return cityApi.getCityList(query, page)
    }

}