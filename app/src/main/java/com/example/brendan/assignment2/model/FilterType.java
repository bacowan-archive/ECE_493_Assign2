package com.example.brendan.assignment2.model;

/**
 * Created by Brendan on 1/15/2016.
 */
public enum FilterType {
    MEAN,
    MEDIAN;

    public static FilterType stringToFilterType(String str) {
        if (str.equals("0"))
            return MEAN;
        if (str.equals("1"))
            return MEDIAN;
        return null;
    }
}
