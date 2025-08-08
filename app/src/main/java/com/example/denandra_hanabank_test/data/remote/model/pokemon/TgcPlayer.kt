package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class TcgPlayer(
    @SerializedName("url")
    val url: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("prices")
    val prices: Prices?
)