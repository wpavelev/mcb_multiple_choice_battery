package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.wpavelev.mcb_multiplechoicebattery.Data.Test;
import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TaskViewTest extends TaskView {


    Test test = new Test();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Test> tests = new ArrayList<>(db.getTests());
        boolean testFound = false;
        for (final Test t : tests) {
            //<editor-fold desc="for-loop: check if User want to continue last Test">
            if (t.getStackId() == stackID) {

                testFound = true;

                // Alertbox erzeugen
                final AlertDialog.Builder box = new AlertDialog.Builder(this);
                // Nachricht anzeigen

                box.setMessage("Test in diesem Stack fortsetzten?");
                // Eingabefeld erzeugen...



                box.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {

                        test = new Test(t);
                        writeTestStats();
                        if (test.getLastItem() != 0) {
                            position = test.getLastItem()-1;
                        }
                        loadingNextTask();
                        updateAdapter();


                    }
                });
                box.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {

                        startNewTest();
                        writeTestStats();
                    }
                });

                box.show();

                break;

            }
            //</editor-fold>
        }

        if (!testFound) {
            startNewTest();
            writeTestStats();
        }


        //<editor-fold desc="rightButton-Click">
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String textSelectedItems = "";
                String textCorrectItems = "";

                for (int i : correctItems) {
                    textCorrectItems += i;
                }


                for (Integer selectedItem : selectedItems) {
                    textSelectedItems += selectedItem;
                }

                char[] chars = textCorrectItems.toCharArray();
                Arrays.sort(chars);
                textCorrectItems = new String(chars);

                chars = textSelectedItems.toCharArray();
                Arrays.sort(chars);
                textSelectedItems = new String(chars);

                int intSelectedItems;
                if (textSelectedItems.equals("")) {
                    intSelectedItems = -1;
                } else {
                    intSelectedItems = Integer.parseInt(textSelectedItems);
                }
                int intCorrectItems = Integer.parseInt(textCorrectItems);


                int realTaskId = db.findTask(stackID, taskID);
                db.insertTestItem(test.getTestId(), realTaskId, intSelectedItems, intCorrectItems);

                if (selectedItemCorrect()) {
                    test.setCorrect(test.getCorrect() + 1);

                } else {
                    test.setWrong(1 + test.getWrong());
                }

                test.setLastItem(1+ test.getLastItem());

                loadingNextTask();
                selectedItems.clear();

                updateAdapter();


            }
        });
        //</editor-fold>


    }

    void startNewTest () {
        test = new Test(stackID,0, 0, 0, "");
    }

    @Override
    protected void onPause() {
        super.onPause();

        writeTestStats();

    }


    void writeTestStats() {
        test.setTestId(db.insertTest(test));

    }

}
