/*
    Helper subclass of SQLiteOpenHelper class to manage the creation of the EZ Fit database (DB)
    Contains strings and method to create the DB tables and the method to control what happens on a
    DB upgrade
 */
package com.example.ezfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String KEY_ROWID = "_id";

    // Constants for the user table
    public static final String KEY_USERNAME = "user_name";
    public static final String KEY_AGE = "user_age";
    public static final String KEY_WEIGHT = "user_weight";
    public static final String KEY_GENDER = "user_gender";
    public static final String KEY_HEIGHT = "user_height";
    public static final String KEY_BODY_FAT = "user_bodyfat";
    public static final String KEY_BMI = "user_bmi";

    // Constants for the workout table
    public static final String KEY_TYPE = "workout_type";
    public static final String KEY_BODY_PARTS = "bodyparts";
    public static final String KEY_WORKOUT_DURATION = "workout_duration";
    public static final String KEY_DATE = "workout_date";
    public static final String KEY_WORKOUT_NAME = "workout_name";
    public static final String KEY_WORKOUT_USER_ID = "workout_user_id";

    // Constants for the exercise table
    public static final String KEY_EXERCISE_NAME = "exercise_name";
    public static final String KEY_AVG_SPEED = "exercise_avgspeed";
    public static final String KEY_DISTANCE = "exercise_distance";
    public static final String KEY_SETS = "exercise_sets";
    public static final String KEY_REPS = "exercise_reps";
    public static final String KEY_EXERCISE_WEIGHT = "exercise_weight";
    public static final String KEY_EXERCISE_DURATION = "exercise_duration";
    public static final String KEY_WORKOUT_ID = "exercise_workout_id";

    // Constants for the image name table
    public static final String KEY_IMAGE_NAME = "image_name";
    public static final String KEY_IMAGE_DATE = "image_date";

    public static final String DATABASE_NAME = "EZFit";
    public static final int DATABASE_VERSION = 1;

    // This is the string containing the SQL database create statement for the user table
    private static final String DATABASE_CREATE_USER_TABLE =
            "create table User"  +
                    " (_id integer primary key autoincrement, " +
                    "user_name text not null, " +
                    "user_age integer not null, "  +
                    "user_weight real not null," +
                    "user_gender text not null," +
                    "user_height real not null," +
                    "user_bodyfat real not null," +
                    "user_bmi real not null);";

    // This is the string containing the SQL database create statement for the workout table
    private static final String DATABASE_CREATE_WORKOUT_TABLE =
            "create table Workout"  +
                    " (_id integer primary key autoincrement, " +
                    "workout_type text not null, " +
                    "bodyparts text," +
                    "workout_duration integer not null, "  +
                    "workout_date text not null," +
                    "workout_name text not null," +
                    "workout_user_id integer not null," +
                    "CONSTRAINT fk_user" +
                    "   FOREIGN KEY (workout_user_id)" +
                    "   REFERENCES User(_id));";

    // This is the string containing the SQL database create statement for the exercises in a workout table
    private static final String DATABASE_CREATE_EXERCISE_IN_WORKOUT_TABLE =
            "create table Exercise_In_Workout"  +
                    " (_id integer primary key autoincrement, " +
                    "exercise_name text not null," +
                    "exercise_avgspeed real, " +
                    "exercise_distance real, "  +
                    "exercise_sets integer," +
                    "exercise_reps integer," +
                    "exercise_weight real," +
                    "exercise_duration integer," +
                    "exercise_workout_id integer not null," +
                    "CONSTRAINT fk_workout" +
                    "   FOREIGN KEY (exercise_workout_id)" +
                    "   REFERENCES Workout(_id));";

    // This is the string containing the SQL database create statement for the images table
    private static final String DATABASE_CREATE_IMAGE_TABLE =
            "create table Image"  +
                    " ( _id integer primary key autoincrement, " +
                    "image_name text not null," +
                    "image_date text);";

    // constructor for the dB helper class
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method called when DB is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Arguments to populate the User table when its first created
        ContentValues args = new ContentValues();
        args.put(KEY_USERNAME, "Unknown");
        args.put(KEY_AGE, 0);
        args.put(KEY_WEIGHT, 0.0);
        args.put(KEY_GENDER, "Unknown");
        args.put(KEY_HEIGHT, 0.0);
        args.put(KEY_BODY_FAT, 0.0);
        args.put(KEY_BMI, 0.0);

        // Statements needed to set up the DB
        db.execSQL(DATABASE_CREATE_USER_TABLE);
        db.insert("User", null, args);
        db.execSQL(DATABASE_CREATE_WORKOUT_TABLE);
        db.execSQL(DATABASE_CREATE_EXERCISE_IN_WORKOUT_TABLE);
        db.execSQL(DATABASE_CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Method for changing structure of DB on an upgrade

    }
}