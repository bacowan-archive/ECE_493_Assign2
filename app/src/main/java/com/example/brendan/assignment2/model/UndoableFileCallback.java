package com.example.brendan.assignment2.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.brendan.assignment2.Callback;
import com.example.brendan.assignment2.view.UndoContainer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Brendan on 2/7/2016.
 */
public class UndoableFileCallback implements Callback {

    private UndoContainer<File> undoContainer;
    private Bitmap image;
    private Context context;

    public UndoableFileCallback(UndoContainer<File> undoContainer, Context context) {
        this.undoContainer = undoContainer;
        this.context = context;
    }

    public void call() {
        if (image != null) {
            try {
                File tempFile = BitmapUtils.saveBitmapTemporarily(image, context);
                undoContainer.append(tempFile);
            } catch (IOException e) {}
        }
    }

    protected void setImage(Bitmap image) {
        this.image = image;
    }
}
