package com.shk.hiltfeed.data.repository

import User
import com.shk.hiltfeed.base.ViewState
import com.shk.hiltfeed.data.SharedPref
import com.shk.hiltfeed.data.local.NewsFeedDb
import com.shk.hiltfeed.data.remote.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private var apiInterface: ApiInterface,
    private var newsFeedDb: NewsFeedDb,
    private var sharedPref: SharedPref
) {
    suspend fun fetchUser(limit: Int): List<User> {
        val userOffset = sharedPref.getValue(SharedPref.PAGE, 0) as Int
        val userResponse = apiInterface.getUsers(limit, userOffset * limit)
        if (userResponse.isSuccessful) {
            val userDtoList = User.toUserDtoList(userResponse.body()?.users);
            newsFeedDb.userDao().insertUsers(userDtoList)
            sharedPref.setValue(SharedPref.PAGE, userOffset+1)
            return userResponse.body()?.users?: emptyList()
        }
        return emptyList()
    }

    suspend fun fetchUserById(id: Long) = flow {
        val user = newsFeedDb.userDao().getUserById(id)
        if (user!=null) {
            emit(ViewState.Success(user))
        } else {
            val userResponse = apiInterface.getUserById(id)
            if (userResponse.isSuccessful) {
                val postDtoList = User.toUserDtoList(listOf(userResponse.body()));
                newsFeedDb.userDao().insertUsers(postDtoList)
                emit(ViewState.Success(userResponse.body()?.let { User.toUserDto(it) }))
            }
        }
    }.flowOn(Dispatchers.IO)

    fun fetchUserWithPost(id: Long) = flow {
        val user = newsFeedDb.userDao().getUserWithPosts(id)
        if (user!=null) {
            emit(ViewState.Success(user))
        } else {
            emit(ViewState.Error("Failed"))
        }
    }.catch {
        emit(ViewState.Error(it.message))
    }.flowOn(Dispatchers.IO)
}