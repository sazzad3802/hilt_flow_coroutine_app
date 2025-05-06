package com.shk.hiltfeed.services

import android.content.Context
import androidx.work.*
import com.shk.hiltfeed.data.repository.UserRepository
import com.shk.hiltfeed.utils.ConstData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class UserFetchWorker (
    context: Context,
    workerParams: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                val limit = inputData.getInt("limit", 10)
                val lastUserList = userRepository.fetchUser(limit)

                // If we have more data to fetch, chain another worker
                if (lastUserList.isNotEmpty()) {
                    enqueueNextWorker()
                }

                Result.success()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun enqueueNextWorker() {
        val workRequest = OneTimeWorkRequestBuilder<UserFetchWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

    companion object {
        fun enqueue(context: Context) {
            val inputData = Data.Builder()
                .putInt("limit", ConstData.limit)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<UserFetchWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // instant execution
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "user_fetch_loop",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }

}