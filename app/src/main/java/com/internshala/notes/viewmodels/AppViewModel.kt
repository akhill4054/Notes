package com.internshala.notes.viewmodels

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.internshala.notes.utils.PreferenceManager

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _prefs = PreferenceManager.getInstance(application)

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    init {
        if (_prefs.getIsThemeEverModified()) {
            val isDarkMode = _prefs.getIsDarkMode()

            // Notify observers
            _isDarkMode.value = isDarkMode

            // Change app theme forcefully
            setTheme(isDarkMode)
        } else {
            val currentNightMode =
                application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            _isDarkMode.value = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }
    }

    fun toggleDarkMode() {
        val isDarkMode = !_isDarkMode.value!!

        // Update prefs
        _prefs.updateThemeModifiedFlag()
        _prefs.setIsDarkMode(isDarkMode)

        // Update status
        _isDarkMode.value = isDarkMode

        // Change app theme forcefully
        setTheme(isDarkMode)
    }

    private fun setTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}