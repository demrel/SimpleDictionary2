package com.example.bv.simpledictionary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.bv.simpledictionary.Activities.DailyWord;

import java.util.Calendar;

import static android.R.attr.id;



public class NotifyBuilder  {
    private Context mContext;
    private long mTime;

    public NotifyBuilder(Context context){
        this.mContext=context;
    }
    public void setTime(long time){
        this.mTime=time;
    }

    public Calendar calendar(int hour,int minute){
        Calendar calendar=Calendar.getInstance();
        Calendar cal=Calendar.getInstance();
        calendar.set(Calendar.YEAR,cal.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH,cal.get(Calendar.MONTH));

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
    public void makeNotification(Notification notificationm, int id, int hour,int minute){
        Intent notification =new Intent(mContext,NotificationPublisherReciver.class);
        notification.putExtra(NotificationPublisherReciver.NOTIFICATION_ID,id);
        notification.putExtra(NotificationPublisherReciver.NOTIFICATION,notificationm);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(mContext,id,notification,PendingIntent.FLAG_UPDATE_CURRENT);

      Calendar cal= calendar(hour,minute);
        if (cal.getTimeInMillis()<System.currentTimeMillis()){
            cal.add(Calendar.DATE,1);
        }
        AlarmManager alarmManager= (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() ,AlarmManager.INTERVAL_DAY,pendingIntent);

        }
    public Notification getNotification(String content ){
        Bitmap bitmap= BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_logo_notification);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(mContext)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setContentTitle(mContext.getString(R.string.notification_content))
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[] { 1000, 1000})
                .setWhen(mTime);
        Intent resultIntent=new Intent(mContext,DailyWord.class);

        TaskStackBuilder stackBuilder=TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resunltPendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resunltPendingIntent);
        return  mBuilder.build();
    }
}
