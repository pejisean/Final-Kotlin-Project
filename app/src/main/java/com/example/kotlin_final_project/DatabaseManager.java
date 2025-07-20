package com.example.kotlin_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;


    public DatabaseManager(Context ctx) {
        context = ctx;
    }

    public DatabaseManager open() throws SQLDataException{
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void userInsert(String username, String password, String email, String creationDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_NAME, username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD, password);
        contentValues.put(DatabaseHelper.USERS_EMAIL, email);
        contentValues.put(DatabaseHelper.USERS_DATE, creationDate);
        database.insert(DatabaseHelper.USERS_TABLE, null, contentValues);

    }

    public Cursor userFetch(){
        String [] columns = new String[] {
                DatabaseHelper.USERS_ID,
                DatabaseHelper.USERS_NAME,
                DatabaseHelper.USERS_PASSWORD,
                DatabaseHelper.USERS_EMAIL,
                DatabaseHelper.USERS_DATE
        };
        Cursor cursor = database.query(DatabaseHelper.USERS_TABLE, columns, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int userUpdate(int id, String username, String password, String email, String creationDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_NAME, username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD, password);
        contentValues.put(DatabaseHelper.USERS_EMAIL, email);
        contentValues.put(DatabaseHelper.USERS_DATE, creationDate);
        return database.update(DatabaseHelper.USERS_TABLE, contentValues, DatabaseHelper.USERS_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void userDelete(int id) {
        database.delete(DatabaseHelper.USERS_TABLE, DatabaseHelper.USERS_ID + " = ?", new String[]{String.valueOf(id)});
    }
}