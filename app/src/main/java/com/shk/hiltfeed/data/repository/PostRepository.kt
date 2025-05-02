package com.shk.hiltfeed.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.shk.hiltfeed.base.ViewState
import com.shk.hiltfeed.data.local.NewsFeedDb
import com.shk.hiltfeed.data.models.Post
import com.shk.hiltfeed.data.remote.ApiInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class PostRepository @Inject constructor(
    private var apiInterface: ApiInterface,
    private var newsFeedDb: NewsFeedDb,
    @ApplicationContext private val context: Context
) {

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true
    }

    fun fetchPost(limit: Int, page: Int) = flow {
        if (page > 0) {
            emit(ViewState.PaginationLoading)
        } else {
            emit(ViewState.Loading)
        }

        val offset = page * limit
        try {
            if (isNetworkAvailable()) {
                try {
                    val response = apiInterface.getPosts(limit, offset)
                    if (response.isSuccessful) {
                        val postsFromApi = Post.toPostDtoList(response.body()?.posts)
                        newsFeedDb.postDao().insertPosts(postsFromApi)
                        emit(ViewState.Success(postsFromApi))
                    } else {
                        // fallback to local
                        val localData = newsFeedDb.postDao().getPosts(limit, offset)
                        if (localData.isNotEmpty()) {
                            emit(ViewState.Success(localData))
                        } else {
                            emit(ViewState.Error(response.message()))
                        }
                    }
                } catch (e: Exception) {
                    // fallback to local on exception (e.g. timeout)
                    val localData = newsFeedDb.postDao().getPosts(limit, offset)
                    if (localData.isNotEmpty()) {
                        emit(ViewState.Success(localData))
                    } else {
                        emit(ViewState.Error(e.message ?: "Network error"))
                    }
                }
            } else {
                // No internet, fallback to local
                val localData = newsFeedDb.postDao().getPosts(limit, offset)
                if (localData.isNotEmpty()) {
                    emit(ViewState.Success(localData))
                } else {
                    emit(ViewState.Error("No internet and no cached data"))
                }
            }
        } catch (ex: Exception){
            emit(ViewState.Error(ex.message))
        }


        /*val postFromLocal = newsFeedDb.postDao().getPosts(limit, offset)
        if (postFromLocal.isNotEmpty()) {
            emit(ViewState.Success(postFromLocal))
        }

        if (isNetworkAvailable()) {
            val postResponse = apiInterface.getPosts(limit, offset)
            if (postResponse.isSuccessful) {
                val postDtoList = Post.toPostDtoList(postResponse.body()?.posts);
                newsFeedDb.postDao().insertPosts(postDtoList)
                emit(ViewState.Success(postDtoList))
            } else {
                emit(ViewState.Error(postResponse.message()))
            }
        } else if (postFromLocal.isEmpty()) {
            emit(ViewState.Error("No Internet & No local data"))
        }*/
    }.flowOn(Dispatchers.IO)
}