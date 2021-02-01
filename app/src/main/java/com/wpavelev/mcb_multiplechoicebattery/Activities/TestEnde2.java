package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wpavelev.mcb_multiplechoicebattery.CustomAdapter.CustomAdapter_TaskViewItems;
import com.wpavelev.mcb_multiplechoicebattery.CustomAdapter.CustomAdapter_TestEndeItems;
import com.wpavelev.mcb_multiplechoicebattery.Data.Task;
import com.wpavelev.mcb_multiplechoicebattery.Data.TestItem;
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;
import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestEnde2 extends AppCompatActivity {

    public final static String INTENT_KEY_TESTID = "testId";
    int testId;
    DatabaseHelper db;

    private List<String> selectedItems = new ArrayList<>();
    private List<String> correctItems = new ArrayList<>();
    private List<String> adapterItems = new ArrayList<>();
    private List<Integer> items = new ArrayList<>();


    List<TestItem> testItems;
    public final static String TAG = "Test Ende 2";

    CustomAdapter_TestEndeItems adapter;


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ende2);

        Intent intent = getIntent();
        testId = intent.getIntExtra(INTENT_KEY_TESTID, 0);
        listView = findViewById(R.id.testende_tasklist);


        db = new DatabaseHelper(getApplicationContext());

        if (db.dataExist(db.TABLE_TESTS) && db.dataExist(db.TABLE_TEST_ITEMS)) { //wenn tests exisitieren, tests laden

            Log.d(TAG, "loading Data for Adapter in TestEnde2: ");

            testItems = new ArrayList<>(db.getTestItems(testId));

            for (TestItem testItem : testItems) {
                Log.d(TAG, "TestItem: " + testItem.getTaskId());
            }

            for (int i = 0; i < testItems.size(); i++) {
                TestItem testItem = new TestItem(testItems.get(i));
                items.add(testItem.getTaskId());
                adapterItems.add(String.valueOf(testItem.getTaskId()));
                selectedItems.add(String.valueOf(testItem.getSelected()));
                correctItems.add(String.valueOf(testItem.getCorrect()));

            }


            adapter = new CustomAdapter_TestEndeItems(
                    this,
                    R.layout.list_item_test_ende_items,
                    adapterItems,
                    selectedItems,
                    correctItems
            );

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showCustomDialog(i);


            }
        });

    }



    private void showCustomDialog(final int id) {
        final Dialog dialog = new Dialog(TestEnde2.this);
        dialog.setContentView(R.layout.activity_task_view);

        Task task = new Task(testItems.get(id).getTask());

        TextView tv_Question = dialog.findViewById(R.id.questionTextView);
        tv_Question.setText(task.getQuestion());

        Button bn_Next = dialog.findViewById(R.id.TaskViewButtonRight);
        bn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (id < items.size() - 1)
                    showCustomDialog(id + 1);

            }
        });


        Button bn_Prev = dialog.findViewById(R.id.TaskViewButtonLeft);
        bn_Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (id > 0) {
                    showCustomDialog(id - 1);
                }

            }
        });

        Button bn_close = dialog.findViewById(R.id.TaskViewButtonMiddle);
        bn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ListView lv_Answers = dialog.findViewById(R.id.listViewAnswers);

        List<String> dialogAdapterItems = new ArrayList<>(task.getAnswers());
        List<Integer> dialogCorrectItems = new ArrayList<>(task.getCorrectAnswers());
        Set<Integer> dialogSelectedItems = new HashSet<>();
        for (int j = 0; j < 5; j++) {
            dialogSelectedItems.add(j);
        }


        CustomAdapter_TaskViewItems dialogTaskViewItems = new CustomAdapter_TaskViewItems(
                TestEnde2.this,
                R.layout.list_item_task_view_items,
                dialogAdapterItems,
                dialogCorrectItems,
                dialogSelectedItems,
                true
        );

        lv_Answers.setAdapter(dialogTaskViewItems);

        dialog.show();
    }





}
