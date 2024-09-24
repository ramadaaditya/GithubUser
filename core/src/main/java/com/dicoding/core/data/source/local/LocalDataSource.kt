package com.dicoding.core.data.source.local

import com.dicoding.core.data.source.local.database.UserDao
import com.dicoding.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(private val mUserDao: UserDao) {

    fun getAllFavorite(): Flow<List<UserEntity>> = mUserDao.getAllFavorite()
    fun getDataByUsername(username: String): Flow<UserEntity> = mUserDao.getDataByUsername(username)

    suspend fun insertUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            try {
                mUserDao.insert(user)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun deleteUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            try {
                mUserDao.delete(user)
            } catch (e: Exception) {
                throw e
            }
        }
    }
}