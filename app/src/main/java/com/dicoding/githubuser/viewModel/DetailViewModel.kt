package com.dicoding.githubuser.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.data.response.DetailResponse
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.data.api.ApiConfig
import com.dicoding.githubuser.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val detailUserData: UserRepository = UserRepository(application)

    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser: MutableLiveData<DetailResponse> = _detailUser

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val errorMessage = MutableLiveData<String>()

    fun insertDataUser(user: UserEntity) {
        detailUserData.insertUser(user)
    }

    fun deleteDataUser(user: UserEntity) {
        detailUserData.deleteUser(user)
    }


    init {
        getDetailUser()
    }

    fun getDetailUser(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                errorMessage.postValue(t.message.toString())
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(username: String = "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDataByUsername(username: String) = detailUserData.getDataByUsername(username)

    class ViewModelFactory(private val application: Application) :
        ViewModelProvider.NewInstanceFactory() {
        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <view : ViewModel> create(modelClass: Class<view>): view {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(application) as view
            }
            throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        private const val TAG = "detailViewModel"
    }

}

