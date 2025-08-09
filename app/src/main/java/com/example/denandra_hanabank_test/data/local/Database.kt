package com.example.denandra_hanabank_test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.denandra_hanabank_test.data.local.dao.PokemonCardDao
import com.example.denandra_hanabank_test.data.local.entity.PokemonCardEntity

@Database(
    entities = [PokemonCardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonCardDao(): PokemonCardDao
}