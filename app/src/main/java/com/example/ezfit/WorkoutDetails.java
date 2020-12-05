/*
    Class to control the actions of the WorkoutDetails activity.
    Holds the methods to control what happens on creation of the activity.
    Class has a DB manager connection allowing it to make calls on the database.
    The class has an inner class ClientCursorAdapter that is a subclass of ResourceCursorAdapter.
    This inner class allows the population of the list view with data pointed to by a cursor.
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

public class WorkoutDetails extends ListActivity {
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_details);

        // Create connection to database manager
        dbManager = new DatabaseManager(this);

        // Open the database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // get the ID of the workout from the intent extras
        final int workoutID = (int) getIntent().getExtras().getLong("rowID");

        // Create and set the cursor adapter
        ClientCursorAdapter myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getExercisesInWorkout(workoutID), 0);
        setListAdapter(myAdapter);

        dbManager.close();

        // Code to control what happens when the delete workout button is clicked
        Button deleteButton = (Button) findViewById(R.id.deleteWorkout);

        deleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        // remove exercises in that workout and remove the workout from the database
                        dbManager.removeExerciseByWorkoutID(workoutID);
                        dbManager.deleteWorkout(workoutID);

                        dbManager.close();

                        // Tell user deletion was a success and finish the activity
                        Toast.makeText(WorkoutDetails.this, "Workout Deleted Successfully", Toast.LENGTH_LONG).show();
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

        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        // Method to bind the view to the data pointed to by the cursor
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Set data to its corresponding text view
            TextView exerciseName = (TextView) view.findViewById(R.id.col1);
            exerciseName.setText(cursor.getString(cursor.getColumnIndex("exercise_name")));

            TextView sets = (TextView) view.findViewById(R.id.col2);
            sets.setText(cursor.getString(cursor.getColumnIndex("exercise_sets")));

            TextView reps = (TextView) view.findViewById(R.id.col3);
            reps.setText(cursor.getString(cursor.getColumnIndex("exercise_reps")));

            TextView weight = (TextView) view.findViewById(R.id.col4);
            weight.setText(cursor.getString(cursor.getColumnIndex("exercise_weight")));
        }
    }
}
