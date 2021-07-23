package com.example.ledgr

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val TAG = "acali SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d(TAG, "onCreatePreference")
        /**
        PreferenceManager.getDefaultSharedPreferences(requireContext()).registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            onSharedPreferenceChanged(sharedPreferences, key)
        }
        */



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val version = findPreference<Preference>("version")

        version?.summaryProvider = Preference.SummaryProvider<Preference> { preference ->
            getPackageVersion()
        }

    }


    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        val key = preference?.key
        val pref = sharedPreferences.getString(key, "")
        Log.d(TAG, "Pending Preference value: $key: $pref")

        return super.onPreferenceTreeClick(preference)

    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        Log.d(TAG, "")
        return true
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val selectedPref = sharedPreferences!!.getString(key, "")
        Log.e(TAG, "onSharedPreferenceChanged $selectedPref")
        if (key == "darkmode") {
            when (selectedPref) {
                "dark" -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
                "light" -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
                "system" -> { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) }
            }
        }
        else if (key == "theme") {
            val themeId = requireContext().resources.getIdentifier(selectedPref, "style", requireContext().packageName)
            requireActivity().setTheme(themeId)
            requireActivity().recreate()
        }
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            onSharedPreferenceChanged(sharedPreferences, key)
        }
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        sharedPreferences.unregisterOnSharedPreferenceChangeListener { sharedPreferences, key ->
            onSharedPreferenceChanged(sharedPreferences, key)
        }
        super.onPause()
    }

    private fun getPackageVersion(): String {
        val packageInfo = context?.packageManager?.getPackageInfo(context?.packageName.toString(), 0)
        return if (Build.VERSION.SDK_INT >= 28) {
            "${packageInfo?.versionName} (${packageInfo?.longVersionCode})"
        }
        else {
            "${packageInfo?.versionName}"
        }

    }


}