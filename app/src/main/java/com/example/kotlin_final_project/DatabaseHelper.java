package com.example.kotlin_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME = "DIARY.db";
    static final int DATABASE_VERSION = 1;

    //Users Table Building
    static final String USERS_TABLE = "Users";
    static final String USERS_ID = "id";
    static final String USERS_NAME = "Name";
    static final String USERS_EMAIL = "Email";
    static final String USERS_PASSWORD = "Password";
    static final String USERS_DATE = "Creation_Date";

    static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" +
                    USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USERS_NAME + " TEXT NOT NULL, " +
                    USERS_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    USERS_PASSWORD + " TEXT NOT NULL, " +
                    USERS_DATE + " TEXT NOT NULL" +
                    ");";


    //Notes Table Building
    static final String CREATE_NOTES_TABLE =
            "CREATE TABLE IF NOT EXISTS Note (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userId INTEGER NOT NULL, " +
                    "title TEXT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "date TEXT NOT NULL, " +
                    "FOREIGN KEY(userId) REFERENCES Users(id) ON DELETE CASCADE" +
                    ");";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
    }
}
