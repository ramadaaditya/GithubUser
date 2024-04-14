package com.dicoding.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.repository.UserRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {
    private val _favorite = MutableLiveData<ResultState<List<UserEntity>>>()
    val favorite: LiveData<ResultState<List<UserEntity>>> = _favorite

    fun getAllFavorite() {
        _favorite.value = ResultState.Loading
        viewModelScope.launch {
            try {
                repository.getAllFavorite().collect { favoriteList ->
                    if (favoriteList.isEmpty()) {
                        _favorite.value =
                            ResultState.Error("Tidak ada data favorite yang ditemukan")
                    } else {
                        _favorite.value = ResultState.Success(favoriteList)
                    }
                }
            } catch (e: Exception) {
                _favorite.value = ResultState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}