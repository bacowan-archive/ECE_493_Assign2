package com.example.brendan.assignment2.model;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by Brendan on 1/15/2016.
 */
public class MedianImageSectionFilter implements ImageSectionFilter {
    @Override
    public int filterSection(int[] pixels) {
        return colourMedian(pixels);
    }

    private static int colourMedian(int[] colours) {
        return Color.argb(medianAlphaFromColorArray(colours),
                medianRedFromColorArray(colours),
                medianGreenFromColorArray(colours),
                medianBlueFromColorArray(colours));
    }

    private static int medianAlphaFromColorArray(int[] colours) {
        int[] alphas = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            alphas[i] = Color.alpha(colours[i]);
        return median(alphas);
    }

    private static int medianRedFromColorArray(int[] colours) {
        int[] reds = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            reds[i] = Color.red(colours[i]);
        return median(reds);
    }

    private static int medianGreenFromColorArray(int[] colours) {
        int[] greens = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            greens[i] = Color.green(colours[i]);
        return median(greens);
    }

    private static int medianBlueFromColorArray(int[] colours) {
        int[] blues = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            blues[i] = Color.blue(colours[i]);
        return median(blues);
    }

    private static int median(int[] values) {
        Arrays.sort(values);
        int max = values[values.length-1];
        int min = values[0];
        return (min+max)/2;
    }
}
