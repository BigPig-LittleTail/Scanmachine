package com.hit.zhou.scanmachine.common.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.zhou.scanmachine.common.MyMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhou on 2018/11/18.
 */

public abstract class MessageListCallBack implements Callback {
    @Override
    public void onFailure(Call call, IOException e) {
        onFailure();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //:TODO json解析
//        String result = response.body().string();
//        Type type = new TypeToken<ArrayList<MyMessage>>(){}.getType();
//        Gson gson = new Gson();
//        ArrayList<MyMessage> list = gson.fromJson(result,type);
//        for(int i = 0;i<list.size();i++){
//            Log.e("list",list.get(i).toString());
//        }
//        onResponse();
    }

    public abstract void onResponse(ArrayList<MyMessage> machines,int itemPosition);
    public abstract void onFailure();
}
