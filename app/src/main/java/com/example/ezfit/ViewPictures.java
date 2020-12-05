/*
    Class to control the actions of the ViewPictures activity.
    Holds the methods to control what happens on creation of the activity and the method to load an
    image from the phones internal storage.
    Class has a DB manager connection allowing it to make calls on the database.
    The class has an inner class ClientCursorAdapter that is a subclass of ResourceCursorAdapter.
    This inner class allows the population of the list view with data pointed to by a cursor.
 */
package com.example.ezfit;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ViewPictures extends ListActivity {
    private DatabaseManager dbManager;
    private Bitmap image = null;
    private ClientCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pictures);

        // Create connection to database manager
        dbManager = new DatabaseManager(this);

        // Try open the database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create and Bind adapter to the list view
        myAdapter = new ClientCursorAdapter(this, R.layout.view_pictures_row, dbManager.getImages(), 0);
        setListAdapter(myAdapter);

        dbManager.close();

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

    // Client cursor adapter class to display workout details with the list view
    public class ClientCursorAdapter extends ResourceCursorAdapter {

        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        // Method to bind view to the data pointed to by the cursor
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Set data to its corresponding text view
            ImageView displayImage = (ImageView) view.findViewById(R.id.display_image);
            displayImage.setImageBitmap(loadImageFromStorage("/data/data/com.example.ezfit/app_images", cursor.getString(cursor.getColumnIndex("image_name"))));

            TextView imageDate = (TextView) view.findViewById(R.id.imageDate);
            imageDate.setText(cursor.getString(cursor.getColumnIndex("image_date")));
        }
    }

    // Reference: The following code is from https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    // The code has been heavily modified from the original to suit my own needs
    // Method to load image from internal storage
    private Bitmap loadImageFromStorage(String path, String imageName)
    {
        try {
            File file = new File(path, imageName);
            image = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return image;
    }
    // Reference complete
}