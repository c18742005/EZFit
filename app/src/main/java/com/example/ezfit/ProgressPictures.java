package com.example.ezfit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgressPictures extends AppCompatActivity {
    private DatabaseManager dbManager;
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    ImageView image;
    Bitmap picture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_pictures);

        // Code to control what happens when the capture button is clicked
        Button captureButton = (Button) findViewById(R.id.takePhoto);

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ContextCompat.checkSelfPermission(ProgressPictures.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Intent to start the camera
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        } else {
                            ActivityCompat.requestPermissions(ProgressPictures.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    }
                }
        );

        // Code to control what happens when the view progress pictures button is clicked
        Button loadImagesButton = (Button) findViewById(R.id.viewPhotos);

        loadImagesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Move to the ViewMetrics activity
                        Intent switchScreens = new Intent(ProgressPictures.this, ViewPictures.class);
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the save button is clicked
        Button saveButton = (Button) findViewById(R.id.savePhoto);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // If the picture variable is not empty then save the picture to internal storage
                        if (picture != null) {
                            saveToInternalStorage(picture);
                            Toast.makeText(ProgressPictures.this, "Picture Saved Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            // No picture was taken so inform user they must take a picture first
                            Toast.makeText(ProgressPictures.this, "There is no image to be saved. Please take a photo first", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        // Code to control what happens when the return button is clicked
        Button returnButton = (Button) findViewById(R.id.goBack);

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Finish activity
                        finish();
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Permissions are accepted so start the camera app
        if(requestCode == CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Intent to start the camera app
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            // Permissions not accepted, tell user they have to accept permissions
            Toast.makeText(ProgressPictures.this, "Permissions for camera must be accepted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        image = (ImageView) findViewById(R.id.imageView);

        // if picture was successfully taken by app
        if(resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            // Get the bitmap from data extras and save to picture variable
            picture = (Bitmap) data.getExtras().get("data");
            // Set the image view to display the taken image
            image.setImageBitmap(picture);
        }
    }

    // Method to save an image to the phones internal storage
    private String saveToInternalStorage(Bitmap image){
        String name = generateImageName();
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // path to /data/data/com.example.ezfit/app_images/
        File directory = wrapper.getDir("images", Context.MODE_PRIVATE);

        // Create images directory
        File myPath = new File(directory, name);

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return directory.getAbsolutePath();
    }

    // Method to load image from internal storage
    private void loadImageFromStorage(String path, String imageName)
    {
        try {
            File file = new File(path, imageName);
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(file));
            ImageView thumbNail = (ImageView) findViewById(R.id.imageView);
            thumbNail.setImageBitmap(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to create a file name for each image
    private String generateImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_.jpg";

        // Create connection to database manager
        dbManager = new DatabaseManager(this);

        // Open the database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbManager.addImage(imageName);

        dbManager.close();

        return imageName;
    }
}
