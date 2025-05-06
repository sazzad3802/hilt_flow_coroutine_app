package com.shk.hiltfeed.data
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPref constructor(private var sharedPreferences: SharedPreferences) {

    companion object {
        const val PAGE = "page"
    }

    fun getInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }


    fun getValue(key: String, default: Any?): Any? {
        return when (default) {
            is Boolean -> sharedPreferences.getBoolean(key, default)
            is String -> sharedPreferences.getString(key, default)
            is Int -> sharedPreferences.getInt(key, default)
            is Float -> sharedPreferences.getFloat(key, default)
            is Long -> sharedPreferences.getLong(key, default)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }


    fun setValue(key: String, value: Any?) {
        when (value) {
            is Boolean -> sharedPreferences.edit { putBoolean(key, value) }
            is String -> sharedPreferences.edit { putString(key, value) }
            is Float -> sharedPreferences.edit { putFloat(key, value) }
            is Int -> sharedPreferences.edit { putInt(key, value) }
            is Long -> sharedPreferences.edit { putLong(key, value) }
        }
    }


    fun clear(key: String) {
        sharedPreferences.edit { remove(key) }
    }
}