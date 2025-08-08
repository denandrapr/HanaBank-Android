package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class Legalities(
    @SerializedName("unlimited")
    val unlimited: String?,
    @SerializedName("expanded")
    val expanded: String?
)