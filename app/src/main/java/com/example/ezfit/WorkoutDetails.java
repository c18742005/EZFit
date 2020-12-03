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

public class WorkoutDetails extends ListActivity {
    private DatabaseManager dbManager;
    private ClientCursorAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_details);

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int workoutID = (int) getIntent().getExtras().getLong("rowID");

        myAdapter = new ClientCursorAdapter(this, R.layout.workout_row, dbManager.getExercisesInWorkout(workoutID), 0);
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
