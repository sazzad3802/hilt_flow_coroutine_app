package com.shk.hiltfeed.data
import android.content.SharedPreferences

class SharedPref constructor(private var sharedPreferences: SharedPreferences) {

    companion object {
        const val PAGE = "page"
    }


    fun getValue(key: String, default: Any?): Any? {
        return when (default) {
            is Boolean -> sharedPreferences.getBoolean(key, default)
            is String -> sharedPreferences.getString(key, default)
            is Int -> sharedPreferences.getInt(key, default)
            is Float -> sharedPreferences.getFloat(key, default)
            else -> sharedPreferences.getString(key, default as String?)
        }
    }


    fun setValue(key: String, value: Any?) {
        when (value) {
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Float -> sharedPreferences.edit().putFloat(key, value).apply()
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
        }
    }


    fun clear(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}