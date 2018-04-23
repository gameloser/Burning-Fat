package cs.dal.a4176_project;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *  @reference
 *  PhilJay:MPAndroidChart at github
 * */
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class ProgressFragment extends Fragment {

    DatabaseManager databaseManager;
    BarChart barChart;
    TextView textView;
    private static int TV_ID = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseManager = new DatabaseManager(getActivity());
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Cursor cursor = databaseManager.getAllSportRecordsOrderByDate();
        float barWidth;
        barWidth = 0.9f;
        //create text view and barchart
        textView = view.findViewById(R.id.bdaresult);
        barChart = view.findViewById(R.id.barChart);
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(true);
        // init array to store day
        final String[] day = new String[]{" "," "," "," "," "," "," "," "};
        // array list bar entry
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        cursor.moveToLast();
        // traverse the array for values
        for(int i = 1;i<=cursor.getCount();i++){
            day[i] = cursor.getString(3);
            String[] parts = day[i].split("-", 4);
            day[i]  = parts[2];
            float f = cursor.getFloat(9);
            barEntries.add(new BarEntry(i, f));
            cursor.move(-1);
        }

        // init a bar data set
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        int pre_color = Color.rgb(0, 204, 204);
        int curr_color = Color.rgb(153, 255, 204);
        barDataSet.setColors(pre_color, pre_color, pre_color, pre_color, pre_color, pre_color, curr_color);
        // pass bar data set into bar data
        final BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(16f);
        barData.setValueFormatter(new LargeValueFormatter());
        // set properties of bar chart
        barChart.setData(barData);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(8);
        barChart.getData().setHighlightEnabled(true);
        barChart.getLegend().setEnabled(false);

        //X-axis properties
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(0.2f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setTextSize(13f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(day));

        //Y-axis propreties
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(20f);
        //leftAxis.setAxisMinimum(0f);

        // add on select clicked event listener
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x=e.getX();
                float y=e.getY();
                int i = (int) x;
                String gender = "Male";
                String s = "";
                float bmi = 0.0f;
                float height = 181.0f / 100;
                bmi = y / (height * height);
                s +=  "Your BMI on " + day[i] + " is " + String.format("%.2f", bmi) + "\n";
                s += "Your Status is " + bmiStatus(gender, bmi, view);
                textView.setText(s);
            }

            @Override
            public void onNothingSelected() {}
        });
    }

    // calculate bmi values and it will change the corresponding background with the value of bmi.
    public String bmiStatus (String gender, float bmi, View view) {
        int highlight_color = Color.rgb(204, 255, 255);
        if (gender.equals("Male")) {
            if (bmi < 20.7) {
                if (TV_ID != 0) {
                    TextView m = view.findViewById(TV_ID);
                    m.setBackgroundColor(Color.TRANSPARENT);
                    m = view.findViewById(R.id.m1);
                    m.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.m1);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.m1;
                return "Underweight";
            }
            else if (bmi < 26.4) {
                if (TV_ID != 0) {
                    TextView m = view.findViewById(TV_ID);
                    m.setBackgroundColor(Color.TRANSPARENT);
                    m = view.findViewById(R.id.m2);
                    m.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.m2);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.m2;
                return "Normal";
            }
            else if (bmi < 27.8) {
                if (TV_ID != 0) {
                    TextView m = view.findViewById(TV_ID);
                    m.setBackgroundColor(Color.TRANSPARENT);
                    m = view.findViewById(R.id.m3);
                    m.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.m3);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.m3;
                return "Marginally overweight";
            }
            else if (bmi < 31.1) {
                if (TV_ID != 0) {
                    TextView m = view.findViewById(TV_ID);
                    m.setBackgroundColor(Color.TRANSPARENT);
                    m = view.findViewById(R.id.m4);
                    m.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.m4);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.m4;
                return "Overweight";
            }
            else {
                if (TV_ID != 0) {
                    TextView m = view.findViewById(TV_ID);
                    m.setBackgroundColor(Color.TRANSPARENT);
                    m = view.findViewById(R.id.m5);
                    m.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.m5);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.m5;
                return "Obese";
            }
        }
        else {
            if (bmi < 19.1) {
                if (TV_ID != 0) {
                    TextView f = view.findViewById(TV_ID);
                    f.setBackgroundColor(Color.TRANSPARENT);
                    f = view.findViewById(R.id.f1);
                    f.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.f1);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.f1;
                return "Underweight";
            }
            else if (bmi < 25.8) {
                if (TV_ID != 0) {
                    TextView f = view.findViewById(TV_ID);
                    f.setBackgroundColor(Color.TRANSPARENT);
                    f = view.findViewById(R.id.f2);
                    f.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.f2);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.f2;
                return "Normal";
            }
            else if (bmi < 27.3) {
                if (TV_ID != 0) {
                    TextView f = view.findViewById(TV_ID);
                    f.setBackgroundColor(Color.TRANSPARENT);
                    f = view.findViewById(R.id.f3);
                    f.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.f3);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.f3;
                return "Marginally overweight";
            }
            else if (bmi < 32.3) {
                if (TV_ID != 0) {
                    TextView f = view.findViewById(TV_ID);
                    f.setBackgroundColor(Color.TRANSPARENT);
                    f = view.findViewById(R.id.f4);
                    f.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.f4);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.f4;
                return "Overweight";
            }
            else {
                if (TV_ID != 0) {
                    TextView f = view.findViewById(TV_ID);
                    f.setBackgroundColor(Color.TRANSPARENT);
                    f = view.findViewById(R.id.f5);
                    f.setBackgroundColor(highlight_color);
                }
                else {
                    TextView m = view.findViewById(R.id.f5);
                    m.setBackgroundColor(highlight_color);
                }
                TV_ID = R.id.f5;
                return "Obese";
            }
        }
    }


}