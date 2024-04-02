package com.example.klinq.utils

import com.example.klinq.model.Product

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    class Success(val data: Product) : ApiState()
    object Empty : ApiState()
}