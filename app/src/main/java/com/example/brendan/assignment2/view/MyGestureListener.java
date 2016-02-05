package com.example.brendan.assignment2.view;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.brendan.assignment2.Callback;
import com.example.brendan.assignment2.EmptyCallback;
import com.example.brendan.assignment2.R;

import java.util.ArrayList;

/**
 * Created by Brendan on 2/4/2016.
 */
class MyGestureListener extends GestureDetector.SimpleOnGestureListener implements GestureOverlayView.OnGesturePerformedListener {

    private GestureLibrary gestureLibrary;
    private Callback longPressCallback = new EmptyCallback();
    private Callback swirlCallback = new EmptyCallback();
    private Callback rippleCallback = new EmptyCallback();

    public MyGestureListener(Context c) {
        gestureLibrary = GestureLibraries.fromRawResource(c, R.raw.gestures);
        gestureLibrary.load();
    }

    public void setLongPressCallback(Callback callback) {
        longPressCallback = callback;
    }

    public void setSwirlCallback(Callback callback) {
        swirlCallback = callback;
    }

    public void setRippleCallback(Callback callback) {
        rippleCallback = callback;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        longPressCallback.call();
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

        if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
            String predictionName = predictions.get(0).name;
            if ("circle".equalsIgnoreCase(predictionName))
                swirlCallback.call();
            else if ("ripple".equalsIgnoreCase(predictionName))
                rippleCallback.call();
        }
    }
}
