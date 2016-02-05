package com.example.brendan.assignment2.presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.brendan.assignment2.model.BitmapUtils;
import com.example.brendan.assignment2.model.FileUtils;
import com.example.brendan.assignment2.model.RenderScriptSyncTask;
import com.example.brendan.assignment2.model.BubbleTask;
import com.example.brendan.assignment2.model.RippleTask;
import com.example.brendan.assignment2.model.SwirlTask;
import com.example.brendan.assignment2.view.MainActivity;

import java.io.IOException;
import java.util.Observable;

/**
 * Created by Brendan on 1/13/2016.
 */
public class MainActivityPresenter extends Observable {

    private Activity activity;

    private RenderScriptTask renderScriptTask;

    public MainActivityPresenter(MainActivity mainActivity) {
        this.activity = mainActivity;
        addObserver(mainActivity);
    }

    public void setBitmapFromPath(String path) {
        Bitmap bitmap = BitmapUtils.bitmapFromPath(path);
        forceNotifyObservers(bitmap);
    }

    public void setBitmapFromUri(Uri uri) throws IOException {
        Bitmap bitmap = BitmapUtils.bitmapFromUri(activity, uri);
        forceNotifyObservers(bitmap);
    }

    public void saveImage(Bitmap image) {
        try {
            FileUtils.saveImage(image, activity);
            forceNotifyObservers("Image Saved");
        } catch (IOException e) {
            forceNotifyObservers("Save Failed");
        }
    }

    public void bubbleImage(Bitmap image) {
        startRenderScriptTask(image, new BubbleTask());
    }

    public void swirlImage(Bitmap image) {
        startRenderScriptTask(image, new SwirlTask());
    }

    public void rippleImage(Bitmap image) {
        startRenderScriptTask(image, new RippleTask());
    }

    private void startRenderScriptTask(Bitmap image, RenderScriptSyncTask task) {
        if (renderScriptTask != null)
            renderScriptTask.cancel(false);
        task.createRenderScript(image, activity);
        renderScriptTask = new RenderScriptTask(task);
        renderScriptTask.execute();
    }

    private class RenderScriptTask extends AsyncTask<Void, Integer, Bitmap> {

        private RenderScriptSyncTask syncTask;

        public RenderScriptTask(RenderScriptSyncTask syncTask) {
            this.syncTask = syncTask;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return syncTask.execute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            forceNotifyObservers(result);
        }
    }

    private void forceNotifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
