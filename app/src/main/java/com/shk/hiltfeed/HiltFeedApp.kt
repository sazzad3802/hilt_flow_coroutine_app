package com.shk.hiltfeed

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltFeedApp : Application(){
    companion object {
        lateinit var instance: HiltFeedApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}