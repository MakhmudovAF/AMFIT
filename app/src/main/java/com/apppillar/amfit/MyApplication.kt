package com.apppillar.amfit

import android.app.Application
import android.content.Context
import com.apppillar.core.storage.LocaleManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        val newContext = LocaleManager.applyStoredLocale(base)
        super.attachBaseContext(newContext)
    }
}