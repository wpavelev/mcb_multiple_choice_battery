package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;
import com.wpavelev.mcb_multiplechoicebattery.R;
import com.wpavelev.mcb_multiplechoicebattery.Data.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEnde1 extends AppCompatActivity {

    public final static String INTENT_KEY_TESTID = "testId";
    Test test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ende1);

        Intent intent = getIntent();
        int testId = intent.getIntExtra(INTENT_KEY_TESTID,0);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        test = db.getTest(testId);



        PieChart chart = findViewById(R.id.chart);
        chart.setHoleRadius(0f);
        chart.setTransparentCircleRadius(0f);

        List<PieEntry> entries1 = new ArrayList<>();


        float correct = (float) test.getCorrect();
        float wrong = (float) test.getWrong();

        entries1.add(new PieEntry(correct, "Correct"));
        entries1.add(new PieEntry(wrong, "Wrong"));

        PieDataSet set1 = new PieDataSet(entries1, "");

        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.green));
        colors.add(getResources().getColor(R.color.red));

        set1.setColors(colors);
        PieData data = new PieData(set1);

        chart.setData(data);
        chart.invalidate();


    }


    public void onClickNext(View view) {
        Intent intent = new Intent(getApplicationContext(), TestEnde2.class);
        intent.putExtra(TestEnde2.INTENT_KEY_TESTID, test.getTestId());
        startActivity(intent);
    }

}
