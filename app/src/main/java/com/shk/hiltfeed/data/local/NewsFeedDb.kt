package com.shk.hiltfeed.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shk.hiltfeed.data.local.dao.BlogDao
import com.shk.hiltfeed.data.local.dao.PostDao
import com.shk.hiltfeed.data.local.dao.UserDao
import com.shk.hiltfeed.data.local.dto.BlogDto
import com.shk.hiltfeed.data.local.dto.PostDto
import com.shk.hiltfeed.data.local.dto.UserDto


@Database(
    entities = [
        PostDto::class,
        UserDto::class,
        BlogDto::class,
    ],
    version = 1
)
abstract class NewsFeedDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun blogDao(): BlogDao
}
