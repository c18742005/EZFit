package com.example.ezfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.ezfit.DatabaseHelper.KEY_BODY_PARTS;
import static com.example.ezfit.DatabaseHelper.KEY_ROWID;
import static com.example.ezfit.DatabaseHelper.KEY_USERNAME;
import static com.example.ezfit.DatabaseHelper.KEY_AGE;
import static com.example.ezfit.DatabaseHelper.KEY_WEIGHT;
import static com.example.ezfit.DatabaseHelper.KEY_GENDER;
import static com.example.ezfit.DatabaseHelper.KEY_HEIGHT;
import static com.example.ezfit.DatabaseHelper.KEY_BODY_FAT;
import static com.example.ezfit.DatabaseHelper.KEY_BMI;
import static com.example.ezfit.DatabaseHelper.KEY_TYPE;
import static com.example.ezfit.DatabaseHelper.KEY_WORKOUT_DURATION;
import static com.example.ezfit.DatabaseHelper.KEY_DATE;
import static com.example.ezfit.DatabaseHelper.KEY_WORKOUT_NAME;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_NAME;
import static com.example.ezfit.DatabaseHelper.KEY_AVG_SPEED;
import static com.example.ezfit.DatabaseHelper.KEY_DISTANCE;
import static com.example.ezfit.DatabaseHelper.KEY_SETS;
import static com.example.ezfit.DatabaseHelper.KEY_REPS;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_WEIGHT;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_DURATION;
import static com.example.ezfit.DatabaseHelper.KEY_WORKOUT_ID;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_ID;
import static com.example.ezfit.DatabaseHelper.KEY_WORKOUT_USER_ID;

public class DatabaseManager {
    Context context;
    private DatabaseHelper myDatabaseHelper;
    private SQLiteDatabase myDatabase;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        myDatabaseHelper = new DatabaseHelper(context);
        myDatabase = myDatabaseHelper.getWritableDatabase();

        return this;
    }

    //---closes the database--- any activity that uses the dB will need to do this
    public void close() {
        myDatabaseHelper.close();
    }

    // Updates the user details in the database
    public boolean updateUser(String user_name, int user_age, float user_weight, String user_gender, float user_height, float user_bodyfat, float user_bmi) {
        ContentValues args = new ContentValues();
        args.put(KEY_USERNAME, user_name);
        args.put(KEY_AGE, user_age);
        args.put(KEY_WEIGHT, user_weight);
        args.put(KEY_GENDER, user_gender);
        args.put(KEY_HEIGHT, user_height);
        args.put(KEY_BODY_FAT, user_bodyfat);
        args.put(KEY_BMI, user_bmi);

        return myDatabase.update("User", args, KEY_ROWID + "=" + 1, null) > 0;
    }

    // Return user details from the database
    public Cursor getUserDetails() {
        Cursor mCursor =
                myDatabase.query(true, "User", new String[] {
                                KEY_ROWID,
                                KEY_USERNAME,
                                KEY_AGE,
                                KEY_WEIGHT,
                                KEY_GENDER,
                                KEY_HEIGHT,
                                KEY_BODY_FAT,
                                KEY_BMI
                        },
                        KEY_ROWID + "=" + 1,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    // Return workout or run history from the database
    public Cursor getWorkoutHistory(String workout_type) {
        return myDatabase.query("Workout", new String[] {
                                KEY_ROWID,
                                KEY_TYPE,
                                KEY_BODY_PARTS,
                                KEY_WORKOUT_DURATION,
                                KEY_DATE,
                                KEY_WORKOUT_NAME
                        },
                        KEY_TYPE + "=?",
                                new String[]{workout_type},
                        null,
                        null,
                                KEY_DATE,
                        null);
    }

    // Add a workout to the database
    public int addWorkout(String workout_type, int workout_duration, String workout_name, String bodyparts) {
        // Format todays date to be added to the database
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String date = dateFormat.format(localDate);

        ContentValues args = new ContentValues();
        args.put(KEY_TYPE, workout_type);
        args.put(KEY_BODY_PARTS, bodyparts);
        args.put(KEY_WORKOUT_DURATION, workout_duration);
        args.put(KEY_DATE, date);
        args.put(KEY_WORKOUT_NAME, workout_name);
        args.put(KEY_WORKOUT_USER_ID, 1);

        return (int) myDatabase.insert("Workout", null, args);
    }

    public boolean deleteWorkout(int workout_id) {

        return myDatabase.delete("Workout", KEY_ROWID + "=" + workout_id, null) > 0;
    }

    // Get exercises in a workout
    public Cursor getExercisesInWorkout(int workout_id) {
        return myDatabase.query(true, "Exercise_In_Workout", new String[] {
                        KEY_ROWID,
                        KEY_EXERCISE_NAME,
                        KEY_AVG_SPEED,
                        KEY_DISTANCE,
                        KEY_SETS,
                        KEY_REPS,
                        KEY_EXERCISE_WEIGHT,
                        KEY_EXERCISE_DURATION,
                        KEY_WORKOUT_ID,
                        KEY_EXERCISE_ID
                },
                KEY_WORKOUT_ID + "=" + workout_id,
                null,
                null,
                null,
                KEY_ROWID,
                null);
    }

    // Add a workout to the database
    public long addExercise(int workout_id, String exercise_name, float avg_speed, float distance, int sets, int reps, float weight_lifted, int duration) {
        ContentValues args = new ContentValues();
        args.put(KEY_WORKOUT_ID, workout_id);
        args.put(KEY_EXERCISE_NAME, exercise_name);
        args.put(KEY_AVG_SPEED, avg_speed);
        args.put(KEY_DISTANCE, distance);
        args.put(KEY_SETS, sets);
        args.put(KEY_REPS, reps);
        args.put(KEY_EXERCISE_WEIGHT, weight_lifted);
        args.put(KEY_EXERCISE_DURATION, duration);

        return myDatabase.insert("Exercise_In_Workout", null, args);
    }

    // Remove an exercise from the database by its id
    public boolean removeExerciseByExerciseID(int exercise_id) {

        return myDatabase.delete("Exercise_In_Workout", KEY_ROWID + "=" + exercise_id, null) > 0;
    }

    // Remove exercises from the database by the workout id that they are in
    public boolean removeExerciseByWorkoutID(int workout_id) {

        return myDatabase.delete("Exercise_In_Workout", KEY_WORKOUT_ID + "=" + workout_id, null) > 0;
    }

    public int countWorkouts(String workout_type) {
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM Workout WHERE workout_type=?", new String[] {workout_type});

        return cursor.getCount();
    }

    public int getAverageSessionTime(String workout_type) {
        int totalTime = 0;
        Cursor cursor = myDatabase.rawQuery("SELECT SUM(" + KEY_WORKOUT_DURATION + ") as Time FROM Workout WHERE workout_type=?", new String[] {workout_type});

        if (cursor.moveToFirst()) {
            totalTime = cursor.getInt(cursor.getColumnIndex("Time"));
        }

        if(countWorkouts(workout_type) > 0) {
            return totalTime / countWorkouts(workout_type);
        } else {
            return 0;
        }
    }
}