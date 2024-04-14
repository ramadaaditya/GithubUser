package com.dicoding.githubuser.di

import android.app.Application
import com.dicoding.githubuser.data.local.room.UserDatabase
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.repository.UserRepository
import com.dicoding.githubuser.ui.setting.SettingPreferences
import com.dicoding.githubuser.ui.setting.dataStore

object Injection {
    fun provideRepository(application: Application): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getDatabase(application)
        return UserRepository.getInstance(apiService, database.userDao())
    }

    fun provideSettingPreferences(application: Application): SettingPreferences {
        return SettingPreferences.getInstance(application.dataStore)
    }
}