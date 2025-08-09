package com.example.denandra_hanabank_test.data.repository

import com.example.denandra_hanabank_test.data.remote.api.ApiService
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: ApiService
) {
    private val items = mutableListOf<PokemonCard>()
    private val _cardsFlow = MutableStateFlow<List<PokemonCard>>(emptyList())

    fun observeCards(): Flow<List<PokemonCard>> = _cardsFlow

    suspend fun getPokemon(page: Int, pageSize: Int): ApiResultHandler<Int> {
        return try {
            val response = api.getCards(page = page, pageSize = pageSize)
            if (!response.isSuccessful) {
                return ApiResultHandler.Error(
                    "Server error: ${response.code()}",
                    response.code()
                )
            }

            val list = response.body()?.data ?: emptyList()

            if (page == 1) items.clear()
            items.addAll(list)

            _cardsFlow.value = items.toList()

            ApiResultHandler.Success(list.size)
        } catch (e: IOException) {
            ApiResultHandler.Error("No internet connection ${e.message}")
        } catch (e: Exception) {
            ApiResultHandler.Error("Unknown error: ${e.localizedMessage}")
        }
    }

    fun clearAll() {
        items.clear()
        _cardsFlow.value = emptyList()
    }
}