/*
    Class to control the actions of the TrackRun activity.
    Holds the methods to control what happens on creation, resumption and pausing of the activity.
    Class has a DB manager connection allowing it to make calls on the database and a button click
    listener to save the run details to the database.
    Class also holds the code for the use of the stopwatch timer and the gps tracker.
 */
package com.example.ezfit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.sql.SQLException;

public class TrackRun extends AppCompatActivity implements LocationListener {
    private DatabaseManager dbManager;

    // Variables needed for the stopwatch
    private int seconds = 0;

    // Variables needed for the stopwatch and the gps tracking
    private boolean isRunning;
    private boolean wasRunning;

    // Variables needed for the gps tracker
    private LocationManager locationManager;
    private static final int MY_PERMISSION_GPS = 1;
    private double distance = 0.0;
    long minTime = 3000;
    float minDistance = 10;
    private TextView distanceTravelled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_run);

        distanceTravelled = (TextView) findViewById(R.id.distance);
        runTimer(); // set up the stopwatch
        setUpLocation(); // Start tracking the location of the device

        // Code to control what happens when the start button is clicked
        Button startButton = (Button) findViewById(R.id.start_button);

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start running the stopwatch and tracking location
                        isRunning = true;
                    }
                }
        );

        // Code to control what happens when the save run button is clicked
        Button saveButton = (Button) findViewById(R.id.save_run);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // If run was being recorded then save the run
                        if(seconds > 0) {
                            // Get the duration of the run in minutes and the average speed
                            int duration = seconds / 60;

                            // If duration is 0 minutes then increment to 1
                            if (duration == 0) {
                                duration = 1;
                            }

                            // Get the average speed of the run
                            float avgSpeed = (float) distance / duration;

                            // Get a connection to the DB manager
                            dbManager = new DatabaseManager(TrackRun.this);

                            try {
                                dbManager.open();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            // Add the run to the database
                            int id = dbManager.addWorkout("run", duration, "Run", "Cardiovascular");

                            // Add run details to the database
                            dbManager.addExercise(id, "run", avgSpeed, (float) distance, 0,0,0, duration);

                            dbManager.close();

                            Toast.makeText(getBaseContext(), "Run Saved Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            // else start button was never clicked so no run recorded. Do not save the run
                            Toast.makeText(TrackRun.this, "No run was completed. You must click start before saving your run.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        // Code to control what happens when the stop button is clicked
        Button stopButton = (Button) findViewById(R.id.stop_button);

        stopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Stop the stopwatch
                        wasRunning = isRunning;
                        isRunning = false;
                    }
                }
        );

        // Code to control what happens when the return button is clicked
        Button returnButton = (Button) findViewById(R.id.goBack);

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // switch off location updates
                        locationManager.removeUpdates(TrackRun.this);

                        // Finish the activity
                        finish();
                    }
                }
        );
    }

    // Method to control what happens when activity is paused
    protected void onPause() {
        super.onPause();

        // switch off location updates
        locationManager.removeUpdates(this);

        // Stop the stopwatch
        wasRunning = isRunning;
        isRunning = false;
    }

    // Method to control what happens when activity is resumed
    protected void onResume() {
        super.onResume();

        setUpLocation();

        if(wasRunning) {
            isRunning = true;
        }
    }

    // Method to control the stopwatch
    private void runTimer() {
        final TextView time = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                // Convert seconds to hours, minutes and seconds
                int hours = seconds / 3600;
                int mins = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Format the time to a string
                String formatTime = String.format("%02d:%02d:%02d", hours, mins, secs);

                // Set the text view to show the formatted time
                time.setText(formatTime);

                // If stopwatch is running then increment seconds
                if(isRunning) {
                    seconds++;
                }

                // Delay the incrementation of the seconds by 1000 milliseconds
                handler.postDelayed(this, 1000);
            }
        });
    }

    // Method to set up teh location tracking service
    private void setUpLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if activity has the permission to access the gps tracking function of the phone
        // If not then request the permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TrackRun.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_GPS);
        }
        else { // permission granted so start tracking the user
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }
    }

    // Method to control what happens when the location changes
    public void onLocationChanged(Location location) {
        // If location is not empty and user wants their distance tracked then
        // Increment distance by 10 meters ech time a change in location is detected
        if (location != null && isRunning) {
            distance += 0.01;

            // Format the distance to a string and set it to the text view
            String formatDistance = String.format("%.2f Km", distance);
            distanceTravelled.setText(formatDistance);
        }
    }

    public void onProviderDisabled(String provider) {}
    public void onProviderEnabled(String provider) {}
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    // Callback method associated with the user having entered in their permissions
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissions granted
                    Toast.makeText(this, "permissions granted.", Toast.LENGTH_LONG).show();
                } else {
                    // Tell user they need to allow permissions
                    Toast.makeText(this, "You must switch on location permissions.", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}
