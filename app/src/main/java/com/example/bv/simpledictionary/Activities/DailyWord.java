package com.example.bv.simpledictionary.Activities;

import com.example.bv.simpledictionary.Adapter.DictionaryAdapter;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.NAME_SP;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.TimeConstants.T_HIGH;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.TimeConstants.T_LOW;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.TimeConstants.T_MEDIUM;


public class DailyWord extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerView;
    private DictionaryAdapter mAdapter;
    public static final int TASK_LOADER_ID=5;
    private SharedPreferences sharedPreferences;
    private long timelow;
    private long timeMed;
    private long timeHigh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_word);
        sharedPreferences=getSharedPreferences(NAME_SP,MODE_PRIVATE);
        boolean first=  sharedPreferences.getBoolean("firstDaily",true);
        if(first){
            sharedPreferences.edit()
                    .putLong(T_LOW,System.currentTimeMillis())
                    .putLong(T_MEDIUM,System.currentTimeMillis())
                    .putLong(T_HIGH,System.currentTimeMillis())
                    .putBoolean("firstDaily",false).apply();
        }
        timelow = sharedPreferences.getLong(T_LOW,System.currentTimeMillis());
        timeMed = sharedPreferences.getLong(T_MEDIUM,System.currentTimeMillis());
        timeHigh = sharedPreferences.getLong(T_HIGH,System.currentTimeMillis());
        recyclerView = (RecyclerView) findViewById(R.id.dailyListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new DictionaryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(TASK_LOADER_ID,null,this);

    }

    @Override
    protected void onResume() {
            super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData=null;

            @Override
            protected void onStartLoading() {
                if(mTaskData!=null) deliverResult(mTaskData);
                else forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                Cursor cursor=null;
                Cursor cursor1;
                int limit=5;
                long diff=System.currentTimeMillis()-timelow;
                   int daydifL=(int)TimeUnit.MICROSECONDS.toDays(diff);
                    int daydifM=(int)TimeUnit.MICROSECONDS.toDays(System.currentTimeMillis()-timeMed);
                try {
                    if(daydifL%5==0&&daydifL!=0){
                        cursor=getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI, null,
                                Contract.DictionaryEntry.COLUMN_DEGREE+" =?",
                                new String[]{"1"},
                                "RANDOM() limit 2");
                    }
                    else if(daydifM%2==0){
                        cursor=getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI, null,
                                Contract.DictionaryEntry.COLUMN_DEGREE+" =?",
                                new String[]{"2"},
                                "RANDOM() limit 2");
                    }


                    int count= cursor.getCount();

                    switch (count){
                        case 0:limit=5;
                            break;
                        case 1:limit=4;
                            break;
                        case 2:limit=3;
                            break;
                    }
                    cursor1=getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI, null,
                            Contract.DictionaryEntry.COLUMN_DEGREE+" =?",
                            new String[]{"3"},"RANDOM() limit "+String.valueOf(limit));
                           MergeCursor cursor2=new MergeCursor(new Cursor[] {cursor,cursor1} );
                    return cursor2;
                } catch (Exception e) {
                    Log.e(WordListActivity.class.getName(), "FAILED ASYNCHRONOUSLY LOAD DATA. ");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
/*
 getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI,null,Contract.DictionaryEntry.DIRECTORY_ID+" =?" +"AND "+
        Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE +" LIKE ?",
        new String[]{directoryID,"%"+filterStr+"%"},
        Contract.DirectoryEntry.COLUMN_CREATE_DATE+" DESC");
*/

//todo-3 make random 5 words here from sqlite  url: https://stackoverflow.com/questions/8369767/random-in-sqlite-and-android-sdk
//make logic with day and start first low to high with get cursor.count; and make switch case statment
// "RANDOM() limit "+String.valueOf(limit))