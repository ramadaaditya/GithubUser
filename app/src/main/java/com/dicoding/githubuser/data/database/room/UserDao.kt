package com.dicoding.githubuser.data.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuser.data.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: UserEntity)

    @Delete
    fun delete(favUser: UserEntity)

    @Query("SELECT * FROM FavoriteUser ORDER BY username  ASC")
    fun getAllFavorite(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getDataByUsername(username: String): LiveData<List<UserEntity>>

}