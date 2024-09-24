package com.dicoding.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.core.domain.usecase.UserUseCase

class FavoriteViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    fun getAllFavorite() = userUseCase.getAllFavorite().asLiveData()
}