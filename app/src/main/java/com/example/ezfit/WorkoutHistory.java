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

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getWorkoutHistory("workout"), 0);
        setListAdapter(myAdapter);

        dbManager.close();

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

    public class ClientCursorAdapter extends ResourceCursorAdapter {

        public ClientCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
            super(context, layout, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
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
        Cursor data = (Cursor) myAdapter.getItem(position);

        Intent switchScreens = new Intent(WorkoutHistory.this, WorkoutDetails.class);
        switchScreens.putExtra("rowID", id);

        startActivity(switchScreens);
    }

    protected void onResume() {
        super.onResume();

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getWorkoutHistory("workout"), 0);
        setListAdapter(myAdapter);

        dbManager.close();
    }
}
