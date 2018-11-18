package com.hit.zhou.scanmachine.ui.login;

import android.os.Message;

import com.hit.zhou.scanmachine.common.IPresenter;

/**
 * Created by zhou on 2018/11/7.
 */

public interface LoginIPresenter extends IPresenter {
    void checkUserIsExist(String phone);
    void checkUserAndPassword(String phone,String password);
}
