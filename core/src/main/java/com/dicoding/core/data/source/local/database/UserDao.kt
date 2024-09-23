package com.dicoding.core.data.source.local.database

import androidx.room.*
import com.dicoding.core.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favUser: UserEntity)

    @Delete
    suspend fun delete(favUser: UserEntity)

    @Query("SELECT * FROM FavoriteUser ORDER BY login ASC")
    fun getAllFavorite(): Flow<List<UserEntity>>

    @Query("SELECT * FROM FavoriteUser WHERE login = :username")
    fun getDataByUsername(username: String): Flow<UserEntity>
}