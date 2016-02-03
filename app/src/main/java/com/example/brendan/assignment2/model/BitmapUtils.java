package com.example.brendan.assignment2.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.brendan.assignment2.Exceptions.InvalidFilterType;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Brendan on 1/13/2016.
 */
public class BitmapUtils {

    public static Bitmap bitmapFromPath(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap bitmapFromUri(Activity activity, Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
    }

    public static Bitmap filterImage(Bitmap image, FilterType filterType, int filterSize) {
        return createFilter(filterType, filterSize).filterImage(image);
    }

    private static ImageFilter createFilter(FilterType filterType, int filterSize) {
        return new ImageFilter(createSectionFilter(filterType), filterSize);
    }

    private static ImageSectionFilter createSectionFilter(FilterType filterType) {
        if (filterType.equals(FilterType.MEAN))
            return new MeanImageSectionFilter();
        if (filterType.equals(FilterType.MEDIAN))
            return new MedianImageSectionFilter();
        throw new InvalidFilterType(filterType.name());
    }

}
