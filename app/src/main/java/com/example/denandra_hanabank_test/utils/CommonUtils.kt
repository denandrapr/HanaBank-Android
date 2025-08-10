package com.example.denandra_hanabank_test.utils

object CommonUtils {
    fun Float.dp(context: android.content.Context): Float {
        return this * context.resources.displayMetrics.density
    }
}