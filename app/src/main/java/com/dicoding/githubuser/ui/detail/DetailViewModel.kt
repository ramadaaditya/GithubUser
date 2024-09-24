package com.dicoding.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.core.domain.model.User
import com.dicoding.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    fun getDataByUsername(username: String) = userUseCase.getDataByUsername(username)?.asLiveData()
    fun getDetailUser(username: String) = userUseCase.getDetailUser(username).asLiveData()
    fun deleteDataUser(user: User) {
        viewModelScope.launch {
            userUseCase.deleteUser(user)
        }
    }

    fun insertDataUser(user: User) {
        viewModelScope.launch {
            userUseCase.insertUser(user)
        }
    }
    fun getFollowers(username: String) = userUseCase.getFollowers(username).asLiveData()
    fun getFollowing(username: String) = userUseCase.getFollowing(username).asLiveData()
}