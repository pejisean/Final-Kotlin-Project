package com.example.kotlin_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME = "DIARY.db";
    static final int DATABASE_VERSION = 1;


    static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS User (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "passwordHash TEXT NOT NULL, " +
            "creationDate TEXT NOT NULL" +
            ");";

    static final String CREATE_NOTES_TABLE =
            "CREATE TABLE IF NOT EXISTS Note (" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    userId INTEGER NOT NULL," +
            "    title TEXT NOT NULL," +
            "    content TEXT NOT NULL," +
            "    date TEXT NOT NULL," +
            "    FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE" +
            ");";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
    }
}
