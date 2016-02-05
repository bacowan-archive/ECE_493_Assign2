package com.example.brendan.assignment2.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.brendan.assignment2.R;
import com.example.brendan.assignment2.model.FileUtils;
import com.example.brendan.assignment2.presenter.MainActivityPresenter;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private static int CAPTURE_IMAGE_REQUEST = 1;
    private static int RESULT_LOAD_IMAGE = 2;

    private ImageView imageView;
    private Bitmap image;
    private GestureOverlayView gestureView;

    private Uri imageUri;
    private GestureDetectorCompat gestureDetector;

    private MainActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainActivityPresenter(this);

        initializeViews();
        initializeImage();
    }

    private void initializeImage() {
        Uri uri = getIntent().getData();
        //TODO: Clean this
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
        return new Callback() {
            @Override
            public void call() {
                presenter.bubbleImage(image);
            }
        };
    }

    private Callback initializeSwirlCallback() {
        return new Callback() {
            @Override
            public void call() {
                presenter.swirlImage(image);
            }
        };
    }

    private Callback initializeRippleCallback() {
        return new Callback() {
            @Override
            public void call() {
                presenter.rippleImage(image);
            }
        };
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

        if (id == R.id.action_load)
            return loadFromGallery();
        else if (id == R.id.action_camera)
            return startCamera();
        else if (id == R.id.action_save) {
            presenter.saveImage(image);
            return true;
        }
        else if (id == R.id.action_discard)
            return startDiscardImagePrompt();

        return super.onOptionsItemSelected(item);
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
        //DialogFragment dialog = new DiscardConfirmDialogFragment();
        //dialog.show(getSupportFragmentManager(), "discard");
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
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
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

    private void enableButtons() {
        //loadButton.setEnabled(true);
        //cameraButton.setEnabled(true);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data.getClass() == Bitmap.class) {
            image = (Bitmap) data;
            imageView.setImageBitmap(image);
            enableButtons();
        }
        else if (data.getClass() == String.class) {
            toast((String) data);
        }
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

    private class DiscardConfirmDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.discard_confirm_prompt);
            builder.setPositiveButton(R.string.discard_yes, new PositiveClickListener());
            builder.setNegativeButton(R.string.discard_no, new NegativeClickListener());
            return builder.create();
        }

        private class PositiveClickListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int id) {
                discardImage();
            }
        }

        private class NegativeClickListener implements DialogInterface.OnClickListener{
            public void onClick(DialogInterface dialog, int id) {}
        }

        private void discardImage() {

        }
    }


}
