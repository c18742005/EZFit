package com.example.ezfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ViewMetrics extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_metrics);

        // Code to control what happens when the workout history button is clicked
        Button workoutButton = (Button) findViewById(R.id.workoutHistory);

        workoutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(ViewMetrics.this, WorkoutHistory.class);
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the run history button is clicked
        Button runButton = (Button) findViewById(R.id.runHistory);

        runButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent switchScreens = new Intent(ViewMetrics.this, RunHistory.class);
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
}
