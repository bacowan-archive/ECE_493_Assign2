package com.example.brendan.assignment2.model;

import android.graphics.Bitmap;

/**
 * Created by Brendan on 1/15/2016.
 *
 * Uses the template design pattern
 */
public class ImageFilter {

    private ImageSectionFilter sectionFilter;
    private int filterSize;

    public ImageFilter(ImageSectionFilter sectionFilter, int filterSize) {
        this.sectionFilter = sectionFilter;
        this.filterSize = filterSize;
    }

    public Bitmap filterImage(Bitmap image) {
        int imageSize = image.getHeight()*image.getWidth();
        int[] newImagePixels = new int[imageSize];

        for (int i = 0; i<imageSize; i++)
            newImagePixels[i] = filterImagePixel(image, i);

        return Bitmap.createBitmap(newImagePixels, image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
    }

    private int filterImagePixel(Bitmap image, int pixelIndex) {
        int pixelX = pixelIndex % image.getWidth();
        int pixelY = pixelIndex / image.getWidth();
        int leftSectionPixelIndex = Math.max(0, pixelX - pixelsOnSide());
        int sectionWidth = Math.min(image.getWidth()-1, pixelX + pixelsOnSide()+1) - leftSectionPixelIndex;
        int topSectionPixelIndex = Math.max(0, pixelY - pixelsOnSide());
        int sectionHeight = Math.min(image.getHeight()-1, pixelY + pixelsOnSide()+1) - topSectionPixelIndex;
        int[] surroundingPixels = new int[sectionHeight * sectionWidth];

        image.getPixels(surroundingPixels, 0, sectionWidth, leftSectionPixelIndex, topSectionPixelIndex, sectionWidth, sectionHeight);

        return sectionFilter.filterSection(surroundingPixels);
    }

    private int pixelsOnSide() {
        return filterSize / 2;
    }



}
