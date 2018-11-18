package com.hit.zhou.scanmachine.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.NetService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhou on 2018/11/17.
 */

public class MachineDetailPresenter implements MachineDetailConstruct.detailIPresenter {
    private Messenger service;
    private ServiceConnection connection;
    private Context viewContext;
    private MachineDetailConstruct.detailIView view;
    private boolean isBound;
    private Messenger viewMessenger = new Messenger(new ReplyToServiceHandler(this));
    
    public MachineDetailPresenter(Context context,MachineDetailConstruct.detailIView iView){
        this.viewContext = context;
        this.view = iView;
    }

    private static class ReplyToServiceHandler extends Handler {
        private WeakReference<MachineDetailPresenter> reference;

        ReplyToServiceHandler(MachineDetailPresenter presenterImp){
            reference = new WeakReference<>(presenterImp);
        }

        @Override
        public void handleMessage(Message message){
            MachineDetailPresenter machineDetailPresenter = reference.get();
            switch (message.what){
                case NetService.MSG_ERROR_NET_ERROR:{
                    //:TODO 这个注释是正确的做法， 现在为了没有服务器也能登录
                    break;
                }
                case NetService.MSG_RESULT_REFRESH:
                    Random random = new Random();
                    int i = random.nextInt(20)+80;
                    int j = random.nextInt(2)+1;
                    ArrayList<MachineFormData> list = new ArrayList<>();
                    list.add(new MachineFormData("11-11",82f));
                    list.add(new MachineFormData("11-12",78f));
                    list.add(new MachineFormData("11-13",90f));
                    list.add(new MachineFormData("11-14",94f));
                    list.add(new MachineFormData("11-15",88f));
                    list.add(new MachineFormData("11-16",100f));
                    list.add(new MachineFormData("11-17",96f));
                    machineDetailPresenter.view.update(Integer.toString(i),Integer.toString(j),list);
                    break;
            }
        }
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service = new Messenger(iBinder);
                isBound = true;
                Message message = Message.obtain();
                message.what = NetService.REPLY_TO_SERVICE_CONNECTION;
                message.replyTo = viewMessenger;
                try {
                    service.send(message);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                view.bindServiceCallback(isBound);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                service = null;
                isBound = false;
                view.bindServiceCallback(false);
            }
        };
    }

    @Override
    public void onStart() {
        Intent intent = new Intent(viewContext,NetService.class);
        viewContext.bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        if(isBound){
            viewContext.unbindService(connection);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IView getView() {
        return view;
    }

    @Override
    public void requestRefresh(final String machineId) {
        //:TODO 这是假的请求，真正走网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Message message = Message.obtain();
                    message.what = NetService.MSG_RESULT_REFRESH;
                    viewMessenger.send(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
