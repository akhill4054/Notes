package com.internshala.notes.utils

import android.app.Application
import android.content.Context
import com.internshala.notes.BuildConfig
import com.internshala.notes.data.models.User

class PreferenceManager private constructor(application: Application) {

    private val _sharedPrefs = application.getSharedPreferences(
        "${BuildConfig.APPLICATION_ID}.ASSESSMENT.PREFS_FILE_KEY", Context.MODE_PRIVATE
    )

    // Current user
    fun getCurrentUser(): User? {
        val json = getString(KEY_USER)

        return if (json.isNotEmpty()) {
            User.fromJson(json)
        } else {
            null
        }
    }

    fun setCurrentUser(user: User?) {
        putString(KEY_USER, user?.toJson() ?: "")
    }

    // Theme
    fun setIsDarkMode(isDarkMode: Boolean) {
        putBool(KEY_IS_DARK_MODE, isDarkMode)
    }

    fun getIsDarkMode(): Boolean {
        return getBool(KEY_IS_DARK_MODE)
    }

    fun updateThemeModifiedFlag() {
        putBool(KEY_WAS_THEME_EVER_MODIFIED, true)
    }

    fun getIsThemeEverModified(): Boolean {
        return getBool(KEY_WAS_THEME_EVER_MODIFIED)
    }

    private fun getString(key: String): String {
        return _sharedPrefs.getString(key, "")!!
    }

    private fun putString(key: String, value: String) {
        val editor = _sharedPrefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getBool(key: String): Boolean {
        return _sharedPrefs.getBoolean(key, false)
    }

    private fun putBool(key: String, value: Boolean) {
        val editor = _sharedPrefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    companion object {
        private const val KEY_USER = "user"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
        private const val KEY_WAS_THEME_EVER_MODIFIED = "was_theme_ever_modified"

        @Volatile
        private var INSTANCE: PreferenceManager? = null

        fun getInstance(application: Application): PreferenceManager {
            var localRef = INSTANCE // To reduce one read operation on volatile field

            return localRef ?: synchronized(PreferenceManager::class.java) {
                localRef = INSTANCE // To reduce one read operation on volatile field
                localRef ?: PreferenceManager(application).also {
                    // Initializing static field
                    INSTANCE = it
                }
            }
        }
    }
}