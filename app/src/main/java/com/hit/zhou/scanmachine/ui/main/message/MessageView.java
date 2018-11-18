package com.hit.zhou.scanmachine.ui.main.message;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.common.MyMessage;
import com.hit.zhou.scanmachine.ui.main.MyPagerAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/18.
 */

public class MessageView extends FrameLayout implements MessageViewIView{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private ArrayList<Boolean> initState;

    private AVLoadingIndicatorView avLoadingIndicatorView;

    private Context context;

    private MessagePresenter messagePresenter;

    public MessageView(Context context){
        this(context,null);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.message_view,this);
        this.context = context;
        init();
    }

    private void init(){
        messagePresenter = new MessageIPresenterImp(this.context,this);
        toolbar = findViewById(R.id.message_toolbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.message_viewpager);
        avLoadingIndicatorView = findViewById(R.id.message_view_loading);
        avLoadingIndicatorView.show();
        initState = new ArrayList<Boolean>(){{
            add(false);
            add(false);
            add(false);
        }
        };

        initRecyclerView();
        initViewPagerAndTabLayout();
    }

    private void initRecyclerView(){
        recyclerView1 = new RecyclerView(this.context);
        recyclerView2 = new RecyclerView(this.context);
        recyclerView3 = new RecyclerView(this.context);
        recyclerView1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView3.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        recyclerView3.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initViewPagerAndTabLayout(){
        ArrayList<View> recyclerViewArrayList = new ArrayList<>();
        recyclerViewArrayList.add(recyclerView1);
        recyclerViewArrayList.add(recyclerView2);
        recyclerViewArrayList.add(recyclerView3);
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("论坛");
        titleList.add("我发布的");
        titleList.add("提到我的");
        final MyPagerAdapter adapter = new MyPagerAdapter(recyclerViewArrayList,titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(!initState.get(tab.getPosition())){
                    avLoadingIndicatorView.show();
                }
                else {
                    avLoadingIndicatorView.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                if(!initState.get(tab.getPosition())){
//                    avLoadingIndicatorView.hide();
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        this.messagePresenter = (MessagePresenter) presenter;
    }

    @Override
    public IPresenter getPresenter() {
        return messagePresenter;
    }


    @Override
    public void showMessageRecyclerView(ArrayList<MyMessage> list, int itemPosition) {
        if(!initState.get(itemPosition)){
            initState.set(itemPosition,true);
            avLoadingIndicatorView.hide();
        }
        switch (itemPosition){
            case 0:
                recyclerView1.setAdapter(new MessageItemOneAdapter(list));
                break;
            case 1:
                recyclerView2.setAdapter(new MessageItemOneAdapter(list));
                break;
            case 2:
                recyclerView3.setAdapter(new MessageItemOneAdapter(list));
                break;
        }
    }
}
