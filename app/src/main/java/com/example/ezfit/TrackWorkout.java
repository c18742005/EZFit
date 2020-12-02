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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TrackWorkout extends ListActivity {
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
                        EditText name = (EditText) findViewById(R.id.exercise_name);
                        exerciseNames.add(name.getText().toString());

                        EditText sets = (EditText) findViewById(R.id.sets_completed);
                        exerciseSets.add(sets.getText().toString());

                        EditText reps = (EditText) findViewById(R.id.reps_completed);
                        exerciseReps.add(reps.getText().toString());

                        EditText weight = (EditText) findViewById(R.id.weight_lifted);
                        exerciseWeights.add(weight.getText().toString());

                        clearText(name, sets, reps, weight);

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
                        Intent switchScreens = new Intent(TrackWorkout.this, SaveWorkout.class);
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
                        finish();
                    }
                }
        );
    }

    public class MyCustomAdapter extends ArrayAdapter<String> {
        // Constructor
        public MyCustomAdapter(Context context, int rowLayoutId, ArrayList<String> myArrayData) {
            super(context, rowLayoutId, myArrayData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if(row == null) {

                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row, parent, false);

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

    public void clearText(EditText name, EditText sets, EditText reps, EditText weight) {
        name.setText("");
        sets.setText("");
        reps.setText("");
        weight.setText("");
    }
}