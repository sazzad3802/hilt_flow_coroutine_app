package com.shk.hiltfeed.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.shk.hiltfeed.data.local.dto.UserDto
import com.shk.hiltfeed.data.local.relations.UserWithPosts
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id=:id")
    fun getUserById(id: Long): UserDto?

    @Query("SELECT COUNT(*) FROM users")
//    fun getUserCount(): LiveData<Int> // for reactive approach

//    suspend fun getUserCount(): Int     // for non-reactive/ fetch once approach

    fun observeUserCount(): Flow<Int>   // flow version

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(posts: List<UserDto>)

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithPosts(userId: Long): UserWithPosts?
}
