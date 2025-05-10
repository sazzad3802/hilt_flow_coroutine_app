package com.shk.hiltfeed.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shk.hiltfeed.data.local.dao.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userDao: UserDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // for reactive approach
//    val userCount: LiveData<Int> = userDao.getUserCount()


    // for non-reactive / fetch once approach
    /*val userCount = MutableLiveData<Int>()

    init {
        viewModelScope.launch {
            userCount.value = userDao.getUserCount()
        }
    }*/


    // flow approach if initialised and then collected

    private var _selectedTabIndex = MutableStateFlow(0)
    var selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    val lastCount: StateFlow<Int> = savedStateHandle.getStateFlow(LAST_COUNT, 0)

    private val _userCount = MutableStateFlow(0)
    val userCount: StateFlow<Int> = _userCount.asStateFlow()

    init {
        viewModelScope.launch {
            userDao.observeUserCount()
                .flowOn(Dispatchers.Default)
                .collect {
                    _userCount.value = it
                }
        }
    }

    fun updateLastCount(lastCount: Int) {
        savedStateHandle[LAST_COUNT] = lastCount
    }

    fun updateTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }

//    val userCount: Flow<Int> = userDao.observeUserCount() // can do this if collect and initialise at one go

    companion object {
        private const val LAST_COUNT = "LAST_COUNT"
    }
}