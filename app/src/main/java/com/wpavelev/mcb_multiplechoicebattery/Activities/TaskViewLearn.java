package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.os.Bundle;
import android.view.View;

import com.wpavelev.mcb_multiplechoicebattery.Data.Mode;


public class TaskViewLearn extends TaskViewPractise {

    Mode tMode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tMode = Mode.learn;
        setItemColorOn(); //Richtige antworten werden zu beginn angezeigt
        showCorrectItems();

        //<editor-fold desc="rightButton-Click">
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tMode) {
                    case learn: //Lernmodus war aktiv
                        levelUp();
                        loadingNextTask();
                        break;

                    case practise://Übungsmodus war aktiv
                        afterPractise();
                        break;
                }
            }
        });
        //</editor-fold>

    }



    @Override
    void preparePractise() {
        super.preparePractise();
        tMode = Mode.practise;
    }

    @Override
    void loadingNextTask() {
       super.loadingNextTaskFromOriginClass();

        if (levelMaster.containsKey(taskID)) {
          preparePractise();

        } else {
            prepareLearn();

        }

    }

    void prepareLearn() {
        selectedItems.clear();
        setItemColorOn();
        showAnswerText(true);
        tMode = Mode.learn;

        showCorrectItems();
        updateAdapter();
    }






}
