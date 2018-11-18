package com.hit.zhou.scanmachine.ui.main.message;

import android.os.Messenger;

import com.hit.zhou.scanmachine.common.IPresenter;

/**
 * Created by zhou on 2018/11/18.
 */

public interface MessagePresenter extends IPresenter {
    void requestMessage(Messenger service,int type,String phone);
}
