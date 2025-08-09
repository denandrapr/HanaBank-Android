package com.example.denandra_hanabank_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_cards")
data class PokemonCardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageSmall: String?,
    val rarity: String?,
    val indexInResponse: Int,
    val evolvesFrom: String?,
    val types: List<String>?,
    val page: Int,
    val updatedAt: Long
)