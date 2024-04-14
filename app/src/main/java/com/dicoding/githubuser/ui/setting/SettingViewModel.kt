package com.dicoding.githubuser.ui.setting

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val prefs: SettingPreferences) : ViewModel() {

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
            if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
                return SettingViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}