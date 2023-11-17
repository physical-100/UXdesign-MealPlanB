package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.mealplanb.R

class AlarmSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
    //아아
}