package com.example.bv.simpledictionary.Activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;

public class AddWord extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WORDS_LIST_LOADER = 0;
    private Uri mCurrentWordUri;

    private EditText mWordEditText;
    private EditText mTranslateEditText;
    private EditText mTranscriptionEditText;
    private SeekBar mRatingBar;
    private TextView mRatingView;
    private EditText mDescriptionEditText;


    private View.OnTouchListener touchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
         mRatingBar = (SeekBar) findViewById(R.id.ratingBar);
        mRatingView =(TextView) findViewById(R.id.rating_TextView);
        mRatingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRatingView.setText(makeRating(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button delete = (Button) findViewById(R.id.delete);

        Intent intent=getIntent();
        mCurrentWordUri=intent.getData();
        if(mCurrentWordUri==null){
            setTitle("Add new word");
            delete.setVisibility(View.GONE);
        }
        else {
            setTitle("Change word");
            getLoaderManager().initLoader(WORDS_LIST_LOADER,null,this);
        }
        mWordEditText = (EditText) findViewById(R.id.word);
        mTranslateEditText = (EditText) findViewById(R.id.translate);
        mTranscriptionEditText= (EditText) findViewById(R.id.transcription);
        mDescriptionEditText= (EditText) findViewById(R.id.descriptionEditText);


        mTranscriptionEditText.setOnTouchListener(touchListener);
        mWordEditText.setOnTouchListener(touchListener);
        mTranslateEditText.setOnTouchListener(touchListener);
        mDescriptionEditText.setOnTouchListener(touchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addword_menu,menu);
         super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                finish();
                return true;
        }
        return false;
    }

    //onclick buttons
    public void add(View view) {
        saveData();
        resetTextArea();
    }

    //button delete
    public void delete(View view) {
        deleteData();
    }

    public void deleteData(){
        if(mCurrentWordUri!=null){
            int rowsDeleted=getContentResolver().delete(mCurrentWordUri,null,null);
            if (rowsDeleted==0) Toast.makeText(this, "Row not deleted", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Success  delete", Toast.LENGTH_SHORT).show();
            }
            finish();
    }

    private void saveData(){
        String word=mWordEditText.getText().toString().trim();
        String translate=mTranslateEditText.getText().toString().trim();
        String transcription=mTranscriptionEditText.getText().toString().trim();
        String description=mDescriptionEditText.getText().toString().trim();
        int rating=mRatingBar.getProgress();
        if (mCurrentWordUri==null && TextUtils.isEmpty(word)&&TextUtils.isEmpty(translate)){
        return;
        }
        int id =getIntent().getIntExtra("id",-1);
        ContentValues contentValues=new ContentValues();
        contentValues.put(Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE,word);
        contentValues.put(Contract.DictionaryEntry.COLUMN_TRANSLATE,translate);
        contentValues.put(Contract.DictionaryEntry.COLUMN_TRANSCRIPTION,transcription);
        contentValues.put(Contract.DictionaryEntry.COLUMN_DEGREE,rating);
        contentValues.put(Contract.DictionaryEntry.COLUMN_DESCRIPTION,description);
        if (mCurrentWordUri==null){
            contentValues.put(Contract.DictionaryEntry.DIRECTORY_ID,id);
            Uri uri=getContentResolver().insert(Contract.DictionaryEntry.CONTENT_URI,contentValues);
            if (uri==null){
                Toast.makeText(this,"Failed to insert data",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Success to insert data", Toast.LENGTH_SHORT).show();
        }
        else {
            int rowAffected=getContentResolver().update(mCurrentWordUri,contentValues,null,null);
            if(rowAffected==0){
                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Success to update data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,mCurrentWordUri,null,null,null,null);    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor==null||cursor.getCount()<1){
            return;
        }
        if(cursor.moveToFirst()){
            int wordColumnIndex=cursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE);
            int translateColumnIndex=cursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_TRANSLATE);
            int transcriptionIndex=cursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_TRANSCRIPTION);
            int ratingIndex=cursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_DEGREE);
            int descriptionIndex=cursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_DESCRIPTION);


            int rating=cursor.getInt(ratingIndex);
            String word=cursor.getString(wordColumnIndex);
            String transcription=cursor.getString(transcriptionIndex);
            String translate=cursor.getString(translateColumnIndex);
            String descriptionText=cursor.getString(descriptionIndex);
            mWordEditText.setText(word);
            mTranslateEditText.setText(translate);
            mTranscriptionEditText.setText(transcription);
            mDescriptionEditText.setText(descriptionText);
            mRatingBar.setProgress(rating);
            mRatingView.setText(makeRating(rating));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        resetTextArea();
        //hard coding make default progress higher it is number 3
        mRatingBar.setProgress(3);
        mRatingView.setText(makeRating(3));
    }

    private String makeRating(int progress){
        String text="";
        switch (progress){
            case 0:
                text="Not Show";
                break;
            case 1:
                text="Low";
                break;
            case 2:
                text="Medium";
                break;
            case 3:
                text="Faster";
                break;
        }
        return text;
    }

    private void resetTextArea(){
        mWordEditText.setText("");
        mTranslateEditText.setText("");
        mTranscriptionEditText.setText("");
        mDescriptionEditText.setText("");
    }



}
