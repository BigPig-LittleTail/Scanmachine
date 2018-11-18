package com.hit.zhou.scanmachine.common.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhou on 2018/11/8.
 */

public abstract class StringCallback implements Callback{
    @Override
    public void onFailure(Call call, IOException e) {
        onFailure();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String josnResult = response.body().string();
        Gson gson = new Gson();
        String result = gson.fromJson(josnResult,String.class);
        Log.e("result",josnResult);
        onResponse(result);
    }

    public abstract void onResponse(String result);
    public abstract void onFailure();
}

