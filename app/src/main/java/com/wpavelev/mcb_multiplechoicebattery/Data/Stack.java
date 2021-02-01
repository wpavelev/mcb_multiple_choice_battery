package com.wpavelev.mcb_multiplechoicebattery.Data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wpavelev.mcb_multiplechoicebattery.Activities.MainActivity;
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * save and manage the Tasks
 */
public class Stack {

    private static final String TAG = MainActivity.TAG;

    private final static String SPLITTER_ROW = ";#";
    private final static String SPLITTER_COL = ";";


    int stackId = 0;

    Context context;

    private List<Task> tasks = new ArrayList<>();



    DatabaseHelper db;

    public Stack(Context context, int id) {

        this.stackId = id;

        this.context = context;
        db = new DatabaseHelper(context);

        int[] stackIds = new int[1];
        stackIds[0] = stackId;

        db = new DatabaseHelper(this.context);


        if (db.findTask(stackId) == null) {

            Log.e(TAG, "Stack: findTask(" + String.valueOf(stackId) + ")=null");
            Toast.makeText(context, "ERROR AT LOADING TASKS", Toast.LENGTH_SHORT).show();
        } else {

            List<Integer> taskIds = new ArrayList<>(db.findTask(stackId));

            for (Integer taskId : taskIds) {
                tasks.add(new Task(db.getTask(taskId)));
            }
        }

    }

    /**
     * @return Anzahl der Aufgaben
     */
    public int getTaskLimit() {
        return tasks.size();
    }


    /**
     * @param id Task id to Open
     * @return Task if exist, if not: null
     */
    public Task getTask(Integer id) {
        if (id < tasks.size()) {
            return tasks.get(id);
        } else {
            return null;
        }


    }


}
