package com.example.klinq.api

import com.example.klinq.model.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("6701/253620?lang=en&store=KWD")
    suspend fun getProduct(): Product
}