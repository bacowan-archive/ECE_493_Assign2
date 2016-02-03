package com.example.brendan.assignment2.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.brendan.assignment2.R;
import com.example.brendan.assignment2.presenter.MainActivityPresenter;
import com.example.brendan.assignment2.view.preferences.PreferenceParser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private static int CAPTURE_IMAGE_REQUEST = 1;
    private static int RESULT_LOAD_IMAGE = 2;
    private Button cameraButton;
    private Button loadButton;
    private Button tempButton;
    private ImageView imageView;
    private Bitmap image;
    private Uri imageUri;
    private PreferenceParser preferenceParser;

    private MainActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainActivityPresenter(this);
        preferenceParser = new PreferenceParser(this);

        setLayoutViews();
        requestImageLoadPermissions();
        initializeCameraButton();
        initializeLoadButton();

        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                presenter.setBitmapFromUri(uri, this);
            } catch (IOException e) {}
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView.setImageBitmap(image);
    }

    private void setLayoutViews() {
        cameraButton = (Button) findViewById(R.id.camera_button);
        loadButton = (Button) findViewById(R.id.load_button);
        imageView = (ImageView) findViewById(R.id.image_view);
        tempButton = (Button) findViewById(R.id.temp_button);
        tempButton.setOnClickListener(new TempButtonClickListener());
    }

    private void initializeCameraButton() {
        cameraButton.setOnClickListener(new CameraButtonClickListener());
    }

    private void initializeLoadButton() {
        loadButton.setOnClickListener(new LoadButtonClickListener());
    }

    private void requestImageLoadPermissions() {
        String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE"};
        int permsRequestCode = 100;
        ActivityCompat.requestPermissions(this, permissions, permsRequestCode);
    }

    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return startSettingsActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data.getClass() == Bitmap.class) {
            image = (Bitmap) data;
            imageView.setImageBitmap(image);
            enableButtons();
        }
    }

    private class CameraButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
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
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = SimpleDateFormat.getDateTimeInstance().format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private class LoadButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
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

    private boolean validateSettings() {
        if (image == null) {
            toast("No image loaded");
            return false;
        }
        else if (preferenceParser.getFilterType() == null) {
            toast("No filter type was selected");
            return false;
        }
        else if (!isOdd(preferenceParser.getFilterSize())) {
            toast("Filter size must be odd");
            return false;
        }
        else if (filterTooLarge(preferenceParser.getFilterSize())) {
            toast("Filter size must be smaller than image");
            return false;
        }
        else if (preferenceParser.getFilterSize() < 1) {
            toast("Filter size must be positive");
            return false;
        }
        return true;
    }

    private boolean isOdd(int val) {
        return val%2 == 1;
    }

    private boolean filterTooLarge(int val) {
        return val > image.getHeight() || val > image.getWidth();
    }

    private void toast(String text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private void disableButtons() {
        loadButton.setEnabled(false);
        cameraButton.setEnabled(false);
    }

    private void enableButtons() {
        loadButton.setEnabled(true);
        cameraButton.setEnabled(true);
    }

    private class TempButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {

        }
    }

}
