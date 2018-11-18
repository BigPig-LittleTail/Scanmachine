package com.hit.zhou.scanmachine.common.http;

/**
 * Created by zhou on 2018/11/15.
 */
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.zhou.scanmachine.common.Machine;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public abstract class MachineListCallBack implements Callback{
    @Override
    public void onFailure(Call call, IOException e) {
        onFailure();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();
        Log.e("result",result);
        Type type = new TypeToken<ArrayList<Machine>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<Machine> list = gson.fromJson(result,type);
//        for(int i = 0;i<list.size();i++){
//            Log.e("list",list.get(i).toString());
//        }
        onResponse(list);
    }

    public abstract void onResponse(ArrayList<Machine> machines);
    public abstract void onFailure();
}
