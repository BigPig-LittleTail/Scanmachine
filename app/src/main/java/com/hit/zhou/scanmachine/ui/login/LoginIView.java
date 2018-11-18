package com.hit.zhou.scanmachine.ui.login;

import com.hit.zhou.scanmachine.common.IView;

/**
 * Created by zhou on 2018/11/8.
 */

public interface LoginIView extends IView {
    void bindServiceCallback(boolean isBind);
    void loginError(int errorType);
    void loginSuccess();
}
