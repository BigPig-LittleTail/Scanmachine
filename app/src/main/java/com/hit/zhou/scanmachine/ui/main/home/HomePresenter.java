package com.hit.zhou.scanmachine.ui.main.home;

import android.os.Messenger;

import com.hit.zhou.scanmachine.common.IPresenter;

/**
 * Created by zhou on 2018/11/17.
 */

public interface HomePresenter extends IPresenter {
    void requestMachineInfo(Messenger service,String phone);
}
