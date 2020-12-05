/*
    Class to control the actions of the RunDetails activity.
    Holds the method to control what happens on creation of the activity and an inner class,
    ClientCursorAdapter that's a subclass of ResourceCursorAdapter allowing details to be taken from
    data pointed to by cursor and set in text views.
    Class has a DB manager connection allowing it to make calls on the database.
 */
package com.example.ezfit;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class RunDetails extends ListActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_details);

        // Create connection to database manager
        dbManager = new DatabaseManager(this);

        // Open the database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // get the ID of the run from the intent extras
        final int runID = (int) getIntent().getExtras().getLong("rowID");

        // Create and set the cursor adapter
        ClientCursorAdapter myAdapter = new ClientCursorAdapter(this, R.layout.run_row, dbManager.getExercisesInWorkout(runID), 0);
        setListAdapter(myAdapter);

        dbManager.close();

        // Code to control what happens when the delete workout button is clicked
        Button deleteButton = (Button) findViewById(R.id.deleteRun);

        deleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // remove exercises in that run and remove the run from the database
                        dbManager.removeExerciseByWorkoutID(runID);
                        dbManager.deleteWorkout(runID);

                        dbManager.close();

                        // Tell user deletion was a success and finish the activity
                        Toast.makeText(RunDetails.this, "Run Deleted Successfully", Toast.LENGTH_LONG).show();
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
                        // Finish the activity
                        finish();
                    }
                }
        );
    }

    // Client cursor adapter class to display run details with the list view
    public class ClientCursorAdapter extends ResourceCursorAdapter {

        // Class constructor
        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        // Method to bind the view to the data pointed to by the cursor
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Set data to its corresponding text view
            TextView avgSpeed = (TextView) view.findViewById(R.id.column1);
            avgSpeed.setText(cursor.getString(cursor.getColumnIndex("exercise_avgspeed")));

            TextView distance = (TextView) view.findViewById(R.id.column2);
            distance.setText(cursor.getString(cursor.getColumnIndex("exercise_distance")));

            TextView duration = (TextView) view.findViewById(R.id.column3);
            duration.setText(cursor.getString(cursor.getColumnIndex("exercise_duration")));
        }
    }
}
