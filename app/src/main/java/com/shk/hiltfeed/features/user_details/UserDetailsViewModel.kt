package com.shk.hiltfeed.features.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shk.hiltfeed.base.ViewState
import com.shk.hiltfeed.data.local.dto.UserDto
import com.shk.hiltfeed.data.repository.UserRepository
import com.shk.hiltfeed.features.user_details.adapters.UserPostRecyclerViewAdapter
import com.shk.hiltfeed.listeners.ItemClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(private var userRepository: UserRepository) : ViewModel(),
    ItemClickListener {
    var postRecyclerViewAdapter = UserPostRecyclerViewAdapter()
    var user = MutableSharedFlow<UserDto>()

    fun fetchUserWithPost(id: Long) {
        viewModelScope.launch {
            userRepository.fetchUserWithPost(id).collect {
                when (it) {
                    is ViewState.Success -> {
                        it.data?.let { it1 ->
                            user.emit(it1.user)
                            postRecyclerViewAdapter.setData(it1.posts,this@UserDetailsViewModel)
                            postRecyclerViewAdapter.notifyDataSetChanged()
                        }
                    }

                    is ViewState.Error -> TODO()
                    ViewState.Loading -> TODO()
                    ViewState.PaginationLoading -> TODO()
                }
            }
        }
    }

    override fun onClick(data: Any) {
        TODO("Not yet implemented")
    }
}