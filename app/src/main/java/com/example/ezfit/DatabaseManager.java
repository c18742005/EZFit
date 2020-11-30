package com.example.ezfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

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
import static com.example.ezfit.DatabaseHelper.KEY_WORKOUT_USER_ID;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_NAME;
import static com.example.ezfit.DatabaseHelper.KEY_AVG_SPEED;
import static com.example.ezfit.DatabaseHelper.KEY_DISTANCE;
import static com.example.ezfit.DatabaseHelper.KEY_SETS;
import static com.example.ezfit.DatabaseHelper.KEY_REPS;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_WEIGHT;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_DURATION;
import static com.example.ezfit.DatabaseHelper.KEY_WORKOUT_ID;
import static com.example.ezfit.DatabaseHelper.KEY_EXERCISE_ID;

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

    //---deletes a particular contact person---
    public boolean deleteTask(long rowId) {
        // delete statement.  If any rows deleted (i.e. >0), returns true
        return myDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the rows ---
    public Cursor getAllWorkouts() {
        return myDatabase.query("User", new String[] {
                        KEY_ROWID,
                        KEY_WORKOUT_DURATION,
                        KEY_DATE,
                        KEY_WORKOUT_NAME},
                KEY_TYPE + "=" + "workout",
                null,
                null,
                null,
                null);
    }

    //---retrieves a particular contact person---
    public Cursor getTask(long rowId) throws SQLException {
        Cursor mCursor =
                myDatabase.query(true, DATABASE_TABLE, new String[] {
                                KEY_ROWID,
                                KEY_TASKNAME,
                                KEY_TASKDESC,
                                KEY_COMPLETESTATUS
                        },
                        KEY_ROWID + "=" + rowId,
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

    //---updates a contact person---
    public boolean updateTask(long rowId, String taskName,
                              String taskDesc, String completeStatus) {
        ContentValues args = new ContentValues();
        args.put(KEY_TASKNAME, taskName);
        args.put(KEY_TASKDESC, taskDesc);
        args.put(KEY_COMPLETESTATUS, completeStatus);

        return myDatabase.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}