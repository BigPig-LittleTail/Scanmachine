package com.hit.zhou.scanmachine.common;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.hit.zhou.scanmachine.common.http.OkHttpUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by zhou on 2018/11/4.
 */

public class MyApplication extends Application {
//    public static final String SERVER_URL = "http://www.baidu.com";
    public static final String SERVER_URL = "http://172.20.75.104:5000/";
    public static final String TAG = "MyApplication";
    private static Context applicationContext;
    public static String phone = "";
    @Override
    public void onCreate(){
        super.onCreate();
        Log.e(TAG,"onCreate");
        applicationContext = getApplicationContext();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L,TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtil.getInstance().init(okHttpClient,SERVER_URL);
        Intent intent = new Intent(applicationContext,NetService.class);
        startService(intent);
    }

    public static Context getInstance(){
        return applicationContext;
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        Intent intent = new Intent(applicationContext,NetService.class);
        stopService(intent);
    }
}
