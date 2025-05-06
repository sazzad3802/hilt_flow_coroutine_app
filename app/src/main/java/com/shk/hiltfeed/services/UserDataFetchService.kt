package com.shk.hiltfeed.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.shk.hiltfeed.data.repository.UserRepository
import com.shk.hiltfeed.utils.ConstData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class UserDataFetchService : Service() {
    @Inject
    lateinit var userRepository: UserRepository
    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)

    val handler = Handler(Looper.getMainLooper())

    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            userRepository.fetchUser(ConstData.limit)
        }

        runnable = object : Runnable {
            override fun run() {
                coroutineScope.launch {
                    val lastUserList = userRepository.fetchUser(ConstData.limit)
                    if (lastUserList.isEmpty()) {
                        handler.removeCallbacks(runnable)
                    }
                }
                handler.postDelayed(this, 2 * 60000) //2 mins delay
            }
        }

        handler.post(runnable)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
        coroutineScope.cancel()
    }
}
