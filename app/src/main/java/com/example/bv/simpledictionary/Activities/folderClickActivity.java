package com.example.bv.simpledictionary.Activities;


import android.content.Intent;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;


public class folderClickActivity extends AppCompatActivity {
    int id;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_click);
        id=getIntent().getIntExtra("id",-1);
        name=getIntent().getStringExtra("name");
        setTitle(name+" Dictionary");
    }

    public void add(View view) {

        Intent intent =new Intent(this,AddWord.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void view(View view) {
        Intent intent = new Intent(this,WordListActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    public void quiz(View view) {
       Cursor cursor= getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI,null,Contract.DictionaryEntry.DIRECTORY_ID+" =?",
                new String[]{String.valueOf(id)},
                Contract.DirectoryEntry.COLUMN_CREATE_DATE+" ASC");
        if (cursor!=null&&cursor.getCount()>5){
            Intent intent = new Intent(this,Quiz.class);
            intent.putExtra("id",id);
            intent.putExtra("name",name);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "add at least 5 word", Toast.LENGTH_SHORT).show();
        }


    }
}
