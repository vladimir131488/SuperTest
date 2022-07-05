package com.example.supertest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private NotificationManager notificationManager;
    public static final int DEFAULT_NOTIFICATION_ID = 101;
    private static final String CHANNEL_ID = "1";
    Intent intentService;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("LOG", "Start Service :");
        notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        FindApp mr = new FindApp();
        new Thread(mr).start();
        return START_STICKY;
    }


    public void sendNotification(String Ticker,String Title,String Text) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(Title)
                        .setContentText(Text);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
        //startForeground(DEFAULT_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);

        stopSelf();

    }


    class FindApp implements Runnable {

        @Override
        public void run() {
                int quant = 0;
                final PackageManager pm = getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                quant = packages.size();
                while(true){
                    packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                    if(quant > packages.size()){
                        sendNotification("Ticker","Title","Deleted app");
                        Log.d("LOG", "Deleted package :");
                        quant = packages.size();
                        FindApp mr = new FindApp();
                        new Thread(mr).start();
                        break;
                    }
                    if(quant < packages.size()){
                        sendNotification("Ticker","Title","Install app");
                        Log.d("LOG", "Installed package :");
                        quant = packages.size();
                        FindApp mr = new FindApp();
                        new Thread(mr).start();
                        break;
                    }
                }
                for (ApplicationInfo packageInfo : packages) {
                    //All apps
                    Log.d("LOG", "Installed package :" + packageInfo.packageName);
                }
                try {
                    TimeUnit.SECONDS.sleep(2);
                    Log.d("LOG", "Installed package :");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

        void stop() {

        }
    }

}