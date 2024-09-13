package com.dicoding.githubuser.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.di.Injection
import com.dicoding.githubuser.repository.UserRepository
import com.dicoding.githubuser.ui.detail.DetailViewModel
import com.dicoding.githubuser.ui.favorite.FavoriteViewModel
import com.dicoding.githubuser.ui.fragment.FollowViewModel
import com.dicoding.githubuser.ui.main.MainViewModel

class ViewModelFactory(
    private val repository: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FollowViewModel::class.java) -> {
                FollowViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            val repository = Injection.provideRepository(application)
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(repository).also { instance = it }
            }
        }
    }
}