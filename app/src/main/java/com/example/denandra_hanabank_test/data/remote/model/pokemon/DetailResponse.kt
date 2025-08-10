package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("data")
    val data: PokemonCard,
)