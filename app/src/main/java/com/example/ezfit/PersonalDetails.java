package com.example.ezfit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;

public class PersonalDetails extends AppCompatActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details);

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = dbManager.getUserDetails();

        dbManager.close();

        setAttributes(cursor);

        // Code to control what happens when update details button is clicked
        Button updateButton = (Button) findViewById(R.id.updateDetails);

        updateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(PersonalDetails.this, UpdateDetails.class);
                        startActivity(switchScreens);
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

    protected void onResume() {
        super.onResume();

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = dbManager.getUserDetails();

        dbManager.close();

        setAttributes(cursor);
    }

    // Method to set show the user their details
    public void setAttributes(Cursor cursor) {
        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(cursor.getString(cursor.getColumnIndex("user_name")));
        TextView age = (TextView) findViewById(R.id.user_age);
        age.setText(cursor.getString(cursor.getColumnIndex("user_age")));
        TextView gender = (TextView) findViewById(R.id.user_gender);
        gender.setText(cursor.getString(cursor.getColumnIndex("user_gender")));
        TextView weight = (TextView) findViewById(R.id.user_weight);
        weight.setText(cursor.getString(cursor.getColumnIndex("user_weight")));
        TextView height = (TextView) findViewById(R.id.user_height);
        height.setText(cursor.getString(cursor.getColumnIndex("user_height")));
        TextView bmi = (TextView) findViewById(R.id.user_bmi);
        bmi.setText(cursor.getString(cursor.getColumnIndex("user_bmi")));
        TextView bodyFat = (TextView) findViewById(R.id.user_body_fat);
        bodyFat.setText(cursor.getString(cursor.getColumnIndex("user_bodyfat")));
    }
}
