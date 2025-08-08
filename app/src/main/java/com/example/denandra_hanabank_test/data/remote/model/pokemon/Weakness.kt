package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class Weakness(
    @SerializedName("type")
    val type: String?,
    @SerializedName("value")
    val value: String?
)