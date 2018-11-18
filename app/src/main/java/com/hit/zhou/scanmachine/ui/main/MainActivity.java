package com.hit.zhou.scanmachine.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.ui.main.home.HomeView;
import com.hit.zhou.scanmachine.ui.main.message.MessageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainConstruct.MainIView {
    private MainConstruct.MainPresenter mainPresenter;
    private MyViewPager viewPager;
    private HomeView homeView;
    private MessageView messageView;
    private ArrayList<View> allView;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_machine:
                    viewPager.setCurrentItem(1,false);
                    return true;
                case R.id.navigation_trash:
                    viewPager.setCurrentItem(2,false);
                    return true;
                case R.id.navigation_message:
                    viewPager.setCurrentItem(0,false);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);


        allView = new ArrayList<>();
        mainPresenter = new MainPresenterImp1(this,this);
        homeView = new HomeView(this);
        messageView = new MessageView(this);
        allView.add(messageView);
        allView.add(homeView);
        allView.add(new MessageView(this));

        viewPager = findViewById(R.id.viewpager);
        mainPresenter.onCreate(savedInstanceState);

        MyPagerAdapter adapter = new MyPagerAdapter(allView,null);
        viewPager.setAdapter(adapter);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
    }

    @Override
    public void onStart(){
        super.onStart();
        mainPresenter.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        mainPresenter.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();

    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.mainPresenter = (MainConstruct.MainPresenter) presenter;
    }

    @Override
    public IPresenter getPresenter() {
        return this.mainPresenter;
    }

    @Override
    public void bindServiceCallback(boolean isBind) {
        if(isBind){
            mainPresenter.initThreeItemView(homeView,messageView);
        }
    }



}
