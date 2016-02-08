package com.example.brendan.assignment2.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.brendan.assignment2.Callback;
import com.example.brendan.assignment2.Exceptions.NoMoreUndosException;
import com.example.brendan.assignment2.R;
import com.example.brendan.assignment2.model.FileUtils;
import com.example.brendan.assignment2.model.UndoableFileCallback;
import com.example.brendan.assignment2.presenter.MainActivityPresenter;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer, SharedPreferences.OnSharedPreferenceChangeListener {

    private static int CAPTURE_IMAGE_REQUEST = 1;
    private static int RESULT_LOAD_IMAGE = 2;

    private ImageView imageView;
    private GestureOverlayView gestureView;

    private GestureDetectorCompat gestureDetector;

    private Bitmap image;
    private Uri imageUri;
    private UndoContainer<File> undoBitmaps;

    private boolean buttonsEnabled = true;

    private static int defaultMaxUndos;

    private MainActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainActivityPresenter(this);

        defaultMaxUndos = Integer.parseInt(getResources().getString(R.string.default_undo_count));
        undoBitmaps = new UndoContainer<>(defaultMaxUndos);

        initializeViews();
        initializeImage();
    }

    private void initializeImage() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                presenter.setBitmapFromUri(uri);
            } catch (IOException e) {}
        }
    }

    private void initializeViews() {
        setContentView(R.layout.activity_main);
        setLayoutViews();
        initializeListeners();
    }

    private void setLayoutViews() {
        imageView = (ImageView) findViewById(R.id.image_view);
        gestureView = (GestureOverlayView) findViewById(R.id.gestures);
    }

    private void initializeListeners() {
        initializeTouchListeners();
    }

    private void initializeTouchListeners() {
        MyGestureListener gestureListener = new MyGestureListener(this);
        gestureListener.setLongPressCallback(initializeLongPressCallback());
        gestureListener.setSwirlCallback(initializeSwirlCallback());
        gestureListener.setRippleCallback(initializeRippleCallback());
        gestureDetector = new GestureDetectorCompat(this, gestureListener);
        imageView.setOnTouchListener(new ImageTouchListener());
        gestureView.addOnGesturePerformedListener(gestureListener);
    }

    private Callback initializeLongPressCallback() {
        return new UndoableFileCallback(undoBitmaps, this) {
            @Override
            public void call() {
                setImage(image);
                super.call();
                setBusy();
                presenter.bubbleImage(image);
            }
        };
    }

    private Callback initializeSwirlCallback() {
        return new UndoableFileCallback(undoBitmaps, this) {
            @Override
            public void call() {
                setImage(image);
                super.call();
                setBusy();
                presenter.swirlImage(image);
            }
        };
    }

    private Callback initializeRippleCallback() {
        return new UndoableFileCallback(undoBitmaps, this) {
            @Override
            public void call() {
                setImage(image);
                super.call();
                setBusy();
                presenter.rippleImage(image);
            }
        };
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!buttonsEnabled) {
            for (int i=1; i<menu.size(); i++)
                menu.getItem(i).setEnabled(false);
        } else {
            for (int i=1; i<menu.size(); i++)
                menu.getItem(i).setEnabled(true);
        }
        return true;
    }

    private void setBusy() {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        buttonsEnabled = false;
    }

    private void setNotBusy() {
        findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
        buttonsEnabled = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView.setImageBitmap(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
            return startSettingsActivity();
        else if (id == R.id.action_load)
            return loadFromGallery();
        else if (id == R.id.action_camera)
            return startCamera();
        else if (id == R.id.action_save) {
            presenter.saveImage(image);
            return true;
        }
        else if (id == R.id.action_discard)
            return startDiscardImagePrompt();
        else if (id == R.id.action_undo)
            return undo();

        return super.onOptionsItemSelected(item);
    }

    private boolean startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }

    private boolean loadFromGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
        return true;
    }


    private boolean startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile;
        try {
            imageFile = createImageFile();
        } catch (IOException e) {
            imageFile = null;
        }

        imageUri = Uri.fromFile(imageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);

        return true;
    }

    private boolean startDiscardImagePrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_confirm_prompt);
        builder.setPositiveButton(R.string.discard_yes, new PositiveClickListener());
        builder.setNegativeButton(R.string.discard_no, new NegativeClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    private boolean undo() {
        try {
            presenter.setBitmapFromUri(Uri.fromFile(undoBitmaps.pop()));
        } catch (IOException e) {
            toast("Error undoing");
            return false;
        }
        catch (NoMoreUndosException e) {
            toast("No more undos available");
            return false;
        }
        return true;
    }

    private File createImageFile() throws IOException {
        String imageFileName = FileUtils.createFileName();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            presenter.setBitmapFromPath(imageUri.getPath());
            galleryAddPic(imageUri);
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            presenter.setBitmapFromPath(picturePath);

        }
    }

    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.undo_count_key))) {
            undoBitmaps.setMaxSize(sharedPreferences.getInt(key, defaultMaxUndos));
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data.getClass() == Bitmap.class) {
            updateImage((Bitmap) data);
            setNotBusy();
        }
        else if (data.getClass() == String.class) {
            toast((String) data);
        }
    }

    private void updateImage(Bitmap newImage) {
        image = newImage;
        imageView.setImageBitmap(image);
    }

    private void toast(String text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private class ImageTouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    }

    private class PositiveClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            updateImage(null);
        }
    }

    private class NegativeClickListener implements DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int id) {}
    }

}
