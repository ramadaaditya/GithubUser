package com.dicoding.githubuser.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.settings.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val prefs: SettingPreferences): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return prefs.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkMode: Boolean) {
        viewModelScope.launch {
            prefs.saveThemeSetting(isDarkMode)
        }
    }

    class ViewModelFactory(private val pref: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                return SettingsViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}