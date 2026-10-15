package com.ilyassavli.helloworld

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HelloWorldApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // OBS-01: logs only in debug. With crash reporting chosen, plant the release tree from
            // references/integrations.md instead of planting nothing.
            Timber.plant(Timber.DebugTree())
            // PERF-01: surface disk/network access on the main thread during development.
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectLeakedClosableObjects().penaltyLog().build())
        }
    }
}
