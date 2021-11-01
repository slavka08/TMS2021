package by.slavintodron.babyhelper.app

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BabyHelperApp: Application() {
    val prefs: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
}