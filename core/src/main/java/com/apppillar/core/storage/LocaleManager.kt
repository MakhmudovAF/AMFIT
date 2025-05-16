package com.apppillar.core.storage

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleManager {

    private const val PREF_NAME = "amfit_prefs"
    private const val KEY_LANG = "selected_language"

    fun persistLanguage(context: Context, langCode: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANG, langCode).apply()
    }

    fun applyStoredLocale(context: Context): Context {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val langCode = prefs.getString(KEY_LANG, Locale.getDefault().language) ?: "en"
        return setLocale(context, langCode)
    }

    private fun setLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}