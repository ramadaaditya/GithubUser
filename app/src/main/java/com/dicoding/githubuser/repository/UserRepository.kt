package com.dicoding.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.database.entity.UserEntity
import com.dicoding.githubuser.data.database.room.UserDao
import com.dicoding.githubuser.data.database.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllFavorite(): LiveData<List<UserEntity>> = mUserDao.getAllFavorite()

    fun insertUser(user: UserEntity) {
        executorService.execute {
            mUserDao.insert(user)
        }
    }

    fun deleteUser(user: UserEntity) {
        executorService.execute {
            mUserDao.delete(user)
        }
    }

    fun getDataByUsername(username: String): LiveData<List<UserEntity>> = mUserDao.getDataByUsername(username)

}