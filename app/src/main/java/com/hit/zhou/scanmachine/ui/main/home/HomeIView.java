package com.hit.zhou.scanmachine.ui.main.home;

import com.hit.zhou.scanmachine.common.IView;
import com.hit.zhou.scanmachine.common.Machine;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/17.
 */

public interface HomeIView extends IView {
    void showMachineInfo(ArrayList<Machine> machines);
}
