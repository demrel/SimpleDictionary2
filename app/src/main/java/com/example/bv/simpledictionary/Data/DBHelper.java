package com.example.bv.simpledictionary.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.bv.simpledictionary.Data.Contract.DirectoryEntry;

/**
 * Created by bv on 3/22/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="dictionary.db";
    private static final int DATABASE_VERSION=1;




    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_DIRECTORY=
        "CREATE TABLE "+ DirectoryEntry.TABLE_NAME+" ("+
                DirectoryEntry._ID +" INTEGER PRIMARY KEY, "+
                DirectoryEntry.COLUMN_DIRECTORY_NAME+" TEXT NOT NULL, "+
                DirectoryEntry.COLUMN_CREATE_DATE+" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);";
        final String CREATE_TABLE_DICTIONARY=
                "CREATE TABLE "+ Contract.DictionaryEntry.TABLE_NAME+" ("+
                        Contract.DictionaryEntry._ID +" INTEGER PRIMARY KEY, "+
                        Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE+" TEXT NOT NULL, "+
                        Contract.DictionaryEntry.COLUMN_TRANSLATE+" TEXT NOT NULL, "+
                        Contract.DictionaryEntry.DIRECTORY_ID+" INTEGER ," +
                        "FOREIGN KEY ("+ Contract.DictionaryEntry.DIRECTORY_ID+") REFERENCES "+ DirectoryEntry.TABLE_NAME+"("+ DirectoryEntry._ID+"));";

        db.execSQL(CREATE_TABLE_DIRECTORY);
       db.execSQL(CREATE_TABLE_DICTIONARY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + Contract.DictionaryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + Contract.DirectoryEntry.TABLE_NAME);
       onCreate(db);
    }
}
