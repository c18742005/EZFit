/*
    Class to control the actions of the UpdateDetails activity.
    Holds the methods to control what happens on creation of the activity and to set the text in the
    text views with data pointed to by a cursor.
    Class has a button click listener to control what happens when a user saves their details.
    Class has a DB manager connection allowing it to make calls on the database.
 */
package com.example.ezfit;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.SQLException;

public class UpdateDetails extends AppCompatActivity {

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_details);

        // Create a connection to the DB manager
        dbManager = new DatabaseManager(this);

        // Try to open the database
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Get the user details from the database
        Cursor cursor = dbManager.getUserDetails();

        dbManager.close();

        // Set the attributes using the cursor data
        setAttributes(cursor);

        // Code to control what happens when save button is clicked
        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Try to open the database connection
                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // Retrieve data from the edit text fields
                        EditText name = (EditText) findViewById(R.id.getName);
                        EditText age = (EditText) findViewById(R.id.getAge);
                        EditText gender = (EditText) findViewById(R.id.getGender);
                        EditText weight = (EditText) findViewById(R.id.getWeight);
                        EditText height = (EditText) findViewById(R.id.getHeight);
                        EditText bmi = (EditText) findViewById(R.id.getBmi);
                        EditText bodyFat = (EditText) findViewById(R.id.getBodyFat);

                        // Update the users details using the retrieved data
                        dbManager.updateUser(name.getText().toString(), Integer.parseInt(age.getText().toString()), Float.parseFloat(weight.getText().toString()),
                                gender.getText().toString(), Float.parseFloat(height.getText().toString()), Float.parseFloat(bodyFat.getText().toString()),
                                Float.parseFloat(bmi.getText().toString()));

                        // Close the database connection
                        dbManager.close();

                        Toast.makeText(UpdateDetails.this, "Details Updated Successfully", Toast.LENGTH_LONG).show();

                        // Finish the activity
                        finish();
                    }
                }
        );

        // Code to control what happens when the return button is clicked
        Button returnButton = (Button) findViewById(R.id.goBack);

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Finish the activity
                        finish();
                    }
                }
        );
    }

    // Method to set and display the user details
    public void setAttributes(Cursor cursor) {
        EditText name = (EditText) findViewById(R.id.getName);
        name.setText(cursor.getString(cursor.getColumnIndex("user_name")));
        EditText age = (EditText) findViewById(R.id.getAge);
        age.setText(cursor.getString(cursor.getColumnIndex("user_age")));
        EditText gender = (EditText) findViewById(R.id.getGender);
        gender.setText(cursor.getString(cursor.getColumnIndex("user_gender")));
        EditText weight = (EditText) findViewById(R.id.getWeight);
        weight.setText(cursor.getString(cursor.getColumnIndex("user_weight")));
        EditText height = (EditText) findViewById(R.id.getHeight);
        height.setText(cursor.getString(cursor.getColumnIndex("user_height")));
        EditText bmi = (EditText) findViewById(R.id.getBmi);
        bmi.setText(cursor.getString(cursor.getColumnIndex("user_bmi")));
        EditText bodyFat = (EditText) findViewById(R.id.getBodyFat);
        bodyFat.setText(cursor.getString(cursor.getColumnIndex("user_bodyfat")));
    }
}