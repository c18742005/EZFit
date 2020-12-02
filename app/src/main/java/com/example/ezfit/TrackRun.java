package com.example.ezfit;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TrackRun extends AppCompatActivity {
    private int seconds = 0;
    private boolean isRunning;
    private boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_run);

        runTimer();

        // Code to control what happens when the start button is clicked
        Button startButton = (Button) findViewById(R.id.start_button);

        startButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isRunning = true;
                    }
                }
        );

        // Code to control what happens when the stop button is clicked
        Button stopButton = (Button) findViewById(R.id.stop_button);

        stopButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                        finish();
                    }
                }
        );
    }

    protected void onPause() {
        super.onPause();

        wasRunning = isRunning;
        isRunning = false;
    }

    protected void onResume() {
        super.onResume();

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
}
