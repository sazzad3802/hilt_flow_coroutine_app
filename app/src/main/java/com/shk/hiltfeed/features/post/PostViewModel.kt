package com.shk.hiltfeed.features.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shk.hiltfeed.base.ViewState
import com.shk.hiltfeed.data.local.dto.PostDto
import com.shk.hiltfeed.data.repository.PostRepository
import com.shk.hiltfeed.features.post.adapters.PostRecyclerViewAdapter
import com.shk.hiltfeed.listeners.ItemClickListener
import com.shk.hiltfeed.utils.ConstData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(private var postRepository: PostRepository) : ViewModel(),
    ItemClickListener {
    var postRecyclerViewAdapter = PostRecyclerViewAdapter()
    var progressBarVisibility = MutableStateFlow(false)
    var paginateProgressBarVisibility = MutableStateFlow(false)
    var isLoading = MutableStateFlow(false)
    var selectedPost = MutableSharedFlow<PostDto>()


    init {
        fetchPosts(ConstData.limit, 0)
    }


    fun fetchPosts(limit: Int, page: Int) {
        viewModelScope.launch {
            try {
                postRepository.fetchPost(limit, page).collect {
                    when (it) {
                        is ViewState.Loading -> {
                            progressBarVisibility.emit(true)
                            paginateProgressBarVisibility.emit(false)
                            isLoading.emit(true)
                            Log.d("LLL","LOading")
                        }

                        is ViewState.PaginationLoading -> {
                            progressBarVisibility.emit(false)
                            paginateProgressBarVisibility.emit(true)
                            isLoading.emit(true)
                            Log.d("LLL","LOadingPage")
                        }

                        is ViewState.Success -> {
                            it.data?.let { it1 ->
                                progressBarVisibility.emit(false)
                                paginateProgressBarVisibility.emit(false)
                                isLoading.emit(false)
                                postRecyclerViewAdapter.setData(it1,this@PostViewModel)
                                postRecyclerViewAdapter.notifyDataSetChanged()
                            }
                        }

                        is ViewState.Error -> {
                            progressBarVisibility.value = false
                            paginateProgressBarVisibility.value = false
                            Log.d("ERRR",""+it)
                        }
                    }
                }
            }catch (e:Exception){

            }
        }
    }

    override fun onClick(data: Any) {
        viewModelScope.launch {
            selectedPost.emit(data as PostDto)
        }
    }
}