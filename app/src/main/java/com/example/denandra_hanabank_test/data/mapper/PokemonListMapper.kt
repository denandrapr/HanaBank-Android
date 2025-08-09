package com.example.denandra_hanabank_test.data.mapper

import com.example.denandra_hanabank_test.data.local.entity.PokemonCardEntity
import com.example.denandra_hanabank_test.data.remote.model.pokemon.ImageInfo
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard

fun PokemonCardEntity.toListPokemonUI(): PokemonCard =
    PokemonCard(
        id = id,
        name = name,
        types = types,
        evolvesFrom = evolvesFrom,
        rarity = rarity,
        images = ImageInfo(small = imageSmall)
    )

fun PokemonCard.toListPokemonEntity(indexInResponse: Int, page: Int, time: Long) =
    PokemonCardEntity(
        id = id,
        name = name,
        imageSmall = images?.small,
        rarity = rarity,
        evolvesFrom = evolvesFrom,
        types = types,
        indexInResponse = indexInResponse,
        page = page,
        updatedAt = time
    )