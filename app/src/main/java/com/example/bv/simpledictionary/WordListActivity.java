package com.example.bv.simpledictionary;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.bv.simpledictionary.Adapter.DictionaryAdapter;


public class WordListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TASK_LOADER_ID=1;

    private DictionaryAdapter mAdapter;

    private RecyclerView recyclerView;

    private EditText mSearch;
    private String filterStr = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        recyclerView=(RecyclerView) findViewById(R.id.recyclerViewWords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new DictionaryAdapter();
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


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
