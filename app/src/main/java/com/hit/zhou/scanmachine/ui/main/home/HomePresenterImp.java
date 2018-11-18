package com.hit.zhou.scanmachine.ui.main.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.NetService;

/**
 * Created by zhou on 2018/11/17.
 */

public class HomePresenterImp implements HomePresenter {
    private Context context;
    private HomeIView homeIView;

    public HomePresenterImp(Context context, HomeIView homeIView){
        this.context = context;
        this.homeIView = homeIView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IView getView() {
        return homeIView;
    }

    @Override
    public void requestMachineInfo(Messenger service,String phone) {
        Message message = Message.obtain();
        message.what = NetService.MSG_REQUEST_HOME_VIEW;
        Bundle bundle = new Bundle();
        bundle.putString(NetService.PARAM_USERNAME,phone);
        message.setData(bundle);
        try{
            service.send(message);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
