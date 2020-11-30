package com.example.ezfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Code to control what happens when the personal details button is clicked
        Button detailsButton = (Button) findViewById(R.id.details);

        detailsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(MainActivity.this, PersonalDetails.class);
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the track workout button is clicked
        Button workoutButton = (Button) findViewById(R.id.trackWorkout);

        workoutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(MainActivity.this, TrackWorkout.class);
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the track run button is clicked
        Button runButton = (Button) findViewById(R.id.trackRun);

        runButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(MainActivity.this, TrackRun.class);
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the track run button is clicked
        Button metricsButton = (Button) findViewById(R.id.viewMetrics);

        metricsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(MainActivity.this, ViewMetrics.class);
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the track run button is clicked
        Button picturesButton = (Button) findViewById(R.id.progressPictures);

        picturesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(MainActivity.this, ProgressPictures.class);
                        startActivity(switchScreens);
                    }
                }
        );
    }
}