/*
    Class to control the actions of the WorkoutHistory activity.
    Holds the methods to control what happens on creation and resumption of the activity and what
    happens when a list item is clicked.
    Class has a DB manager connection allowing it to make calls on the database.
    The class has an inner class ClientCursorAdapter that is a subclass of ResourceCursorAdapter.
    This inner class allows the population of the list view with data pointed to by a cursor.
 */
package com.example.ezfit;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import java.sql.SQLException;

public class WorkoutHistory extends ListActivity {
    private DatabaseManager dbManager;
    private ClientCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_history);

        // Get a connection to the database manager
        dbManager = new DatabaseManager(this);

        // Try open database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create and Bind adapter to the list view
        myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getWorkoutHistory("workout"), 0);
        setListAdapter(myAdapter);

        dbManager.close();

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

    // Client cursor adapter class to display workout details with the list view
    public class ClientCursorAdapter extends ResourceCursorAdapter {

        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        // Method to bind view to the data pointed to by the cursor
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Set data to its corresponding text view
            TextView workoutName = (TextView) view.findViewById(R.id.col1);
            workoutName.setText(cursor.getString(cursor.getColumnIndex("workout_name")));

            TextView workoutDate = (TextView) view.findViewById(R.id.col2);
            workoutDate.setText(cursor.getString(cursor.getColumnIndex("workout_date")));

            TextView workoutDuration = (TextView) view.findViewById(R.id.col3);
            workoutDuration.setText(cursor.getString(cursor.getColumnIndex("workout_duration")));

            TextView bodyParts = (TextView) view.findViewById(R.id.col4);
            bodyParts.setText(cursor.getString(cursor.getColumnIndex("bodyparts")));
        }
    }

    // when a list item is clicked open a new activity and display the workout details
    public void onListItemClick(ListView parent, View v, int position, long id) {
        // Create intent to switch to new activity and add row id to extras
        Intent switchScreens = new Intent(WorkoutHistory.this, WorkoutDetails.class);
        switchScreens.putExtra("rowID", id);

        // Start the new activity
        startActivity(switchScreens);
    }

    // Method to control what happens when the activity is resumed
    protected void onResume() {
        super.onResume();

        // Try to open a connection to the database
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create and Bind adapter to the list view
        myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getWorkoutHistory("workout"), 0);
        setListAdapter(myAdapter);

        dbManager.close();
    }
}