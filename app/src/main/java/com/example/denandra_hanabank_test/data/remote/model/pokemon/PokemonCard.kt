package com.example.denandra_hanabank_test.data.remote.model.pokemon

import com.google.gson.annotations.SerializedName

data class PokemonCard(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("supertype")
    val supertype: String? = null,
    @SerializedName("subtypes")
    val subtypes: List<String>? = null,
    @SerializedName("hp")
    val hp: String? = null,
    @SerializedName("types")
    val types: List<String>?,
    @SerializedName("evolvesFrom")
    val evolvesFrom: String?,
    @SerializedName("evolvesTo")
    val evolvesTo: List<String>? = null,
    @SerializedName("rules")
    val rules: List<String>? = null,
    @SerializedName("attacks")
    val attacks: List<Attack>? = null,
    @SerializedName("weaknesses")
    val weaknesses: List<Weakness>? = null,
    @SerializedName("retreatCost")
    val retreatCost: List<String>? = null,
    @SerializedName("convertedRetreatCost")
    val convertedRetreatCost: Int? = null,
    @SerializedName("set")
    val set: SetInfo? = null,
    @SerializedName("number")
    val number: String? = null,
    @SerializedName("artist")
    val artist: String? = null,
    @SerializedName("rarity")
    val rarity: String?,
    @SerializedName("nationalPokedexNumbers")
    val nationalPokedexNumbers: List<Int>? = null,
    @SerializedName("legalities")
    val legalities: Legalities? = null,
    @SerializedName("images")
    val images: ImageInfo?,
    @SerializedName("tcgplayer")
    val tcgplayer: TcgPlayer? = null
)