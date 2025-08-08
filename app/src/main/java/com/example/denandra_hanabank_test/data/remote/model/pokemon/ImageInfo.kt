package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class ImageInfo(
    @SerializedName("symbol")
    val symbol: String? = null,
    @SerializedName("logo")
    val logo: String? = null,
    @SerializedName("small")
    val small: String? = null,
    @SerializedName("large")
    val large: String? = null
)