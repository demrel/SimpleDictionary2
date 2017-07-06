package com.example.bv.simpledictionary.menu;


import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bv.simpledictionary.NotifyBuilder;
import com.example.bv.simpledictionary.R;



import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.FIRST_ID;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.FIRST_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.FIRST_TIME_MINUTE;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_CONSTANTE_MINUTE;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_FIRST_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_SECOND_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.MainConstantTime.M_THIRD_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.NAME_SP;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.SECOND_ID;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.SECOND_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.SECOND_TIME_MINUTE;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.THIRD_ID;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.THIRD_TIME_HOUR;
import static com.example.bv.simpledictionary.Data.SharedPreferenceConstants.THIRD_TIME_MINUTE;

public class NotificationEdit extends AppCompatActivity {

    Button firstButton;
    Button secondButton;
    Button thirdButton;
   private  int firstTimeHour;
   private  int secondTimeHour;
   private  int thirdTimeHour;

    private  int firstTimeMinute;
    private  int secondTimeMinute;
    private  int thirdTimeMinute;

    private int mTime;
    private int mMinute;
  private   SharedPreferences sharedPreferences;
   private NotifyBuilder notifyBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_edit);
         sharedPreferences=getSharedPreferences(NAME_SP,MODE_PRIVATE);

        notifyBuilder=new NotifyBuilder(this);
        firstButton= (Button) findViewById(R.id.first);
        secondButton= (Button) findViewById(R.id.second);
        thirdButton= (Button) findViewById(R.id.third);

     firstTimeHour = sharedPreferences.getInt(FIRST_TIME_HOUR,M_FIRST_TIME_HOUR);
     secondTimeHour =sharedPreferences.getInt(SECOND_TIME_HOUR,M_SECOND_TIME_HOUR);
     thirdTimeHour =sharedPreferences.getInt(THIRD_TIME_HOUR,M_THIRD_TIME_HOUR);

        firstTimeMinute = sharedPreferences.getInt(FIRST_TIME_MINUTE,M_CONSTANTE_MINUTE);
        secondTimeMinute = sharedPreferences.getInt(SECOND_TIME_MINUTE,M_CONSTANTE_MINUTE);
        thirdTimeMinute = sharedPreferences.getInt(THIRD_TIME_MINUTE,M_CONSTANTE_MINUTE);

        firstButton.setText(String.valueOf(firstTimeHour+":"+firstTimeMinute));
        secondButton.setText(String.valueOf(secondTimeHour+":"+secondTimeMinute));
        thirdButton.setText(String.valueOf(thirdTimeHour+":"+thirdTimeMinute));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ok:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void secondClickButton(View view) {
        mTime= sharedPreferences.getInt(SECOND_TIME_HOUR,M_SECOND_TIME_HOUR);
        mMinute=sharedPreferences.getInt(SECOND_TIME_MINUTE,M_CONSTANTE_MINUTE);
        timePicker(R.id.second);
    }
    public void firstClickButton(View view) {
        mTime= sharedPreferences.getInt(FIRST_TIME_HOUR,M_FIRST_TIME_HOUR);
        mMinute=sharedPreferences.getInt(FIRST_TIME_MINUTE,M_CONSTANTE_MINUTE);
        timePicker(R.id.first);
    }

    public void thirdClickButton(View view) {
        mTime= sharedPreferences.getInt(THIRD_TIME_HOUR,M_THIRD_TIME_HOUR);
        mMinute=sharedPreferences.getInt(THIRD_TIME_MINUTE,M_CONSTANTE_MINUTE);
        timePicker(R.id.third);
    }


   private void timePicker(final int id){
       TimePickerDialog timePickerDialog=new TimePickerDialog(NotificationEdit.this, new TimePickerDialog.OnTimeSetListener() {

           @Override
           public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            switch (id){
                case R.id.first:
                    sharedPreferences.edit()
                         .putInt(FIRST_TIME_HOUR,hourOfDay)
                         .putInt(FIRST_TIME_MINUTE,minute).apply();
                    firstButton.setText(String.valueOf(hourOfDay+":"+minute));
                    Toast.makeText(NotificationEdit.this, String.valueOf(sharedPreferences.getInt(FIRST_TIME_HOUR,-20)+":"+sharedPreferences.getInt(FIRST_TIME_MINUTE,-10)), Toast.LENGTH_SHORT).show();
                    notifyBuilder.makeNotification(notifyBuilder.getNotification(String.valueOf(hourOfDay+":"+minute)), FIRST_ID, hourOfDay, minute);

                    break;
                case R.id.second:
                    sharedPreferences.edit()
                            .putInt(SECOND_TIME_HOUR,hourOfDay)
                            .putInt(SECOND_TIME_MINUTE,minute).apply();
                    secondButton.setText(String.valueOf(hourOfDay+":"+minute));
                    Toast.makeText(NotificationEdit.this, String.valueOf(sharedPreferences.getInt(SECOND_TIME_HOUR,-20)+":"+sharedPreferences.getInt(SECOND_TIME_MINUTE,-10)), Toast.LENGTH_SHORT).show();
                    notifyBuilder.makeNotification(notifyBuilder.getNotification(String.valueOf(hourOfDay+":"+minute)), SECOND_ID, hourOfDay, minute);


                    break;
                case R.id.third:
                    sharedPreferences.edit()
                            .putInt(THIRD_TIME_HOUR,hourOfDay)
                            .putInt(THIRD_TIME_MINUTE,minute).apply();
                    thirdButton.setText(String.valueOf(hourOfDay+":"+minute));
                    Toast.makeText(NotificationEdit.this, String.valueOf(sharedPreferences.getInt(THIRD_TIME_HOUR,-20)+":"+sharedPreferences.getInt(THIRD_TIME_MINUTE,-10)), Toast.LENGTH_SHORT).show();

                    notifyBuilder.makeNotification(notifyBuilder.getNotification(getString(R.string.notification_content_main)), THIRD_ID, hourOfDay, minute);

                    break;
            }
           }
       },mTime,mMinute,true);

       timePickerDialog.setTitle("Select Time");
       timePickerDialog.show();
   }



}
