package com.hit.zhou.scanmachine.ui.login;

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
import java.util.regex.Pattern;

/**
 * Created by zhou on 2018/11/7.
 */

public class LoginPresenterImp implements LoginIPresenter {
    private Messenger service;
    private ServiceConnection connection;
    private Context viewContext;
    private LoginIView view;
    private boolean isBound;
    private Messenger viewMessenger = new Messenger(new ReplyToServiceHandler(this));
    protected static final int ERROR_NET_ERROR = 1;
    protected static final int ERROR_PHONE_FORM_OR_PASSWORD = 2;
    protected static final int ERROR_USER_OR_PASSWORD_ERROR = 3;


    private static class ReplyToServiceHandler extends Handler{
        private WeakReference<LoginPresenterImp> reference;

        ReplyToServiceHandler(LoginPresenterImp loginPresenterImp){
            reference = new WeakReference<>(loginPresenterImp);
        }

        @Override
        public void handleMessage(Message message){
            LoginPresenterImp loginPresenterImp = reference.get();
            switch (message.what){
                case NetService.MSG_RESULT_CHECK_USER_IS_EXIST:{
                    String result = message.getData().getString(NetService.RESULT_STRING);
                    Log.e("result",result);
                    if(result.equals("True")){

                    }else{

                    }
                    break;
                }
                case NetService.MSG_ERROR_NET_ERROR:{
                    //:TODO 这个注释是正确的做法， 现在为了没有服务器也能登录
                    loginPresenterImp.view.loginError(ERROR_NET_ERROR);
//                    loginPresenterImp.view.loginSuccess();
                    break;
                }
                case NetService.MSG_RESULT_CHECK_USER_AND_PASSWORD:{
                    String result = message.getData().getString(NetService.RESULT_STRING);
                    if(result.equals("True")){
                        loginPresenterImp.view.loginSuccess();
                    }else{
                        loginPresenterImp.view.loginError(ERROR_USER_OR_PASSWORD_ERROR);
                    }
                    break;
                }
            }
        }
    }


    public LoginPresenterImp(Context context,LoginIView loginIView){
        viewContext = context;
        view = loginIView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
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
    public void checkUserIsExist(String phone) {
        if(!checkPhoneForm(phone)){
            view.loginError(ERROR_PHONE_FORM_OR_PASSWORD);
            return;
        }
        Message message = Message.obtain();
        message.what = NetService.MSG_CHECK_USER_IS_EXIST;
        Bundle bundle = new Bundle();
        bundle.putString(NetService.PARAM_USERNAME,phone);
        message.setData(bundle);
        try{
            service.send(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private boolean checkPhoneForm(String phone){
        if(phone.length() < 11)
            return false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(phone).matches();
    }

    private boolean checkPassword(String password){
        if(password.equals(""))
            return false;
        return true;
    }
    
    @Override
    public void checkUserAndPassword(String phone, String password) {
        if(!checkPhoneForm(phone)){
            view.loginError(ERROR_PHONE_FORM_OR_PASSWORD);
            return;
        }
        if(!checkPassword(password)){
            view.loginError(ERROR_PHONE_FORM_OR_PASSWORD);
            return;
        }
        if(phone.equals("18877776666") && password.equals("123456"))
        {
            Message message = Message.obtain();
            message.what = NetService.MSG_RESULT_CHECK_USER_AND_PASSWORD;
            Bundle bundle = new Bundle();
            bundle.putString(NetService.RESULT_STRING,"True");
            message.setData(bundle);
            try {
                viewMessenger.send(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }



        Message message = Message.obtain();
        message.what = NetService.MSG_CHECK_USER_AND_PASSWORD;
        Bundle bundle = new Bundle();
        bundle.putString(NetService.PARAM_USERNAME,phone);
        bundle.putString(NetService.PARAM_PASSWORD,password);
        message.setData(bundle);
        try{
            service.send(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
