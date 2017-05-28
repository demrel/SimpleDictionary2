package com.example.bv.simpledictionary.Data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by bv on 3/22/2017.
 */

public class ContentProvider extends android.content.ContentProvider {
    public static final int DIRECTORY=100;
    public static final int DIRECTORY_ID=101;
    public static final int DICTIONARY=200;
    public static final int DICTIONARY_ID=201;
    private static final UriMatcher sUriMatcher=buildUriMatcher();
    private DBHelper mDBHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Contract.AUTHORITY,Contract.PATH_DICTIONARY,DICTIONARY);
        uriMatcher.addURI(Contract.AUTHORITY,Contract.PATH_DICTIONARY+"#",DICTIONARY_ID);
        uriMatcher.addURI(Contract.AUTHORITY,Contract.PATH_DIRECTORY,DIRECTORY);
        uriMatcher.addURI(Contract.AUTHORITY,Contract.PATH_DIRECTORY+"#",DIRECTORY_ID);
        return uriMatcher;

    }


    @Override
    public boolean onCreate() {
        Context context=getContext();
        mDBHelper=new DBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db=mDBHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case DICTIONARY:
                retCursor=db.query(Contract.DictionaryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case DIRECTORY:
                retCursor=db.query(Contract.DirectoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw  new UnsupportedOperationException("Unknown query Uri "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }



    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db=mDBHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        Uri retUri;
        long id;
        switch (match){
            case DICTIONARY:
                id=db.insert(Contract.DictionaryEntry.TABLE_NAME,null,values);
                if(id>0){
                    retUri= ContentUris.withAppendedId(Contract.DictionaryEntry.CONTENT_URI,id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
                break;
            case DIRECTORY:
                id=db.insert(Contract.DirectoryEntry.TABLE_NAME,null,values);
                if(id>0){
                    retUri= ContentUris.withAppendedId(Contract.DirectoryEntry.CONTENT_URI,id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw  new UnsupportedOperationException("Unknown insert Uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       final SQLiteDatabase db=mDBHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        int directoryDeleted;
        String id;
        switch (match){
            case DICTIONARY_ID:
               id =uri.getPathSegments().get(1);
                directoryDeleted=db.delete(Contract.DictionaryEntry.TABLE_NAME,"_id=?",new String[]{id});
                break;
            case DIRECTORY_ID:
                id=uri.getPathSegments().get(1);
                directoryDeleted=db.delete(Contract.DirectoryEntry.TABLE_NAME,"_id=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown delete Uri");
        }
        if(directoryDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return directoryDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db=mDBHelper.getWritableDatabase();
        int match =sUriMatcher.match(uri);
        int rowUpdated;
        String id;
        switch (match){
            case DICTIONARY_ID:
                id=uri.getPathSegments().get(1);
                rowUpdated=db.update(Contract.DictionaryEntry.TABLE_NAME,values,"_id=?",new String[]{id});
                 break;
            case DIRECTORY_ID:
                id=uri.getPathSegments().get(1);
                rowUpdated=db.update(Contract.DirectoryEntry.TABLE_NAME,values,"_id=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknow update Uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
