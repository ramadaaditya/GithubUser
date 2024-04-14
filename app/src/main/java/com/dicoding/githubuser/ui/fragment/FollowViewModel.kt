package com.dicoding.githubuser.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.data.ResultState
import com.dicoding.githubuser.data.remote.response.Item
import com.dicoding.githubuser.repository.UserRepository
import kotlinx.coroutines.launch

class FollowViewModel(private val repository: UserRepository) : ViewModel() {
    private val _follow = MutableLiveData<ResultState<List<Item>>>()
    val follow: LiveData<ResultState<List<Item>>> = _follow

    fun getFollowing(username: String) {
        viewModelScope.launch {
            try {
                _follow.value = ResultState.Loading
                val response = repository.getFollowing(username)
                _follow.value = ResultState.Success(response)
            } catch (e: Exception) {
                _follow.value =
                    ResultState.Error("Terjadi kesalahan saat mendapatkan following : ${e.message}")
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            try {
                _follow.value = ResultState.Loading
                val response = repository.getFollowers(username)
                _follow.value = ResultState.Success(response)
            } catch (e: Exception) {
                _follow.value =
                    ResultState.Error("Terjadi kesalahan saat mendapatkan following : ${e.message}")
            }
        }
    }
}