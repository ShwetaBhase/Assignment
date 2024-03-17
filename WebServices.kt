package com.example.demoproject.Retrofit

import com.example.demoproject.Model.DataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("users/{page}")
    fun getData(
        @Query("page") pageIndex: Int
    ): Call<DataModel?>?
}
