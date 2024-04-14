package com.dicoding.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.remote.response.Item
import com.dicoding.githubuser.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<ResultState<List<Item>>>()
    val user: LiveData<ResultState<List<Item>>> = _user

    init {
        searchUsers("Ramada")
    }

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _user.value = ResultState.Loading
            try {
                val users = repository.searchUsers(query)
                _user.value = ResultState.Success(users)
            } catch (e: Exception) {
                _user.value =
                    ResultState.Error("Terjadi kesalahan saat mencari user : ${e.message}")
            }
        }
    }
}