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

public class RunHistory extends ListActivity {
    private DatabaseManager dbManager;
    private ClientCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_history);

        // Get a connection to the database manager
        dbManager = new DatabaseManager(this);

        // Try open database connection
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Bind adapter to the list view
        myAdapter = new ClientCursorAdapter(this, R.layout.run_row, dbManager.getWorkoutHistory("run"), 0);
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

    // Client cursor adapter class to display run details with the list view
    public class ClientCursorAdapter extends ResourceCursorAdapter {

        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Set data to its corresponding text view
            TextView runName = (TextView) view.findViewById(R.id.column1);
            runName.setText(cursor.getString(cursor.getColumnIndex("workout_name")));

            TextView runDate = (TextView) view.findViewById(R.id.column2);
            runDate.setText(cursor.getString(cursor.getColumnIndex("workout_date")));

            TextView runDuration = (TextView) view.findViewById(R.id.column3);
            runDuration.setText(cursor.getString(cursor.getColumnIndex("workout_duration")));
        }
    }

    // when a list item is clicked open a new activity and display the run details
    public void onListItemClick(ListView parent, View v, int position, long id) {
        Cursor data = (Cursor) myAdapter.getItem(position);

        // Create the intent to the new activity and add row ID to extras
        Intent switchScreens = new Intent(RunHistory.this, RunDetails.class);
        switchScreens.putExtra("rowID", id);

        // Start the new activity
        startActivity(switchScreens);
    }

    // Method to control what happens when this activity is resumed
    protected void onResume() {
        super.onResume();

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update the details shown by the list view
        myAdapter = new ClientCursorAdapter(this, R.layout.run_row, dbManager.getWorkoutHistory("run"), 0);
        setListAdapter(myAdapter);

        dbManager.close();
    }
}
