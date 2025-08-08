package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class Holofoil(
    @SerializedName("low")
    val low: Double?,
    @SerializedName("mid")
    val mid: Double?,
    @SerializedName("high")
    val high: Double?,
    @SerializedName("market")
    val market: Double?,
    @SerializedName("directLow")
    val directLow: Double?
)