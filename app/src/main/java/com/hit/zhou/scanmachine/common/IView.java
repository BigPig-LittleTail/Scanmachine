package com.hit.zhou.scanmachine.common;

/**
 * Created by zhou on 2018/11/4.
 */

public interface IView {
    void setPresenter(IPresenter presenter);
    IPresenter getPresenter();
}
