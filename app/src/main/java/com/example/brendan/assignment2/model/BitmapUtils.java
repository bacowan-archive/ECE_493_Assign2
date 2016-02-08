package com.example.brendan.assignment2.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static File saveBitmapTemporarily(Bitmap image, Context c) throws IOException {
        File outputFile = new File(c.getCacheDir() + SimpleDateFormat.getDateTimeInstance().format(new Date()));
        OutputStream outStream = new FileOutputStream(outputFile);
        image.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
        return outputFile;
    }

}
