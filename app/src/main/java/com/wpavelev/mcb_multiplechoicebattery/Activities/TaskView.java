package com.wpavelev.mcb_multiplechoicebattery.Activities;

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
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;
import com.wpavelev.mcb_multiplechoicebattery.R;
import com.wpavelev.mcb_multiplechoicebattery.Data.Stack;
import com.wpavelev.mcb_multiplechoicebattery.Data.Stat;
import com.wpavelev.mcb_multiplechoicebattery.Data.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TaskView extends AppCompatActivity {


    DatabaseHelper db;

    static Set<Integer> selectedItems = new HashSet<>();
    List<Integer> correctItems = new ArrayList<>();
    List<String> adapterItems = new ArrayList<>();
    LinkedList<Integer> queue = new LinkedList<>();

    Stack stack;
    Task task;

    int taskID = 0;
    int position = 0;
    int stackID = 0;

    boolean answersShown = false;


    Button next, quit;
    private String TAG = "TaskView";

    TextView questionTextView, infoTextView;
    ListView listView;
    Button middleButton, rightButton, leftButton;
    CustomAdapter_TaskViewItems adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_view);
        listView = findViewById(R.id.listViewAnswers);
        questionTextView = findViewById(R.id.questionTextView);
        middleButton = findViewById(R.id.TaskViewButtonMiddle);
        rightButton = findViewById(R.id.TaskViewButtonRight);
        middleButton = findViewById(R.id.TaskViewButtonLeft);


        Intent intent = getIntent();
        stackID = intent.getIntExtra(MainActivity.KEY_INTENT_STACK_ID, 0);

        db = new DatabaseHelper(getApplicationContext());

        stack = new Stack(this, stackID);

        loadingNextTask(); //lade ersten Task
        updateAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (selectedItems.isEmpty() || !selectedItems.contains(position)) {
                    selectedItems.add(position);
                } else {
                    selectedItems.remove(position);
                }
                adapter.notifyDataSetChanged();
                updateAdapter();
            }
        });


    }

    boolean selectedItemCorrect() {
        Log.d(TAG, "selectedItemCorrect: ");

        //Prüfe, ob antworten richtig waren


        boolean itemAnswerCorrect = true;

        if (selectedItems.size() == 0) {//wenn kein Item gewählt wurde...
            if (correctItems.size() != 0) {//wenn mind. ein Item richtig ist...
                itemAnswerCorrect = false; //...ist die Auswahl falsch
            }
        } else {
            for (Integer correctItem : correctItems) {//alle Richtigen Antworten durchgehen
                if (!selectedItems.contains(correctItem)) { //wenn in der Liste der gewählten Antworten keine der richtigen Antworten vorkommt
                    itemAnswerCorrect = false; //...ist die Auswahl falsch
                }
            }


            for (Integer selectedItem : selectedItems) {
                if (!correctItems.contains(selectedItem)) {//wenn in der Liste der richrigen Antworten keine der gewählten Antworten vorkommt
                    itemAnswerCorrect = false; //...ist die Auswahl falsch
                }
            }
        }


        return itemAnswerCorrect;

    }

    void nextPosition() {
        Log.d(TAG, "nextPosition: ");
        int counter = db.getTaskCount(stackID);
        if (queue.size() != 0) {
            Log.d(TAG, "nextPosition: queueSize = " + queue.size());

            if (position < stack.getTaskLimit() - 1) {
                position++;
            } else {
                position = 0;
            }
        } else {
            Log.d(TAG, "nextPosition: queueSize = 0");
            for (int i = 0; i < counter; i++) {
                queue.add(i);
            }
        }


    }

    void loadingNextTask() {
        Log.d(TAG, "loadingNextTask: ");
        nextPosition();
        taskID = queue.get(position);
        Log.d(TAG, "taskID=" + taskID);
        task = stack.getTask(taskID);

        adapterItems = new ArrayList<>(task.getAnswers());
        correctItems = new ArrayList<>(task.getCorrectAnswers());
        questionTextView.setText(task.getQuestion());

        Stat stat = db.getStat(stackID, queue.get(position));


    }

    void updateAdapter() { //Adapter einstellen

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



