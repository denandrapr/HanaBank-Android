package com.example.denandra_hanabank_test.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.denandra_hanabank_test.data.local.entity.PokemonCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonCardDao {

    @Query("""
    SELECT * FROM pokemon_cards
    WHERE (:q IS NULL OR :q = '' 
           OR LOWER(name) LIKE '%' || :q || '%'
           OR LOWER(COALESCE(REPLACE(types, '||', ' '), '')) LIKE '%' || :q || '%'
           OR LOWER(COALESCE(evolvesFrom, '')) LIKE '%' || :q || '%')
    ORDER BY indexInResponse ASC
""")
    fun observeAll(q: String): Flow<List<PokemonCardEntity>>

    @Upsert
    suspend fun upsertAll(items: List<PokemonCardEntity>)

    @Query("DELETE FROM pokemon_cards")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM pokemon_cards")
    suspend fun count(): Int
}