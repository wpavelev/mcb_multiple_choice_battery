package com.wpavelev.mcb_multiplechoicebattery.Data;


import com.wpavelev.mcb_multiplechoicebattery.Activities.MainActivity;
import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;

public class TestItem {
    
    int correct, selected, taskId, testId;
    DatabaseHelper db;

    public TestItem(int testId, int taskId, int correct, int selected) {
        db = new DatabaseHelper(MainActivity.getContext());
        this.correct = correct;
        this.selected = selected;
        this.taskId = taskId;
        this.testId = testId;
    }

    public TestItem(TestItem testItem) {
        db = new DatabaseHelper(MainActivity.getContext());
        this.correct = testItem.correct;
        this.selected = testItem.selected;
        this.taskId = testItem.taskId;
        this.testId = testItem.testId;
    }

    public TestItem() {
        db = new DatabaseHelper(MainActivity.getContext());

    }


    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public Task getTask() {
        return db.getTask(taskId);
    }
}
