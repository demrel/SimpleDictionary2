package com.example.bv.simpledictionary;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.bv.simpledictionary.Activities.DailyWord;
import com.example.bv.simpledictionary.menu.NotificationEdit;




import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.FIRST_ID;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.FIRST_RUN;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_CONSTANTE_MINUTE;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_FIRST_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_SECOND_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_THIRD_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.NAME_SP;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.SECOND_ID;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.THIRD_ID;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo_notification);


        ViewPager viewPager=(ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CategoryFragmentPageAdapter(this,getSupportFragmentManager()));
        TabLayout tab=(TabLayout) findViewById(R.id.tab);
        tab.setupWithViewPager(viewPager);

        SharedPreferences sharedPreferences=getSharedPreferences(NAME_SP,MODE_PRIVATE);
        Boolean firstRun=sharedPreferences.getBoolean(FIRST_RUN,true);
        if(firstRun){
            Toast.makeText(this, R.string.first_run, Toast.LENGTH_SHORT).show();
            NotifyBuilder notifyBuilder=new NotifyBuilder(this);
            notifyBuilder.makeNotification(notifyBuilder.getNotification(getString(R.string.notification_content_main)), FIRST_ID,M_FIRST_TIME_HOUR, M_CONSTANTE_MINUTE);
            notifyBuilder.makeNotification(notifyBuilder.getNotification(getString(R.string.notification_content_main)), SECOND_ID, M_SECOND_TIME_HOUR, M_CONSTANTE_MINUTE);
            notifyBuilder.makeNotification(notifyBuilder.getNotification(getString(R.string.notification_content_main)), THIRD_ID, M_THIRD_TIME_HOUR, M_CONSTANTE_MINUTE);

            sharedPreferences.edit().putBoolean(FIRST_RUN,false).apply();
        }
      }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
         super.onCreateOptionsMenu(menu);
        return true;

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.notificaiton:
                Intent intent =new Intent(this, NotificationEdit.class );
                    startActivity(intent);
                return true;
            case R.id.about:
                Intent intent2 =new Intent(this, DailyWord.class );
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
