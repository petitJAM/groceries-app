package com.alexpetitjean.groceries

import android.app.Application
import com.facebook.stetho.Stetho

class Groceries : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
