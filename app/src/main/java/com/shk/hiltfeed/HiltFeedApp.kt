package com.shk.hiltfeed

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.shk.hiltfeed.data.repository.UserRepository
import com.shk.hiltfeed.services.CustomWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltFeedApp : Application(), Configuration.Provider {

    @Inject
    lateinit var userRepository: UserRepository

    companion object {
        lateinit var instance: HiltFeedApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize WorkManager with the custom configuration
        if (!WorkManager.isInitialized()) {
            WorkManager.initialize(this, workManagerConfiguration)
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(CustomWorkerFactory(userRepository))
            .build()
}