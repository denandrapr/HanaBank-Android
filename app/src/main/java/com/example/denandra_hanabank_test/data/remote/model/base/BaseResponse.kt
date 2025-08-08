package com.example.denandra_hanabank_test.data.remote.model.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data")
    val data: List<T>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("error")
    val error: String? = null
)