package com.andrey.fbstest.network

import com.andrey.fbstest.model.RepoItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitApi {
    @GET("search/repositories")
    fun getCityList(
        @Query("q") term: String,
        @Query("page") page: String
    ): Call<RepoItems>
}