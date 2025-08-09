package com.example.denandra_hanabank_test.data.repository

import com.example.denandra_hanabank_test.data.local.dao.PokemonCardDao
import com.example.denandra_hanabank_test.data.mapper.toListPokemonEntity
import com.example.denandra_hanabank_test.data.mapper.toListPokemonUI
import com.example.denandra_hanabank_test.data.remote.api.ApiService
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: ApiService,
    private val dao: PokemonCardDao
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCards(queryFlow: Flow<String>): Flow<List<PokemonCard>> =
        queryFlow
            .map { it.trim().lowercase() }
            .distinctUntilChanged()
            .flatMapLatest { q -> dao.observeAll(q) }
            .map { list -> list.map { it.toListPokemonUI() } }

    suspend fun getPokemon(page: Int, pageSize: Int) : ApiResultHandler<Int> {
        return try {

            val response = api.getCards(page = page, pageSize = pageSize)
            if (!response.isSuccessful) {
                return ApiResultHandler.Error(
                    "Server error: ${response.code()}",
                    response.code()
                )
            }
            val list = response.body()?.data ?: emptyList()
            val base = (page - 1) * pageSize
            val time = System.currentTimeMillis()
            dao.upsertAll(list.mapIndexed { i, c ->
                c.toListPokemonEntity(base + i, page, time)
            })
            ApiResultHandler.Success(list.size)

        } catch (e: IOException) {
            ApiResultHandler.Error("No internet connection ${e.message}")
        } catch (e: Exception) {
            ApiResultHandler.Error("Unknown error: ${e.localizedMessage}")
        }
    }

    suspend fun cachedCount(): Int = dao.count()

    suspend fun clearAll() = dao.clearAll()
}