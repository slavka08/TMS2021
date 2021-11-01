package by.slavintodron.babyhelper.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import by.slavintodron.babyhelper.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }
}