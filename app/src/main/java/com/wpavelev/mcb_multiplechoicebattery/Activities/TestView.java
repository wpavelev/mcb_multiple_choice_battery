package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wpavelev.mcb_multiplechoicebattery.CustomAdapter.CustomAdapter_TaskViewItems;
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;
import com.wpavelev.mcb_multiplechoicebattery.R;
import com.wpavelev.mcb_multiplechoicebattery.Data.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Listet die angefangenen Tests auf
 */

public class TestView extends AppCompatActivity {


    private static final String TAG = "TestView";
    List<Test> tests = new ArrayList<>();
    List<String> adapterItems = new ArrayList<>();
    CustomAdapter_TaskViewItems adapter;

    ListView listView;

    DatabaseHelper db;
    private Set<Integer> selectedItems;
    private List<Integer> correctItems;

    boolean answersShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        listView = findViewById(R.id.testList);

        db = new DatabaseHelper(getApplicationContext());


        adapterItems = new ArrayList<>();
        tests = new ArrayList<>(db.getTests());
        for (Test test : tests) {
            adapterItems.add(test.getDate() + " - " + db.getStackById(test.getStackId()).substring(0,5));

        }

        selectedItems = new HashSet<>();
        correctItems = new ArrayList<>();

        updateAdapter();

        //<editor-fold desc="Click: ListView">
        //beim Click wird ein Item angewählt
        //angewähltes Item soll: gelöscht, fortgeführt, erneut durchgeführt
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemClick: " + String.valueOf(position));

                Intent startTestView = new Intent(TestView.this, TestEnde1.class);

                int testId = tests.get(position).getTestId();
                startTestView.putExtra(TestEnde1.INTENT_KEY_TESTID, testId);
                startActivity(startTestView);

            }
        });




    }


    private void updateAdapter() {

        adapter = new CustomAdapter_TaskViewItems(
                this,
                R.layout.list_item_task_view_items,
                adapterItems,
                correctItems,
                selectedItems,
                answersShown
        );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
