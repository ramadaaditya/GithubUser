package com.dicoding.githubuser.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteUser")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String = "",
    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null
)