package com.hit.zhou.scanmachine.ui.main.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.common.Machine;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/17.
 */

public class HomeView extends FrameLayout implements HomeIView{
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private AVLoadingIndicatorView avLoadingIndicatorView;

    private HomePresenterImp mainPresenter;
    private Context context;

    public HomeView(Context context){
        this(context,null);
    }

    public HomeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context,R.layout.home_view,this);
        this.context = context;
        init();
    }

    private void init(){
        mainPresenter = new HomePresenterImp(context,this);
        toolbar = findViewById(R.id.home_view_toolbar);
        if(context instanceof AppCompatActivity)
        {
            ((AppCompatActivity)context).setSupportActionBar(toolbar);
        }
        recyclerView = findViewById(R.id.testRecyclerView);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        avLoadingIndicatorView = findViewById(R.id.home_view_loading);
        avLoadingIndicatorView.show();
    }


    @Override
    public void setPresenter(IPresenter presenter) {
        this.mainPresenter = (HomePresenterImp) presenter;
    }

    @Override
    public IPresenter getPresenter() {
        return mainPresenter;
    }


    @Override
    public void showMachineInfo(ArrayList<Machine> machines) {
        recyclerView.setAdapter(new MachineAdapter(machines));
        recyclerView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.hide();
    }
}
