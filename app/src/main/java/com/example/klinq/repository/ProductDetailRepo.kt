package com.example.klinq.repository

import com.example.klinq.api.ApiService
import com.example.klinq.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductDetailRepo @Inject constructor(private val apiService: ApiService) {
    suspend fun getProduct(): Flow<Product> = flow {
        emit(apiService.getProduct())
    }.flowOn(Dispatchers.IO)
}