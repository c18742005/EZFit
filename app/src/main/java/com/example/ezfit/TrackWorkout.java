/*
    Class to control the actions of the TrackWorkout activity.
    Holds the methods to control what happens on creation of the activity and to clear the text in
    the edit text fields.
    Class has button click listeners to control what happens when a user adds an exercise to the
    workout and saves the workout.
    Class also has MyCustomAdapter class which is a subclass of ArrayAdapter that allows list view
    to be populated with data from an array list.
 */
package com.example.ezfit;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class TrackWorkout extends ListActivity {
    // Array lists to hold the details of each exercise
    ArrayList<String> exerciseNames = new ArrayList<>();
    ArrayList<String> exerciseSets = new ArrayList<>();
    ArrayList<String> exerciseReps = new ArrayList<>();
    ArrayList<String> exerciseWeights = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_workout);

        // Code to instantiate my custom adapter that will display list of exercises to screen
        final MyCustomAdapter myAdapter = new MyCustomAdapter(this, R.layout.row, exerciseNames);
        setListAdapter(myAdapter);

        // Code to control what happens when the add exercise button is clicked
        Button addButton = (Button) findViewById(R.id.add_exercise);

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Add the details from the edit texts to the corresponding array list
                        EditText name = (EditText) findViewById(R.id.exercise_name);
                        exerciseNames.add(name.getText().toString());

                        EditText sets = (EditText) findViewById(R.id.sets_completed);
                        exerciseSets.add(sets.getText().toString());

                        EditText reps = (EditText) findViewById(R.id.reps_completed);
                        exerciseReps.add(reps.getText().toString());

                        EditText weight = (EditText) findViewById(R.id.weight_lifted);
                        exerciseWeights.add(weight.getText().toString());

                        // Clear the edit texts
                        clearText(name, sets, reps, weight);

                        // Notify the adapter that the data has changed
                        myAdapter.notifyDataSetChanged();
                    }
                }
        );

        // Code to control what happens when the save workout button is clicked
        Button saveButton = (Button) findViewById(R.id.save_workout);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Intent to move to TrakWorkout activity
                        Intent switchScreens = new Intent(TrackWorkout.this, SaveWorkout.class);

                        // Add the array lists to the extras to be sent to the new activity
                        switchScreens.putExtra("names", exerciseNames);
                        switchScreens.putExtra("sets", exerciseSets);
                        switchScreens.putExtra("reps", exerciseReps);
                        switchScreens.putExtra("weights", exerciseWeights);

                        // Start the new activity
                        startActivity(switchScreens);
                    }
                }
        );

        // Code to control what happens when the return button is clicked
        Button returnButton = (Button) findViewById(R.id.goBack);

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Finish the activty
                        finish();
                    }
                }
        );
    }

    // Class for my modified adapter
    public class MyCustomAdapter extends ArrayAdapter<String> {
        // Class Constructor
        public MyCustomAdapter(Context context, int rowLayoutId, ArrayList<String> myArrayData) {
            super(context, rowLayoutId, myArrayData);
        }

        // Method to get a View that displays data at a specified position in the data set.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            // If row is not null then set each row to show exercise details
            if(row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row, parent, false);

                // Set the Text view fields to the corresponding data
                TextView name = (TextView) row.findViewById(R.id.exerciseName);
                name.setText(exerciseNames.get(position));

                TextView sets = (TextView) row.findViewById(R.id.exerciseSets);
                sets.setText(exerciseSets.get(position));

                TextView reps = (TextView) row.findViewById(R.id.exerciseReps);
                reps.setText(exerciseReps.get(position));

                TextView weight = (TextView) row.findViewById(R.id.exerciseWeight);
                weight.setText(exerciseWeights.get(position));
            }

            return row;
        }
    }

    // Method to clear text from the fields specified in the arguments
    public void clearText(EditText name, EditText sets, EditText reps, EditText weight) {
        name.setText("");
        sets.setText("");
        reps.setText("");
        weight.setText("");
    }
}