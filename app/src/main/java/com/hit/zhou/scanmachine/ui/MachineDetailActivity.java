package com.hit.zhou.scanmachine.ui;

import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.common.utils.EasyRefreshLayout;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MachineDetailActivity extends AppCompatActivity implements MachineDetailConstruct.detailIView {
    private LineChart lineChart;
    private MakeLineChart makeLineChart;
    private Toolbar toolbar;
    private EasyRefreshLayout easyRefreshLayout;

    private MachineDetailPresenter machineDetailPresenter;

    private AVLoadingIndicatorView loadingIndicatorView;
    private TextView score;
    private TextView time;

    private boolean isLoading = true;

    private CircleImageView image1;
    private CircleImageView image2;
    private CircleImageView image3;
    private CircleImageView image4;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);
        lineChart = findViewById(R.id.lineChart);
        makeLineChart = new MakeLineChart(lineChart);
        toolbar = findViewById(R.id.detail_toolbar);
        easyRefreshLayout = findViewById(R.id.easyRefreshLayout);
        loadingIndicatorView = findViewById(R.id.detail_loading);
        score = findViewById(R.id.textView);
        time = findViewById(R.id.time);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);

        Picasso.get().load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542445129942&di=0b23554c4e8c711f7962f3b278c69f2c&imgtype=0&src=http%3A%2F%2Fimg.car0575.com%2Fupload%2F2011-9%2F1317177299952.jpg")
                .placeholder(R.color.white)
                .error(R.color.white)
                .into(image1);
        Picasso.get().load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543040202&di=aa6e8063eb91d1020784785e83a9f54a&imgtype=jpg&er=1&src=http%3A%2F%2Ffile1.dzsc.com%2Fproduct%2F13%2F01%2F18%2F327925_163138570.jpg")
                .placeholder(R.color.white)
                .error(R.color.white)
                .into(image2);
        Picasso.get().load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542445524017&di=bef4347a716a2221f3369f16dd48ebbe&imgtype=0&src=http%3A%2F%2Fimg007.hc360.cn%2Fhb%2FGu38529855829fdd1527ddf8071289C3d2.jpg")
                .placeholder(R.color.white)
                .error(R.color.white)
                .into(image3);
        Picasso.get().load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543040281&di=882c5d3c58f18778bf4bb7e21b1a8047&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.dgzhonghao.com%2Fupload%2Fimage%2F20131022%2F2013102211160256256.jpg")
                .placeholder(R.color.white)
                .error(R.color.white)
                .into(image4);
        easyRefreshLayout.setVisibility(View.GONE);
        loadingIndicatorView.show();
        easyRefreshLayout.setOnRefreshListener(new EasyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                machineDetailPresenter.requestRefresh("1");
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        machineDetailPresenter = new MachineDetailPresenter(this,this);
        machineDetailPresenter.onCreate(savedInstanceState);



    }

    @Override
    public void onStart(){
        super.onStart();
        machineDetailPresenter.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
        machineDetailPresenter.onStop();
    }


    @Override
    public void setPresenter(IPresenter presenter) {
        this.machineDetailPresenter = (MachineDetailPresenter) presenter;
    }

    @Override
    public IPresenter getPresenter() {
        return machineDetailPresenter;
    }

    @Override
    public void bindServiceCallback(boolean isBind) {
        if(isBind){
            machineDetailPresenter.requestRefresh("1");
        }
    }

    @Override
    public void update(String score,String time,ArrayList<MachineFormData> list){
        if(isLoading)
            loadingIndicatorView.hide();
        if(easyRefreshLayout.getVisibility() == View.GONE)
            easyRefreshLayout.setVisibility(View.VISIBLE);
        this.score.setText(score);
        String timeee = "更新于"+time+"秒前";
        this.time.setText(timeee);
        easyRefreshLayout.setRefreshing(false);
        makeLineChart.showLineChart(list);
    }

}
