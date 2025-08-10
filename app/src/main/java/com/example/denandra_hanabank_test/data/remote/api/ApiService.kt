package com.example.denandra_hanabank_test.data.remote.api

import com.example.denandra_hanabank_test.data.remote.model.base.BaseResponse
import com.example.denandra_hanabank_test.data.remote.model.pokemon.DetailResponse
import com.example.denandra_hanabank_test.data.remote.model.pokemon.PokemonCard
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("cards")
    suspend fun getCards(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 8
    ): Response<BaseResponse<PokemonCard>>

    @GET("cards/{id}")
    suspend fun getCardDetailsById(
        @Path("id") id: String
    ): Response<DetailResponse>
}