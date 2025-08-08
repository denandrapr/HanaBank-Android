package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class SetInfo(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("series")
    val series: String?,
    @SerializedName("printedTotal")
    val printedTotal: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("legalities")
    val legalities: Legalities?,
    @SerializedName("ptcgoCode")
    val ptcgoCode: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("images")
    val images: ImageInfo?
)