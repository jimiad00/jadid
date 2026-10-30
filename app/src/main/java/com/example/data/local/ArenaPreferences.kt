package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class ArenaPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("grand_line_arena_prefs", Context.MODE_PRIVATE)

    fun getCustomApiKey(): String {
        return prefs.getString(KEY_GEMINI_API_KEY, "") ?: ""
    }

    fun saveCustomApiKey(key: String) {
        prefs.edit().putString(KEY_GEMINI_API_KEY, key.trim()).apply()
    }

    fun clearCustomApiKey() {
        prefs.edit().remove(KEY_GEMINI_API_KEY).apply()
    }

    companion object {
        private const val KEY_GEMINI_API_KEY = "custom_gemini_api_key"
    }
}
