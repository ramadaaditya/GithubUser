package com.dicoding.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuser.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: UserEntity)

    @Delete
    fun delete(favUser: UserEntity)

    @Query("SELECT * FROM FavoriteUser ORDER BY username ASC")
    fun getAllFavorite(): Flow<List<UserEntity>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<List<UserEntity>>
}