package com.example.denandra_hanabank_test.di

import android.content.Context
import androidx.room.Room
import com.example.denandra_hanabank_test.data.local.AppDatabase
import com.example.denandra_hanabank_test.data.local.dao.PokemonCardDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "pokemon_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides @Singleton
    fun providePokemonDao(db: AppDatabase): PokemonCardDao = db.pokemonCardDao()
}