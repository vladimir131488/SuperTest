package com.example.supertest;


import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";
    Intent intentService;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentService = new Intent(this,MyService.class);
        runService();
    }

    public void runService() {
        boolean working = isMyServiceRunning(MyService.class);
        if (!working) {
            startService(intentService);
        } else {
            stopService(intentService);
            startService(intentService);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}