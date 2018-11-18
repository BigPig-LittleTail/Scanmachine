package com.hit.zhou.scanmachine.common;

import android.os.Bundle;

/**
 * Created by zhou on 2018/11/4.
 */

public interface IPresenter {
    void onCreate(Bundle savedInstanceState);
    void onStart();
    void onStop();
    void onDestroy();
    IView getView();
}
