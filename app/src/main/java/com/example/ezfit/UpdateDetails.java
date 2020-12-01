package com.example.ezfit;

import android.content.Intent;
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

        // Code to control what happens when save button is clicked
        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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
}
