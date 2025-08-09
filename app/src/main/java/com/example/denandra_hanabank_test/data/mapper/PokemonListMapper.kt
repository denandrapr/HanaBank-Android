package com.example.denandra_hanabank_test.data.mapper

import com.example.denandra_hanabank_test.data.local.entity.PokemonCardEntity
import com.example.denandra_hanabank_test.data.remote.model.pokemon.ImageInfo
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard

fun PokemonCardEntity.toListPokemonUI(): PokemonCard =
    PokemonCard(
        id = id,
        name = name,
        supertype = null,
        subtypes = null,
        hp = null,
        types = null,
        evolvesTo = null,
        rules = null,
        attacks = null,
        weaknesses = null,
        retreatCost = null,
        convertedRetreatCost = null,
        set = null,
        number = null,
        artist = null,
        rarity = rarity,
        nationalPokedexNumbers = null,
        legalities = null,
        images = ImageInfo(small = imageSmall, large = null),
        tcgplayer = null
    )

fun PokemonCard.toListPokemonEntity(indexInResponse: Int, page: Int, time: Long) =
    PokemonCardEntity(
        id = id,
        name = name,
        imageSmall = images?.small,
        rarity = rarity,
        indexInResponse = indexInResponse,
        page = page,
        updatedAt = time
    )