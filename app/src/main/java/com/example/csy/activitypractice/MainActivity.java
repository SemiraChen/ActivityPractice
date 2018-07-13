package com.example.csy.activitypractice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author CSY
 */
public class MainActivity extends AppCompatActivity {

    //     @BindView(R.id.Chart)
    BarChart qualityChart;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        setA(qualityChart);
//        setAttributes(qualityChart);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SpeechRecognitionActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 设置图标交互属性
     *
     * @param lineChart
     */
    private void setA(BarChart lineChart) {
        // 设置是否可以触摸
        lineChart.setTouchEnabled(false);
        // 是否可以拖拽
        lineChart.setDragEnabled(false);
        // 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleEnabled(false);
        //是否可以缩放 仅x轴
        lineChart.setScaleXEnabled(false);
        //是否可以缩放 仅y轴
        lineChart.setScaleYEnabled(false);
        //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setPinchZoom(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        lineChart.setDoubleTapToZoomEnabled(false);
        //能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setHighlightPerDragEnabled(false);
        //拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        lineChart.setDragDecelerationEnabled(false);
        //与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。
        lineChart.setDragDecelerationFrictionCoef(0.99f);
        // 不显示图例
        lineChart.getLegend().setEnabled(false);
        // 不显示描述
        lineChart.getDescription().setEnabled(false);
        // 没有数据的时候默认显示的文字
        lineChart.setNoDataText("暂无数据");
        lineChart.setNoDataTextColor(Color.GRAY);
        lineChart.setBorderColor(Color.BLUE);
        // 如果x轴label文字比较大，可以设置边距
        lineChart.setExtraRightOffset(25f);
        lineChart.setExtraBottomOffset(10f);
        lineChart.setExtraTopOffset(10f);
    }


    /**
     * 设置图标交互属性
     *
     * @param barChart
     */
    private void setAttributes(BarChart barChart) {
        qualityChart.setDrawBarShadow(false);
        //在图上画值
        qualityChart.setDrawValueAboveBar(true);

        qualityChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        qualityChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = barChart.getAxisLeft();
        // leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        // rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(barChart); // For bounds control
        barChart.setMarker(mv); // Set the marker to the chart

        setData(12, 50);
    }

    private void setData(int count, float range) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(1, 66, getResources().getDrawable(R.drawable.ic_launcher_background)));
        yVals1.add(new BarEntry(2, 88, getResources().getDrawable(R.drawable.ic_launcher_background)));
        yVals1.add(new BarEntry(3, 100, getResources().getDrawable(R.drawable.ic_launcher_background)));
        yVals1.add(new BarEntry(4, 20, getResources().getDrawable(R.drawable.ic_launcher_background)));
        yVals1.add(new BarEntry(5, 50, getResources().getDrawable(R.drawable.ic_launcher_background)));
        yVals1.add(new BarEntry(6, 70, getResources().getDrawable(R.drawable.ic_launcher_background)));


        BarDataSet set1;

        if (qualityChart.getData() != null &&
                qualityChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) qualityChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            qualityChart.getData().notifyDataChanged();
            qualityChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            //  set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //   data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            qualityChart.setData(data);
        }
    }

}
