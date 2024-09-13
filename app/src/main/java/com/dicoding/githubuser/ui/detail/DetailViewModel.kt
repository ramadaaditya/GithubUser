package com.dicoding.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.remote.response.DetailResponse
import com.dicoding.githubuser.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<ResultState<DetailResponse>>()
    val user: LiveData<ResultState<DetailResponse>> = _user

    fun getDetailUser(username: String) {
        viewModelScope.launch {
            try {
                _user.value = ResultState.Loading
                val response = repository.getDetailUser(username)
                _user.value = ResultState.Success(response)
            } catch (e: Exception) {
                _user.value =
                    ResultState.Error("Terjadi kesalahan saat mendapatkan detail user : ${e.message}")
            }
        }
    }

    fun getDataByUsername(username: String) = repository.getDataByUsername(username)

    fun deleteDataUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun insertDataUser(user: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}