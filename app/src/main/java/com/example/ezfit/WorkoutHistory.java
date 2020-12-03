package com.example.ezfit;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import java.sql.SQLException;

public class WorkoutHistory extends ListActivity {
    private DatabaseManager dbManager;

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

        ClientCursorAdapter myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getWorkoutHistory("workout"), 0);
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
            TextView workoutName = (TextView) view.findViewById(R.id.workoutName);
            workoutName.setText(cursor.getString(cursor.getColumnIndex("workout_name")));

            TextView workoutDate = (TextView) view.findViewById(R.id.workoutDate);
            workoutDate.setText(cursor.getString(cursor.getColumnIndex("workout_date")));

            TextView workoutDuration = (TextView) view.findViewById(R.id.workoutDuration);
            workoutDuration.setText(cursor.getString(cursor.getColumnIndex("workout_duration")));

            TextView bodyParts = (TextView) view.findViewById(R.id.bodypartsTrained);
            bodyParts.setText(cursor.getString(cursor.getColumnIndex("bodyparts")));
        }
    }
}
