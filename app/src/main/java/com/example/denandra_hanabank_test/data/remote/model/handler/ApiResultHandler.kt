package com.example.denandra_hanabank_test.data.remote.model.handler

sealed class ApiResultHandler<out T> {
    data class Success<out T>(val data: T) : ApiResultHandler<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResultHandler<Nothing>()
    object Loading : ApiResultHandler<Nothing>()
}