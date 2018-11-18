package com.hit.zhou.scanmachine.ui.main.message;

import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.MyMessage;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/18.
 */

public interface MessageViewIView extends IView {
    void showMessageRecyclerView(ArrayList<MyMessage> list,int itemPosition);
}
