package com.example.denandra_hanabank_test.data.repository

import com.example.denandra_hanabank_test.data.local.dao.PokemonCardDao
import com.example.denandra_hanabank_test.data.mapper.toListPokemonEntity
import com.example.denandra_hanabank_test.data.mapper.toListPokemonUI
import com.example.denandra_hanabank_test.data.remote.api.ApiService
import com.example.denandra_hanabank_test.data.remote.model.handler.ApiResultHandler
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: ApiService,
    private val dao: PokemonCardDao
) {
    fun observeCards(): Flow<List<PokemonCard>> =
        dao.observeAll().map { it.map { data -> data.toListPokemonUI() } }

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