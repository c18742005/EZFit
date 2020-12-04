package com.example.ezfit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrackRun extends AppCompatActivity implements LocationListener {
    private DatabaseManager dbManager;

    private int seconds = 0;
    private boolean isRunning;
    private boolean wasRunning;

    private LocationManager locationManager;
    private long minTime = 3000;
    private float minDistance = 10;
    private static final int MY_PERMISSION_GPS = 1;

    private double distance = 0.0;
    private boolean distChanged = false;

    private TextView distanceTravelled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_run);

        distanceTravelled = (TextView) findViewById(R.id.distance);
        runTimer();

        // Code to control what happens when the start button is clicked
        Button startButton = (Button) findViewById(R.id.start_button);

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpLocation();
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
                        int duration = seconds / 60;
                        float avgSpeed = (float) distance / duration;


                        dbManager = new DatabaseManager(TrackRun.this);

                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        int id = dbManager.addWorkout("run", duration, "Run", "Cardiovascular");

                        dbManager.addExercise(id, "run", avgSpeed, (float) distance, 0,0,0, duration);

                        dbManager.close();

                        Toast.makeText(getBaseContext(), "Run Saved Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        );

        // Code to control what happens when the stop button is clicked
        Button stopButton = (Button) findViewById(R.id.stop_button);

        stopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // switch off location updates
                        locationManager.removeUpdates(TrackRun.this);

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

                        finish();
                    }
                }
        );
    }

    protected void onPause() {
        super.onPause();

        // switch off location updates: to be added
        locationManager.removeUpdates(this);
        wasRunning = isRunning;
        isRunning = false;
    }

    protected void onResume() {
        super.onResume();

        setUpLocation();
        if(wasRunning) {
            isRunning = true;
        }
    }

    private void runTimer() {
        final TextView time = (TextView) findViewById(R.id.timer);

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int mins = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String formatTime = String.format("%02d:%02d:%02d", hours, mins, secs);

                time.setText(formatTime);

                if(isRunning) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void setUpLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check if permission exists.. if not ask the user
        // Use the ContextCompat class' checkSelfPermission method to check
        // and use the ActivityCompat class' requestPermissions method to prompt for permission.. see notes
        // if....
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(TrackRun.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_GPS);
        }
        else { // permission granted

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            if(distChanged != false) {
                distance += 0.01;

                String formatDistance = String.format("%.2f Km", distance);
                distanceTravelled.setText(formatDistance);
            } else {
                distChanged = true;
            }
        }
    }

    public void onProviderDisabled(String provider) {}

    public void onProviderEnabled(String provider) {}

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    // this is a callback method associated with the user having entered in their permission -
    // the compiler will prompt you to add this call back method, when you putin the code for the permission check earlier.

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_GPS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                    Toast.makeText(this, "permissions granted.", Toast.LENGTH_LONG).show();
                } else {
                    // show a Toast message to say you need to switch on permissions
                    // to be added:
                    Toast.makeText(this, "You must switch on location permissions.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
