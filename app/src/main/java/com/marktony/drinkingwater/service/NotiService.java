package com.marktony.drinkingwater.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

import com.marktony.drinkingwater.receiver.AlarmReceiver;
import com.marktony.drinkingwater.R;

import java.util.Calendar;

public class NotiService extends Service {

    public NotiService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //只需要考虑什么时候发起通知，而不用考虑是否需要发出通知，通知的发起将由MainActivity判断
        //判断用户是否设置了手动提醒

        new Thread(new Runnable() {
            @Override
            public void run() {

                Calendar calendar = Calendar.getInstance();
                SharedPreferences.Editor editor = getSharedPreferences("UserData", Context.MODE_PRIVATE).edit();

                ContentResolver cv = getBaseContext().getContentResolver();
                String strTimeFormat = android.provider.Settings.System.getString(cv, android.provider.Settings.System.TIME_12_24);

                if(strTimeFormat != null && strTimeFormat.equals("24") && (calendar.get(Calendar.MINUTE) == 0)){
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 10){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 11){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 12){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 13){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 14){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 15){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 16){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 17){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 18){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 19){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 20){
                        noti();
                    }
                    if (calendar.get(Calendar.HOUR_OF_DAY) == 21){
                        noti();
                    }
                    if(calendar.get(Calendar.HOUR_OF_DAY) == 0){
                        editor.putInt("Progress",0);
                        editor.commit();
                    }
                }


                if((strTimeFormat != null) && (strTimeFormat.equals("12")) && (calendar.get(Calendar.MINUTE) == 0)){
                    if (calendar.get(Calendar.AM_PM) == Calendar.AM){

                        //上午
                        if (calendar.get(Calendar.HOUR_OF_DAY) == 10){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR_OF_DAY) == 11){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR_OF_DAY) == 0){
                            noti();
                        }

                    }else if (calendar.get(Calendar.AM_PM) == Calendar.PM){
                        if (calendar.get(Calendar.HOUR) == 1){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 2){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 3){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 4){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 5){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 6){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 7){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 8){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 9){
                            noti();
                        }
                        if (calendar.get(Calendar.HOUR) == 0){
                            editor.putInt("Progress",0);
                            editor.commit();
                        }
                    }
                }

            }
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int oneSecond =  59 * 1000;//1000表示1秒
        long triggerAtTime = SystemClock.elapsedRealtime() + oneSecond;
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    public void noti(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.noti_title));
        /*builder.setContentInfo(getString(R.string.noti_info));*/
        if(sharedPreferences.getInt("Progress", 0) == 0){
            builder.setContentText(getString(R.string.noti_no_progress));
        }else{
            builder.setContentText(getString(R.string.noti_text_part1) + sharedPreferences.getInt("Progress", 0) + getString(R.string.noti_text_part2));
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setWhen(System.currentTimeMillis());//显示通知时间

        //Intent i = new Intent(this,MainActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(this,0,i,0);
        //builder.setContentIntent(pi);
        //设置PendingIntent为打开MainActivity，问题是当点击了notification之后，
        // MainActivity会重新启动NotiService,此时仍然处在1分钟之内，通知在再次发送，造成通知的重复



        //builder.setOngoing(true);设置是否一直显示
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);//设置通知的声音、是否打开通知灯通知灯、是否振动为默认值
        manager.notify(1, builder.build());
    }
}
