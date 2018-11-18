package com.hit.zhou.scanmachine.common;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.hit.zhou.scanmachine.common.http.MachineListCallBack;
import com.hit.zhou.scanmachine.common.http.MessageListCallBack;
import com.hit.zhou.scanmachine.common.http.OkHttpUtil;
import com.hit.zhou.scanmachine.common.http.StringCallback;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/4.
 */

public class NetService extends Service {
    private final Messenger messenger = new Messenger(new IncomingHandler(this));
    private Messenger presenterMessenger;
    public static final int REPLY_TO_SERVICE_CONNECTION = 0;
    public static final int MSG_CHECK_USER_IS_EXIST  = 1;
    public static final int MSG_RESULT_CHECK_USER_IS_EXIST = 2;
    public static final int MSG_CHECK_USER_AND_PASSWORD = 4;
    public static final int MSG_RESULT_CHECK_USER_AND_PASSWORD = 5;
    public static final int MSG_ERROR_NET_ERROR= 3;
    public static final int MSG_REQUEST_HOME_VIEW = 6;
    public static final int MSG_RESULT_HOME_VIEW = 9;

    public static final int MSG_RESULT_REFRESH = 7;
    public static final int MSG_REQUEST_REFRESH = 8;

    public static final int MSG_REQUEST_MESSAGE_LIST = 10;
    public static final int MSG_RESULT_MESSAGE_LIST = 11;

    public static final String PARAM_USERNAME = "phonenumber";
    public static final String PARAM_PASSWORD = "password";
    public static final String RESULT_STRING = "string_result";
    public static final String RESULT_MACHINE_LIST = "machine_list";
    public static final String MESSAGE_LIST_TYPE = "message_type";
    public static final String MESSAGE_LIST = "message_list";

    public static final String ACTION_MESSAGE_LIST = "message_list";
    private static final String ACTION_CHECK_USER_IS_EXIST = "checkUserExist";
    private static final String ACTION_CHECK_USER_AND_PASSWORD = "login";
    private static final String ACTION_REQUEST_MACHINE_LIST = "basic_machineinfo";

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private static class IncomingHandler extends Handler{
        private final WeakReference<NetService> mReference;
        IncomingHandler(NetService service){
            mReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg){
            NetService service = mReference.get();
            switch (msg.what){
                case MSG_CHECK_USER_IS_EXIST:
                    service.checkUserIsExist(msg.getData().getString(PARAM_USERNAME));
                    break;
                case MSG_CHECK_USER_AND_PASSWORD:
                    service.checkUserAndPassword(
                            msg.getData().getString(PARAM_USERNAME),msg.getData().getString(PARAM_PASSWORD));
                    break;
                case MSG_REQUEST_HOME_VIEW:
                    service.requestMachineList(msg.getData().getString(PARAM_USERNAME));
                    break;
                case MSG_REQUEST_MESSAGE_LIST:
                    service.requestMessageList(msg.getData().getString(MESSAGE_LIST_TYPE),msg.getData().getString(PARAM_USERNAME));
                    break;
                case REPLY_TO_SERVICE_CONNECTION:
                    service.presenterMessenger = msg.replyTo;
                    break;
            }
        }
    }

    private void checkUserIsExist(String phone){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_USERNAME,phone);
            OkHttpUtil.getInstance().post(jsonObject.toString(), ACTION_CHECK_USER_IS_EXIST,
                    new StringCallback() {
                        @Override
                        public void onResponse(String result) {
                            Message message = Message.obtain();
                            message.what = MSG_RESULT_CHECK_USER_IS_EXIST;
                            Bundle bundle = new Bundle();
                            bundle.putString(RESULT_STRING,result);
                            message.setData(bundle);
                            try {
                                presenterMessenger.send(message);
                            }catch (RemoteException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure() {
                            Message message = Message.obtain();
                            message.what = MSG_ERROR_NET_ERROR;
                            try {
                                presenterMessenger.send(message);
                            }catch (RemoteException e){
                                e.printStackTrace();
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkUserAndPassword(String phone,String password){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_USERNAME,phone);
            jsonObject.put(PARAM_PASSWORD,password);

            OkHttpUtil.getInstance().post(jsonObject.toString(), ACTION_CHECK_USER_AND_PASSWORD,
                    new StringCallback() {
                        @Override
                        public void onResponse(String result) {
                            Message message = Message.obtain();
                            message.what = MSG_RESULT_CHECK_USER_AND_PASSWORD;
                            Bundle bundle = new Bundle();
                            bundle.putString(RESULT_STRING,result);
                            message.setData(bundle);
                            try {
                                presenterMessenger.send(message);
                            }catch (RemoteException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure() {
                            Message message = Message.obtain();
                            message.what = MSG_ERROR_NET_ERROR;
                            try {
                                presenterMessenger.send(message);
                            }catch (RemoteException e){
                                e.printStackTrace();
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void requestMachineList(String phone){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_USERNAME,phone);
            Log.e("phone",phone);

            OkHttpUtil.getInstance().post(jsonObject.toString(), ACTION_REQUEST_MACHINE_LIST, new MachineListCallBack() {
                @Override
                public void onResponse(ArrayList<Machine> machines) {
                    Message message = Message.obtain();
                    message.what = MSG_RESULT_HOME_VIEW;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RESULT_MACHINE_LIST,machines);
                    message.setData(bundle);
                    try {
                        presenterMessenger.send(message);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    Message message = Message.obtain();
                    message.what = MSG_ERROR_NET_ERROR;
                    try {
                        presenterMessenger.send(message);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestMessageList(String type,String phone){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_USERNAME,phone);

            OkHttpUtil.getInstance().post(jsonObject.toString(), ACTION_MESSAGE_LIST + type, new MessageListCallBack() {
                @Override
                public void onResponse(ArrayList<MyMessage> messages,int itemPosition) {
                    Message message = Message.obtain();
                    message.what = MSG_RESULT_MESSAGE_LIST;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MESSAGE_LIST,messages);
                    bundle.putInt(MESSAGE_LIST_TYPE,itemPosition);
                    message.setData(bundle);
                    try {
                        presenterMessenger.send(message);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    Message message = Message.obtain();
                    message.what = MSG_ERROR_NET_ERROR;
                    try {
                        presenterMessenger.send(message);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
