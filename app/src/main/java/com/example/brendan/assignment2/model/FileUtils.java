package com.example.brendan.assignment2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Brendan on 2/5/2016.
 */
public class FileUtils {
    public static void saveImage(Bitmap image, Context context) throws IOException {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDirectory, createFileName());
        FileOutputStream out = new FileOutputStream(imageFile);
        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();
        MediaStore.Images.Media.insertImage(context.getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), imageFile.getName());
    }

    public static String createFileName() {
        String timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date());
        return "IMAGE_" + timeStamp + "_";
    }
}
