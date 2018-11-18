package com.hit.zhou.scanmachine.ui.main;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zhou on 2018/11/18.
 */

public class MyPagerAdapter extends PagerAdapter {
    public ArrayList<View> list;
    public ArrayList<String> titleList;

    public MyPagerAdapter(ArrayList<View> list,ArrayList<String> titleList){
        this.titleList = titleList;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public CharSequence getPageTitle(int position){
        return titleList == null ? null:titleList.get(position);
    }
}
