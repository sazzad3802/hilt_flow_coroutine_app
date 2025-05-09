package com.shk.hiltfeed.features.post

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.shk.hiltfeed.base.ViewState
import com.shk.hiltfeed.data.local.dto.PostDto
import com.shk.hiltfeed.data.repository.PostRepository
import com.shk.hiltfeed.features.post.adapters.PostRecyclerViewAdapter
import com.shk.hiltfeed.features.post_details.PostDetailsActivity
import com.shk.hiltfeed.listeners.ItemClickListener
import com.shk.hiltfeed.services.UserFetchWorker
import com.shk.hiltfeed.utils.AppKeys
import com.shk.hiltfeed.utils.ConstData
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


/*
@HiltViewModel
class PostViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private var postRepository: PostRepository
) : ViewModel(),
    ItemClickListener {
    var postRecyclerViewAdapter = PostRecyclerViewAdapter()
    var progressBarVisibility = MutableStateFlow(false)
    var paginateProgressBarVisibility = MutableStateFlow(false)
    var isLoading = MutableStateFlow(false)
    var selectedPost = MutableSharedFlow<PostDto>()

    private var isWorkScheduled = false

    init {
        fetchPosts(ConstData.limit, 0)
    }

    fun fetchUsersIfNeeded() {
        if (isWorkScheduled) return

        UserFetchWorker.enqueue(appContext)
        isWorkScheduled = true
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
}*/




data class PostState(
    val posts: List<PostDto> = emptyList(),
    val isLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class PostViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private var postRepository: PostRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PostState())
    val state: StateFlow<PostState> = _state.asStateFlow()

    private var currentPage = 0
    private var isWorkScheduled = false

    init {
        fetchPosts(ConstData.limit, currentPage)
    }

    fun fetchUsersIfNeeded() {
        if (isWorkScheduled) return
        UserFetchWorker.enqueue(appContext)
        isWorkScheduled = true
    }

    fun fetchPosts(limit: Int, page: Int) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(
                    isLoading = page == 0,
                    isPaginationLoading = page > 0
                ) }

                postRepository.fetchPost(limit, page).collect { result ->
                    when (result) {
                        is ViewState.Success -> {
                            result.data?.let { posts ->
                                _state.update {
                                    it.copy(
                                        posts = if (page == 0) posts else it.posts + posts,
                                        isLoading = false,
                                        isPaginationLoading = false
                                    )
                                }
                            }
                        }
                        is ViewState.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isPaginationLoading = false,
                                    error = result.message ?: "Unknown error"
                                )
                            }
                        }
                        else -> { /* Handle other states if needed */ }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isPaginationLoading = false,
                        error = e.localizedMessage ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun onPostClick(post: PostDto) {
        viewModelScope.launch {
            val intent = Intent(appContext, PostDetailsActivity::class.java).apply {
                putExtra(AppKeys.POST, post)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(intent)
        }
    }
}