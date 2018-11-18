package com.hit.zhou.scanmachine.common.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okhttp的封装
 * Created by zhou on 2018/11/3.
 */

public class OkHttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String ERROR_INIT_CLIENT_WITH_NULL = "Your param client is null";
    private static final String ERROR_INIT_URL_WITH_NULL = "Your param url is null";

    private static OkHttpUtil singleOkHttpUtil;
    private OkHttpClient httpClient;
    private String serverUrl;

    public static OkHttpUtil getInstance(){
        if(singleOkHttpUtil == null){
            synchronized (OkHttpUtil.class){
                if(singleOkHttpUtil == null)
                    singleOkHttpUtil = new OkHttpUtil();
            }
        }
        return singleOkHttpUtil;
    }

    private OkHttpUtil(){

    }

    public synchronized void init(OkHttpClient InitHttpClient,String url){
        if(InitHttpClient == null){
            throw new IllegalArgumentException(ERROR_INIT_CLIENT_WITH_NULL);
        }
        if(this.httpClient == null){
            this.httpClient = InitHttpClient;
        }

        if(url == null){
            throw new IllegalArgumentException(ERROR_INIT_URL_WITH_NULL);
        }
        if(this.serverUrl == null)
        {
            this.serverUrl = url;
        }
    }

    public void get() throws IOException{
        Request request = new Request.Builder()
                .url(serverUrl)
                .build();
        Response response = httpClient.newCall(request).execute();
    }

    public void post(String json, String action, final Callback callback) throws IOException{
        String url = serverUrl;
        if(!action.equals("")){
            url = url + action;
        }
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .addHeader("Connection", "close")
                .url(url)
                .post(requestBody)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(callback);
    }


}
