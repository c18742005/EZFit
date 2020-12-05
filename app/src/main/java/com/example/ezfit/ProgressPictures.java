/*
    Class to control the actions of the ProgressPictures activity.
    Holds the methods to control what happens on creation of the activity, create unique file names
    for the images, ensure user has the correct permissions, saving an image to the devices internal
    storage and what happens when a user takes a picture.
    Class has a DB manager connection allowing it to make calls on the database and button click
    listeners to save pictures to the database and view old progress pictures.
 */
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
                        // Check if app has permission to access the camera. If it does then open the camera
                        if(ContextCompat.checkSelfPermission(ProgressPictures.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // Intent to start the camera
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        } else {
                            // App does not have permission so request it
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

    // Callback method associated with the user having entered in their permissions
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

    // Callback method associated with the user having taken a picture with the camera
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

    // Reference: The following code is from https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    // Code is not copied directly and has been modified to suit my own needs
    // Method to save an image to the phones internal storage
    private void saveToInternalStorage(Bitmap image) {
        String name = generateImageName();
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // path to /data/data/com.example.ezfit/app_images/
        File directory = wrapper.getDir("images", Context.MODE_PRIVATE);

        // Create path to the image
        File myPath = new File(directory, name);

        // Create outputstream to write data to the file
        FileOutputStream outputStream = null;

        // Try write the file to the output stream
        try {
            outputStream = new FileOutputStream(myPath);
            // Use the compress method on the image to write it to the OutputStream
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the output stream
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Reference complete

    // Method to create a unique file name for each image
    private String generateImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "jpg_" + timeStamp + "_.jpg";

        // Create connection to database manager
        DatabaseManager dbManager = new DatabaseManager(this);

        // Open the database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Save the image name to the database
        dbManager.addImage(imageName);

        // Close database connection
        dbManager.close();

        // Return the image name
        return imageName;
    }
}
