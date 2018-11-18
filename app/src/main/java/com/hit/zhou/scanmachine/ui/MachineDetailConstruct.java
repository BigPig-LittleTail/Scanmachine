package com.hit.zhou.scanmachine.ui;

import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.Machine;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/17.
 */

public interface MachineDetailConstruct {
    interface detailIPresenter extends com.hit.zhou.scanmachine.common.IPresenter{
        void requestRefresh(String machineId);
    }

    interface detailIView extends com.hit.zhou.scanmachine.common.IView{
        void bindServiceCallback(boolean isBind);
        void update(String score,String time,ArrayList<MachineFormData> list);
    }

}
