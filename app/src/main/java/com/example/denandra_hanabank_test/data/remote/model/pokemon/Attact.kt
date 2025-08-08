package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class Attack(
    @SerializedName("name")
    val name: String?,
    @SerializedName("cost")
    val cost: List<String>?,
    @SerializedName("convertedEnergyCost")
    val convertedEnergyCost: Int?,
    @SerializedName("damage")
    val damage: String?,
    @SerializedName("text")
    val text: String?
)