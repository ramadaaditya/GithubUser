package com.dicoding.githubuser.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.dicoding.core.data.source.Resource
import com.dicoding.core.domain.model.User
import com.dicoding.core.domain.usecase.UserUseCase

class HomeViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    var username: MutableLiveData<String> = MutableLiveData()
    val user: LiveData<Resource<List<User>>> = username.switchMap { query ->
        userUseCase.getSearchUsers(query).asLiveData()
    }
}