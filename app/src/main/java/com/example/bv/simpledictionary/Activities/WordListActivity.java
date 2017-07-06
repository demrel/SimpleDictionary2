package com.example.bv.simpledictionary.Activities;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.example.bv.simpledictionary.Adapter.DictionaryAdapter;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;


public class WordListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int TASK_LOADER_ID=2;

    private DictionaryAdapter mAdapter;

    private RecyclerView recyclerView;

    private EditText mSearch;
    private String directoryID;
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;
    private String filterStr = "";

    private final BroadcastReceiver FragmentReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            getSupportLoaderManager().restartLoader(0, null,WordListActivity.this);
        }};
    TextView.OnEditorActionListener actionListener= new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        directoryID=String.valueOf(getIntent().getIntExtra("id",-1));
        setTitle(getIntent().getStringExtra("name")+" Dictionary");

        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewWords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new DictionaryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        loaderCallbacks=this;
        mSearch=(EditText) findViewById(R.id.etSearch);

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStr=s.toString().toLowerCase();
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,WordListActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Intent intent =new Intent(this,AddWord.class);
        FloatingActionButton actionButton= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id= Integer.parseInt(directoryID);
                intent.putExtra("id",id);
                startActivity(intent);
            }

        });
        mSearch.setOnEditorActionListener(actionListener);
        registerReceiver(FragmentReceiver1,new IntentFilter("deleteWord"));
        getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);

    }



    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
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
                try {
                    return getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI,null,Contract.DictionaryEntry.DIRECTORY_ID+" =?" +"AND "+
                            Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE +" LIKE ?",
                            new String[]{directoryID,"%"+filterStr+"%"},
                            Contract.DirectoryEntry.COLUMN_CREATE_DATE+" DESC");
                }
                catch (Exception e){
                    Log.e(WordListActivity.class.getName(),"FAILED ASYNCHRONOUSLY LOAD DATA. ");
                    e.printStackTrace();
                    return  null;
                }

            }
            public void deliverResult(Cursor data){
                mTaskData=data;
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
