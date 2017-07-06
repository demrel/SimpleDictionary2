package com.example.bv.simpledictionary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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


public class WakeUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            SharedPreferences sharedPreference=context.getSharedPreferences(NAME_SP,Context.MODE_PRIVATE);
           int firstHour= sharedPreference.getInt(FIRST_TIME_HOUR,M_FIRST_TIME_HOUR);
            int secondHour=sharedPreference.getInt(SECOND_TIME_HOUR,M_SECOND_TIME_HOUR);
            int thirdHour=sharedPreference.getInt(THIRD_TIME_HOUR,M_THIRD_TIME_HOUR);

          int  firstTimeMinute = sharedPreference.getInt(FIRST_TIME_MINUTE,M_CONSTANTE_MINUTE);
          int  secondTimeMinute = sharedPreference.getInt(SECOND_TIME_MINUTE,M_CONSTANTE_MINUTE);
          int  thirdTimeMinute = sharedPreference.getInt(THIRD_TIME_MINUTE,M_CONSTANTE_MINUTE);

            NotifyBuilder notifyBuilder=new NotifyBuilder(context);
            notifyBuilder.makeNotification(notifyBuilder.getNotification(context.getString(R.string.notification_content_main)), FIRST_ID,firstHour, firstTimeMinute);
            notifyBuilder.makeNotification(notifyBuilder.getNotification(context.getString(R.string.notification_content_main)), SECOND_ID, secondHour, secondTimeMinute);
            notifyBuilder.makeNotification(notifyBuilder.getNotification(context.getString(R.string.notification_content_main)), THIRD_ID, thirdHour, thirdTimeMinute);
        }
    }
}
