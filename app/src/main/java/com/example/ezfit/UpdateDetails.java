package com.example.ezfit;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class UpdateDetails extends AppCompatActivity {

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_details);

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = dbManager.getUserDetails();

        dbManager.close();

        setAttributes(cursor);

        // Code to control what happens when save button is clicked
        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbManager = new DatabaseManager(UpdateDetails.this);

                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        EditText name = (EditText) findViewById(R.id.getName);
                        EditText age = (EditText) findViewById(R.id.getAge);
                        EditText gender = (EditText) findViewById(R.id.getGender);
                        EditText weight = (EditText) findViewById(R.id.getWeight);
                        EditText height = (EditText) findViewById(R.id.getHeight);
                        EditText bmi = (EditText) findViewById(R.id.getBmi);
                        EditText bodyFat = (EditText) findViewById(R.id.getBodyFat);

                        dbManager.updateUser(name.getText().toString(), Integer.parseInt(age.getText().toString()), Float.parseFloat(weight.getText().toString()),
                                gender.getText().toString(), Float.parseFloat(height.getText().toString()), Float.parseFloat(bodyFat.getText().toString()),
                                Float.parseFloat(bmi.getText().toString()));

                        dbManager.close();

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
                        finish();
                    }
                }
        );
    }

    // Method to show the user their details
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
