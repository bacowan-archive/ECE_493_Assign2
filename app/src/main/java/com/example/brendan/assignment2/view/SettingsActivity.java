package com.example.brendan.assignment2.view;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Brendan on 1/14/2016.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
