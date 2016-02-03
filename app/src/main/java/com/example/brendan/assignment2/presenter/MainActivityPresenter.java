package com.example.brendan.assignment2.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.brendan.assignment2.model.BitmapUtils;
import com.example.brendan.assignment2.model.FilterType;
import com.example.brendan.assignment2.view.MainActivity;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by Brendan on 1/13/2016.
 */
public class MainActivityPresenter extends Observable {

    public MainActivityPresenter(MainActivity mainActivity) {
        addObserver(mainActivity);
    }

    public void setBitmapFromPath(String path) {
        Bitmap bitmap = BitmapUtils.bitmapFromPath(path);
        forceNotifyObservers(bitmap);
    }

    public void setBitmapFromUri(Uri uri, Activity activity) throws IOException {
        Bitmap bitmap = BitmapUtils.bitmapFromUri(activity, uri);
        forceNotifyObservers(bitmap);
    }

    public void filterImage(Bitmap initialImage, FilterType filterType, int filterSize) {
        FilterParameter parameter = new FilterParameter(initialImage, filterType, filterSize);
        new FilterImageTask().execute(parameter);
    }

    private class FilterImageTask extends AsyncTask<FilterParameter, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(FilterParameter... params) {
            return BitmapUtils.filterImage(params[0].image, params[0].filterType, params[0].filterSize);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            forceNotifyObservers(result);
        }
    }

    private class FilterParameter {

        public Bitmap image;
        public FilterType filterType;
        public int filterSize;

        public FilterParameter(Bitmap image, FilterType filterType, int filterSize) {
            this.image = image;
            this.filterType = filterType;
            this.filterSize = filterSize;
        }
    }

    private void forceNotifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }

}
