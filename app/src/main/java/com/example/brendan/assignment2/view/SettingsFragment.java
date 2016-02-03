package com.example.brendan.assignment2.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.brendan.assignment2.R;


/**
 * Created by Brendan on 1/15/2016.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
