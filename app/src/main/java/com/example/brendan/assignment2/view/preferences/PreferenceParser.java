package com.example.brendan.assignment2.view.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.brendan.assignment2.R;
import com.example.brendan.assignment2.model.FilterType;

/**
 * Created by Brendan on 1/15/2016.
 */
public class PreferenceParser {

    private String filterTypeKey;
    private String filterSizeKey;
    private Context context;

    public PreferenceParser(Context context) {
        this.context = context;
        initializePreferenceKeys(context);
    }

    private void initializePreferenceKeys(Context context) {
        Resources res = context.getResources();
        filterTypeKey = res.getString(R.string.filter_type_key);
        filterSizeKey = res.getString(R.string.filter_size_key);
    }

    public FilterType getFilterType() {
        return FilterType.stringToFilterType(getPreferences().getString(filterTypeKey, ""));
    }

    public int getFilterSize() {
        return Integer.parseInt(getPreferences().getString(filterSizeKey, "3"));
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
