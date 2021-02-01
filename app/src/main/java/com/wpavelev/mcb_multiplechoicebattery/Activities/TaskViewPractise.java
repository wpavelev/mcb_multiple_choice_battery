package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Vlad on 23.03.2018.
 */

public class TaskViewPractise extends TaskView {

    String TAG = "TaskViewPractise";

    private int[] level = {3, 7, 12, 18, 25, 35};
    Map<Integer, Integer> levelMaster = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterPractise();
            }
        });


    }

    @Override
    void loadingNextTask() {
        super.loadingNextTask();

        preparePractise();


    }

    void resetLevel() {
        Log.d(TAG, "resetLevel: posittion=" + position + " taskID=" + task);
        levelMaster.remove(taskID); //nach der Practise - Aufgabe kommt nochmmal eine übung
        //levelMaster.put(taskID, 0); //nach der Practise - Aufgabe kommt erneut eine Practise aufgabe

        queue.add(position + 1, taskID);
        queueChangedLog();


    }

    void queueChangedLog() {

        Log.i(TAG, "queueChangedLog: ");
        Log.i(TAG, "queue = " + queue.toString());


        Log.d(TAG, "queueChangedLog: position=" + position);
        Log.d(TAG, "queueChangedLog: taskID=" + taskID);

    }

    void loadingNextTaskFromOriginClass() {
        super.loadingNextTask();
    }

    void preparePractise() {
        selectedItems.clear();
        setItemColorOff();
        hideCorrectItems();
        showAnswerText(false);

        updateAdapter();

    }

    void showAnswerText(boolean show) {
        Log.d(TAG, "showAnswerText: ");
        String bText = "";

        if (show) {
            for (int i : correctItems) {
                switch (i) {
                    case 0:
                        bText += "A ";
                        break;
                    case 1:
                        bText += "B ";
                        break;
                    case 2:
                        bText += "C ";
                        break;
                    case 3:
                        bText += "D ";
                        break;
                    case 4:
                        bText += "E ";
                        break;

                }
            }
        } else {
            bText = getString(R.string.skip);
        }

        middleButton.setText(bText);
    }

    void setItemColorOn() {
        answersShown = true;
    }

    void setItemColorOff() {
        answersShown = false;
    }


    void levelUp() {

        if (levelMaster.containsKey(taskID)) {
            levelMaster.put(taskID, levelMaster.get(taskID) + 1);
        } else {
            levelMaster.put(taskID, 0);
        }

        if (levelMaster.get(taskID) <= level.length) {
            queue.add(position + level[levelMaster.get(taskID)], taskID);

        }
        queueChangedLog();
    }

    void levelDown() {
        if (levelMaster.containsKey(taskID)) {
            if (levelMaster.get(taskID) > 0) {
                levelMaster.put(taskID, levelMaster.get(taskID) - 1);
            }
        } else {
            levelMaster.put(taskID, 0);
        }

        if (levelMaster.get(taskID) < level.length) {
            queue.add(position + level[levelMaster.get(taskID)], taskID);
        }
        queueChangedLog();
    }

    void afterPractise() {

        if (answersShown) {

            if (selectedItemCorrect()) {
                levelUp();
            } else {
                resetLevel();
            }
            loadingNextTask();

        } else {

            setItemColorOn();
            updateAdapter();


        }

    }

    void showCorrectItems() {
        selectedItems = new HashSet<>(correctItems); //richtige Antworten auswählen, damit diese gezeigt werden können
        updateAdapter();
    }

    void hideCorrectItems() {
        selectedItems.clear();
        updateAdapter();

    }

    @Override
    void updateAdapter() {
        super.updateAdapter();


        Log.d(TAG, "updateAdapter: position=" + position);
        Log.d(TAG, "updateAdapter: taskID=" + taskID);


    }


}
