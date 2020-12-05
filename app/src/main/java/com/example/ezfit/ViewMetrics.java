/*
    Class to control the actions of the ViewMetrics activity.
    Holds the methods to control what happens on creation and resumption of the activity.
    Class has a DB manager connection allowing it to make calls on the database. The class uses the
    DB manager to populate average time and workout/run counts.
    The class has button click listeners to view workout and run history.
 */
package com.example.ezfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.SQLException;

public class ViewMetrics extends AppCompatActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_metrics);

        // Get connection to database manager
        dbManager = new DatabaseManager(this);

        // Try to open the database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retrieve metrics from the database and set them to their respective text views
        TextView numWorkouts = (TextView) findViewById(R.id.num_workouts);
        numWorkouts.setText(Integer.toString(dbManager.countWorkouts("workout")));

        TextView numRuns = (TextView) findViewById(R.id.num_runs);
        numRuns.setText(Integer.toString(dbManager.countWorkouts("run")));

        TextView avgWorkoutTime = (TextView) findViewById(R.id.avg_workout_time);
        avgWorkoutTime.setText(Integer.toString(dbManager.getAverageSessionTime("workout")) + " minutes");

        TextView avgRunTime = (TextView) findViewById(R.id.avg_run_time);
        avgRunTime.setText(Integer.toString(dbManager.getAverageSessionTime("run")) + " minutes");

        dbManager.close();

        // Code to control what happens when the workout history button is clicked
        Button workoutButton = (Button) findViewById(R.id.workoutHistory);

        workoutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create intent and switch to WorkoutHistory activity
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
                        // Create intent and switch to RunHistory activity
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
                        // Finish the activity
                        finish();
                    }
                }
        );
    }

    // Method to control what happens when the activity is resumed
    protected void onResume() {
        super.onResume();

        // Try open database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Retireve metrics from the database and set them to their respective text views
        TextView numWorkouts = (TextView) findViewById(R.id.num_workouts);
        numWorkouts.setText(Integer.toString(dbManager.countWorkouts("workout")));

        TextView numRuns = (TextView) findViewById(R.id.num_runs);
        numRuns.setText(Integer.toString(dbManager.countWorkouts("run")));

        TextView avgWorkoutTime = (TextView) findViewById(R.id.avg_workout_time);
        avgWorkoutTime.setText(Integer.toString(dbManager.getAverageSessionTime("workout")) + " minutes");

        TextView avgRunTime = (TextView) findViewById(R.id.avg_run_time);
        avgRunTime.setText(Integer.toString(dbManager.getAverageSessionTime("run")) + " minutes");

        dbManager.close();
    }
}