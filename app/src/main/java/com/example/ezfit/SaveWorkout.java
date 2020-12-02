package com.example.ezfit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class SaveWorkout extends AppCompatActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_workout);

        // Code to control what happens when the save workout button is clicked
        Button saveButton = (Button) findViewById(R.id.save_workout_button);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) findViewById(R.id.workout_name);
                        String exerciseName = name.getText().toString();
                        EditText duration = (EditText) findViewById(R.id.workout_duration);
                        int exerciseDuration = Integer.parseInt(duration.getText().toString());
                        EditText trainingType = (EditText) findViewById(R.id.bodyparts_trained);
                        String bodyParts = trainingType.getText().toString();

                        dbManager = new DatabaseManager(SaveWorkout.this);

                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        int id = dbManager.addWorkout("workout", exerciseDuration, exerciseName, bodyParts);

                        ArrayList<String> names = getIntent().getExtras().getStringArrayList("names");
                        ArrayList<String> sets = getIntent().getExtras().getStringArrayList("sets");
                        ArrayList<String> reps = getIntent().getExtras().getStringArrayList("reps");
                        ArrayList<String> weights = getIntent().getExtras().getStringArrayList("weights");

                        for(int i = 0; i < names.size(); i++) {
                            dbManager.addExercise(id, names.get(i), 0.0f, 0.0f, Integer.parseInt(sets.get(i)), Integer.parseInt(reps.get(i)), Float.parseFloat(weights.get(i)), 0);
                        }

                        dbManager.close();

                        Toast.makeText(getBaseContext(), "Workout Saved Successfully", Toast.LENGTH_LONG).show();

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