package com.example.mathapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATE = "mydate";
    public static final String NUMBER_CORRECT = "numberCorrect";
    public static final String NUMBER_INCORRECT = "numberIncorrect";
    public static final String DATABASE_NAME = "db";
    public static final String TABLE = "scores";
    public static final String ID = "_id";
    public static final String DATABASE_VERSION = "1";
    private static DatabaseHelper mInstance = null;

    private DatabaseHelper(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, Integer.parseInt(DATABASE_VERSION));
    }

    public static DatabaseHelper getInstance(Context ctx)
    {
        if (mInstance == null)
        {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        android.util.Log.i("onCreate", "Creating Database");

        db.execSQL("CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT, " + NUMBER_CORRECT + " INTEGER, " + NUMBER_INCORRECT + " INTEGER);");
    }

    public void recordScore(int numberCorrect, int numberIncorrect)
    {
        ContentValues cv = new ContentValues();

        cv.put(DATE, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(new Date()));
        cv.put(NUMBER_CORRECT, numberCorrect);
        cv.put(NUMBER_INCORRECT, numberIncorrect);
        getWritableDatabase().insert(TABLE, DATE, cv);
    }

    public void deleteRow(long id)
    {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE + " WHERE _id = '" + id + "'");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        android.util.Log.w("Constants", "Upgrading database, which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS constants");
        onCreate(getWritableDatabase());
    }
}
