package com.alexpetitjean.groceries

import android.app.Application
import com.facebook.stetho.Stetho
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.MaterialModule
import com.squareup.leakcanary.LeakCanary

class Groceries : Application() {

    override fun onCreate() {
        super.onCreate()
        // This must be the first thing in onCreate
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.enableDisplayLeakActivity(this)
        LeakCanary.install(this)

        Stetho.initializeWithDefaults(this)

        Iconify.with(MaterialModule())
    }
}
