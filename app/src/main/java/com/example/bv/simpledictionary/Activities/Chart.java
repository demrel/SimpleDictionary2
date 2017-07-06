package com.example.bv.simpledictionary.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;

public class Chart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        TextView allw= (TextView) findViewById(R.id.allword_textView);
        TextView correctw= (TextView) findViewById(R.id.correct_TextView);
        TextView wrongw= (TextView) findViewById(R.id.wrong_textView);
        TextView ecspectedw= (TextView) findViewById(R.id.escpected_textview);
        Button button= (Button) findViewById(R.id.okButton);
        Bundle bundle=getIntent().getExtras();
        int all=bundle.getInt(Contract.QuizEntry.COLUMN_ALLQUESTION);
        int correct=bundle.getInt(Contract.QuizEntry.COLUMN_CORRECT);
        int wrong=bundle.getInt(Contract.QuizEntry.COLUMN_WRONG);
        int espected=bundle.getInt(Contract.QuizEntry.COLUMN_EXPECTED);
        allw.setText(String.valueOf(all));
        correctw.setText(String.valueOf(correct));
        wrongw.setText(String.valueOf(wrong));
        ecspectedw.setText(String.valueOf(espected));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
