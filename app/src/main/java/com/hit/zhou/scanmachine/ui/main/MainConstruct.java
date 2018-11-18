package com.hit.zhou.scanmachine.ui.main;

import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.ui.main.home.HomeIView;
import com.hit.zhou.scanmachine.ui.main.message.MessageViewIView;

/**
 * Created by zhou on 2018/11/15.
 */

public interface MainConstruct {
    interface MainPresenter extends IPresenter{
        void initThreeItemView(HomeIView homeIView, MessageViewIView messageViewIView);
    }

    interface MainIView extends IView{
        void bindServiceCallback(boolean isBind);
    }

}
