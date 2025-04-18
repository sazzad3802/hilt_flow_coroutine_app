package com.shk.hiltfeed.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shk.hiltfeed.data.local.dto.PostDto

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getPosts(limit: Int, offset: Int): List<PostDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<PostDto>)
}
