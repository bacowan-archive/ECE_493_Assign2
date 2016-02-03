package com.example.brendan.assignment2.model;

import android.graphics.Color;

/**
 * Created by Brendan on 1/15/2016.
 */
public class MeanImageSectionFilter implements ImageSectionFilter {
    @Override
    public int filterSection(int[] pixels) {
        return colourMean(pixels);
    }

    private static int colourMean(int[] colours) {
        return Color.argb(meanAlphaFromColorArray(colours),
                meanRedFromColorArray(colours),
                meanGreenFromColorArray(colours),
                meanBlueFromColorArray(colours));
    }

    private static int meanAlphaFromColorArray(int[] colours) {
        int[] alphas = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            alphas[i] = Color.alpha(colours[i]);
        return mean(alphas);
    }

    private static int meanRedFromColorArray(int[] colours) {
        int[] reds = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            reds[i] = Color.red(colours[i]);
        return mean(reds);
    }

    private static int meanGreenFromColorArray(int[] colours) {
        int[] greens = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            greens[i] = Color.green(colours[i]);
        return mean(greens);
    }

    private static int meanBlueFromColorArray(int[] colours) {
        int[] blues = new int[colours.length];
        for (int i=0; i<colours.length; i++)
            blues[i] = Color.blue(colours[i]);
        return mean(blues);
    }

    private static int mean(int[] values) {
        int sum = 0;
        for (int val : values)
            sum += val;
        return sum / values.length;
    }
}
