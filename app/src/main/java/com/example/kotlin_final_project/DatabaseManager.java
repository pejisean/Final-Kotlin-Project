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


    //User Management Methods
    //Insert a new user
    public void userInsert(String username, String password, String email, String creationDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_NAME, username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD, password);
        contentValues.put(DatabaseHelper.USERS_EMAIL, email);
        contentValues.put(DatabaseHelper.USERS_DATE, creationDate);
        database.insert(DatabaseHelper.USERS_TABLE, null, contentValues);

    }

    // Fetch all users
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

    // Update a user by id
    public int userUpdate(int id, String username, String password, String email, String creationDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USERS_NAME, username);
        contentValues.put(DatabaseHelper.USERS_PASSWORD, password);
        contentValues.put(DatabaseHelper.USERS_EMAIL, email);
        contentValues.put(DatabaseHelper.USERS_DATE, creationDate);
        return database.update(DatabaseHelper.USERS_TABLE, contentValues, DatabaseHelper.USERS_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete a user by id
    public void userDelete(int id) {
        database.delete(DatabaseHelper.USERS_TABLE, DatabaseHelper.USERS_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //Note Management Methods
    // Insert a new note
    public void noteInsert(int userId, String title, String content, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NOTES_USER_ID, userId);
        contentValues.put(DatabaseHelper.NOTES_TITLE, title);
        contentValues.put(DatabaseHelper.NOTES_CONTENT, content);
        contentValues.put(DatabaseHelper.NOTES_DATE, date);
        database.insert(DatabaseHelper.NOTES_TABLE, null, contentValues);
    }

    // Fetch notes for a specific user
    public Cursor noteFetch(int userId) {
        String[] columns = new String[] {
                DatabaseHelper.NOTES_ID,
                DatabaseHelper.NOTES_USER_ID,
                DatabaseHelper.NOTES_TITLE,
                DatabaseHelper.NOTES_CONTENT,
                DatabaseHelper.NOTES_DATE
        };
        Cursor cursor = database.query(
                DatabaseHelper.NOTES_TABLE,
                columns,
                DatabaseHelper.NOTES_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    // Update a note by its id
    public int noteUpdate(int noteId, String title, String content, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NOTES_TITLE, title);
        contentValues.put(DatabaseHelper.NOTES_CONTENT, content);
        contentValues.put(DatabaseHelper.NOTES_DATE, date);
        return database.update(
                DatabaseHelper.NOTES_TABLE,
                contentValues,
                DatabaseHelper.NOTES_ID + " = ?",
                new String[]{String.valueOf(noteId)}
        );
    }

    // Delete a note by its id
    public void noteDelete(int noteId) {
        database.delete(
                DatabaseHelper.NOTES_TABLE,
                DatabaseHelper.NOTES_ID + " = ?",
                new String[]{String.valueOf(noteId)}
        );
    }
}