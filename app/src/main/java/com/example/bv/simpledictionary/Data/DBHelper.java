package com.example.bv.simpledictionary.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bv.simpledictionary.Data.Contract.DirectoryEntry;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 2;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_DIRECTORY =
                "CREATE TABLE " + DirectoryEntry.TABLE_NAME + " (" +
                        DirectoryEntry._ID + " INTEGER PRIMARY KEY, " +
                        DirectoryEntry.COLUMN_DIRECTORY_NAME + " TEXT NOT NULL, " +
                        DirectoryEntry.COLUMN_CREATE_DATE + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);";

        final String CREATE_TABLE_DICTIONARY =
                "CREATE TABLE " + Contract.DictionaryEntry.TABLE_NAME + " (" +
                        Contract.DictionaryEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE + " TEXT NOT NULL, " +
                        Contract.DictionaryEntry.COLUMN_TRANSCRIPTION + " TEXT NOT NULL, " +
                        Contract.DictionaryEntry.COLUMN_DESCRIPTION+" TEXT NOT NULL, "+
                        Contract.DictionaryEntry.COLUMN_DEGREE + " INTEGER NOT NULL DEFAULT 3, " +
                        DirectoryEntry.COLUMN_CREATE_DATE + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        Contract.DictionaryEntry.COLUMN_TRANSLATE + " TEXT NOT NULL, " + Contract.DictionaryEntry.DIRECTORY_ID + " INTEGER ," +
                        "FOREIGN KEY (" + Contract.DictionaryEntry.DIRECTORY_ID + ") REFERENCES " + DirectoryEntry.TABLE_NAME + "(" + DirectoryEntry._ID + "));";

        final String CREATE_TABLE_QUIZ =
                "CREATE TABLE " + Contract.QuizEntry.TABLE_NAME + " (" +
                        Contract.QuizEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.QuizEntry.COLUMN_ALLQUESTION + " INTEGER NOT NULL, " +
                        Contract.QuizEntry.COLUMN_CORRECT + " INTEGER NOT NULL, " +
                        Contract.QuizEntry.COLUMN_WRONG + " INTEGER NOT NULL, " +
                        Contract.QuizEntry.COLUMN_EXPECTED + " INTEGER NOT NULL, " +
                        Contract.QuizEntry.COLUMN_TIME + " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                        Contract.QuizEntry.DIRECTORY_ID + " INTEGER ," +
                        "FOREIGN KEY (" + Contract.QuizEntry.DIRECTORY_ID + ") REFERENCES " + DirectoryEntry.TABLE_NAME + "(" + DirectoryEntry._ID + "));";

                db.execSQL(CREATE_TABLE_DIRECTORY);
                db.execSQL(CREATE_TABLE_DICTIONARY);
                db.execSQL(CREATE_TABLE_QUIZ);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + Contract.DictionaryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + Contract.DirectoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + Contract.QuizEntry.TABLE_NAME);
        onCreate(db);
    }
}
