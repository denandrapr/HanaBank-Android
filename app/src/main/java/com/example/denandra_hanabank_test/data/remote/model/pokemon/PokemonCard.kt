package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonCard(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("supertype")
    val supertype: String?,
    @SerializedName("subtypes")
    val subtypes: List<String>?,
    @SerializedName("hp")
    val hp: String?,
    @SerializedName("types")
    val types: List<String>?,
    @SerializedName("evolvesTo")
    val evolvesTo: List<String>?,
    @SerializedName("rules")
    val rules: List<String>?,
    @SerializedName("attacks")
    val attacks: List<Attack>?,
    @SerializedName("weaknesses")
    val weaknesses: List<Weakness>?,
    @SerializedName("retreatCost")
    val retreatCost: List<String>?,
    @SerializedName("convertedRetreatCost")
    val convertedRetreatCost: Int?,
    @SerializedName("set")
    val set: SetInfo?,
    @SerializedName("number")
    val number: String?,
    @SerializedName("artist")
    val artist: String?,
    @SerializedName("rarity")
    val rarity: String?,
    @SerializedName("nationalPokedexNumbers")
    val nationalPokedexNumbers: List<Int>?,
    @SerializedName("legalities")
    val legalities: Legalities?,
    @SerializedName("images")
    val images: ImageInfo?,
    @SerializedName("tcgplayer")
    val tcgplayer: TcgPlayer?
)