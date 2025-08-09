package com.example.denandra_hanabank_test.data.local.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? =
        list?.joinToString("||")

    @TypeConverter
    fun toStringList(data: String?): List<String>? =
        data?.takeIf { it.isNotEmpty() }?.split("||")
}