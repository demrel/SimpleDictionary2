package com.example.bv.simpledictionary.Activities;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class Quiz extends AppCompatActivity   {

private    TextView foreignWord;
private     EasyFlipView easyFlipView;
private     Button button;
private     EditText text;
private     TextView correctTextView;
private     TextView translateView;
    String editTextText;

      private   int count=0;
      private   int corect_count=0;
      private   int espected_count=0;
      private   int wrong_count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        text= (EditText) findViewById(R.id.checkEditText);
        correctTextView= (TextView) findViewById(R.id.correctTextView);
        translateView= (TextView) findViewById(R.id.translateTextView);
        button= (Button) findViewById(R.id.button);
        foreignWord= (TextView) findViewById(R.id.foreignWord_TextView);

        easyFlipView= (EasyFlipView) findViewById(R.id.switcherFlipView);
        easyFlipView.setFlipOnTouch(false);
        String directoryID = String.valueOf(getIntent().getIntExtra("id", -1));

        Cursor mCursor = getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI, null, Contract.DictionaryEntry.DIRECTORY_ID + " =?",
                new String[]{directoryID},
                Contract.DirectoryEntry.COLUMN_CREATE_DATE + " ASC");
        int foreignId= mCursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE);
        int translateId= mCursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_TRANSLATE);
        final ArrayList<String> foreign=new ArrayList<>();
        final ArrayList<String> translate=new ArrayList<>();
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()){
            foreign.add(mCursor.getString(foreignId));
            translate.add(mCursor.getString(translateId));
        }
        mCursor.close();



        foreignWord.setText(foreign.get(0));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(easyFlipView.isFrontSide()){
                    editTextText=text.getText().toString();
                    if(foreign.size()<=count+1){
                        button.setText(R.string.finish);
                    }
                    else {
                        button.setText(R.string.next);
                    }
                        if (wordSpliter(translate.get(count),editTextText)) {
                            translateView.setText(translate.get(count));
                            correctTextView.setText(editTextText);
                            correctTextView.setTextColor(Color.GREEN);
                            corect_count++;
                        }
                        else if(text.getText().toString().equals("")||text.getText()==null){
                            translateView.setText(translate.get(count));
                            correctTextView.setText("");
                            espected_count++;
                        }
                        else if(!wordSpliter(translate.get(count),editTextText)) {
                            translateView.setText(translate.get(count));
                            correctTextView.setText(editTextText);
                            correctTextView.setTextColor(Color.RED);
                            wrong_count++;
                        }

                    easyFlipView.flipTheView();
                }

                else if (easyFlipView.isBackSide()) {

                    button.setText(R.string.check);
                    text.setText("");
                    if (foreign.size()<=count+1){
                        finishSaveData(foreign.size(),wrong_count,corect_count,espected_count);
                    }
                    else {
                        count++;
                        foreignWord.setText(foreign.get(count));
                        easyFlipView.flipTheView();
                    }
                }
            }
        });
    }
    private void finishSaveData(int all, int wrong,int correct, int ecspected){
        Bundle contentValues=new Bundle();
        contentValues.putInt(Contract.QuizEntry.COLUMN_ALLQUESTION,all);
        contentValues.putInt(Contract.QuizEntry.COLUMN_WRONG,wrong);
        contentValues.putInt(Contract.QuizEntry.COLUMN_CORRECT,correct);
        contentValues.putInt(Contract.QuizEntry.COLUMN_EXPECTED,ecspected);
        Intent intent= new Intent(this,Chart.class);
        intent.putExtras(contentValues);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private Boolean wordSpliter(String wordT,String wordET){
       String writed= wordET.trim().toLowerCase().trim();
        for (String word:wordT.split(",")){
          if( word.toLowerCase().trim().equals(writed)) return true;
        }
        return false;
    }
}
