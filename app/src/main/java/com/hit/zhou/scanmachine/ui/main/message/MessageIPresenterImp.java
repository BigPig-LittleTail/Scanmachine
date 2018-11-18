package com.hit.zhou.scanmachine.ui.main.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;

import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.NetService;

/**
 * Created by zhou on 2018/11/18.
 */

public class MessageIPresenterImp implements MessagePresenter {
    private Context context;
    private MessageViewIView messageViewIView;
    public MessageIPresenterImp(Context context,MessageViewIView messageViewIView){
        this.context = context;
        this.messageViewIView = messageViewIView;
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
        return messageViewIView;
    }

    @Override
    public void requestMessage(Messenger service, int type, String phone) {
        Message message = Message.obtain();
        message.what = NetService.MSG_REQUEST_MESSAGE_LIST;
        Bundle bundle = new Bundle();
        bundle.putString(NetService.PARAM_USERNAME,phone);
        bundle.putString(NetService.MESSAGE_LIST_TYPE,Integer.toString(type));
        message.setData(bundle);
        try {
            service.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
