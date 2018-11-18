package com.hit.zhou.scanmachine.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hit.zhou.scanmachine.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 2018/11/16.
 */

public class MakeLineChart {
    private XAxis xAxis;
    private YAxis yAxis;
    private Legend legend;
    private LineChart lineChart;

    public MakeLineChart(LineChart lineChart){
        this.lineChart = lineChart;
        this.xAxis = lineChart.getXAxis();
        this.yAxis = lineChart.getAxisLeft();
        this.legend = lineChart.getLegend();
        this.legend.setEnabled(false);
        initChart();
    }

    public void showLineChart(ArrayList<MachineFormData> list){
        setXlabels(list);
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MachineFormData data = list.get(i);
            Entry entry = new Entry(i, (float) data.getScore());
            entries.add(entry);
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        initLineDataSet(lineDataSet);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);


    }


    private void initLineDataSet(LineDataSet lineDataSet) {
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);

        lineDataSet.setColor(lineChart.getResources().getColor(R.color.old_orange));
        lineDataSet.setLineWidth(1.5f);



        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        Drawable drawable = lineChart.getResources().getDrawable(R.drawable.fade_orange);
        lineDataSet.setFillDrawable(drawable);
        lineDataSet.setFormLineWidth(2f);
        lineDataSet.setFormSize(1.5f);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }



    private void setXlabels(ArrayList<MachineFormData> list){
        class MyXformat implements IAxisValueFormatter {
            ArrayList<MachineFormData> list;
            public MyXformat(ArrayList<MachineFormData> list){
                this.list = list;
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return list.get((int) value % list.size()).getDate();
            }
        }
        xAxis.setValueFormatter(new MyXformat(list));
    }

    private void initChart() {
        /***图表设置***/
        //是否展示网格线
        lineChart.setBackgroundColor(Color.WHITE);

        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);

        lineChart.setDoubleTapToZoomEnabled(false);

        //设置XY轴动画效果
//        lineChart.animateY(500);
        lineChart.animateX(500);


        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        /***XY轴的设置***/

        lineChart.getAxisRight().setEnabled(false);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(lineChart.getResources().getColor(R.color.colorAccent));
//        xAxis.setGridColor(R.color.gray);

        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7,false);

        yAxis.setDrawGridLines(true);
        yAxis.enableGridDashedLine(10f,10f,0f);
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
//        yAxis.setGridColor(R.color.gray);

        /***折线图例 标签 设置***/

    }

}
