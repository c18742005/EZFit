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
    private ClientCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_details);

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final int runID = (int) getIntent().getExtras().getLong("rowID");

        myAdapter = new ClientCursorAdapter(this, R.layout.run_row, dbManager.getExercisesInWorkout(runID), 0);
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

                        dbManager.removeExerciseByWorkoutID(runID);
                        dbManager.deleteWorkout(runID);

                        dbManager.close();

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
            TextView avgSpeed = (TextView) view.findViewById(R.id.column1);
            avgSpeed.setText(cursor.getString(cursor.getColumnIndex("exercise_avgspeed")));

            TextView distance = (TextView) view.findViewById(R.id.column2);
            distance.setText(cursor.getString(cursor.getColumnIndex("exercise_distance")));

            TextView duration = (TextView) view.findViewById(R.id.column3);
            duration.setText(cursor.getString(cursor.getColumnIndex("exercise_duration")));
        }
    }
}
