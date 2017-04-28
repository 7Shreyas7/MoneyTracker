package com.hmproductions.moneytracker.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.hmproductions.moneytracker.R;

/**
 * Created by Harsh Mahajan on 26/3/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.settings);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int prefCount = prefScreen.getPreferenceCount();

        for(int i=0 ; i< prefCount ; ++i)
        {
            Preference p = prefScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference))
            {
                String value = sharedPreferences.getString(p.getKey(),"");
                setPreferenceSummary(p, value);
            }
        }
    }

    void setPreferenceSummary(Preference preference, String value)
    {
        if(preference instanceof EditTextPreference)
        {
            preference.setSummary(value);
        }

        else if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference)preference;
            int index = listPreference.findIndexOfValue(value);
            if(index>=0)
                listPreference.setSummary(listPreference.getEntries()[index]);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if(preference != null) {
            if (preference instanceof EditTextPreference)
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), "2000"));

            else if(preference instanceof ListPreference)
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(),"Date"));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
