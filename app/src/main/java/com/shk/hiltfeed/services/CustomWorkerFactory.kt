package com.shk.hiltfeed.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.shk.hiltfeed.data.repository.UserRepository


class CustomWorkerFactory(private val userRepository: UserRepository) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UserFetchWorker::class.java.name -> {
                UserFetchWorker(appContext, workerParameters, userRepository)
            }
            else -> {
                // Return null so the default WorkerFactory can handle other workers
                null
            }
        }
    }
}