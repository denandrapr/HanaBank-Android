package com.example.denandra_hanabank_test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.denandra_hanabank_test.data.local.dao.PokemonCardDao
import com.example.denandra_hanabank_test.data.local.entity.PokemonCardEntity
import com.example.denandra_hanabank_test.data.local.utils.Converters

@Database(
    entities = [PokemonCardEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonCardDao(): PokemonCardDao
}