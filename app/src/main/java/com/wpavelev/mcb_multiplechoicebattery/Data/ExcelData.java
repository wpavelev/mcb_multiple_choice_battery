package com.wpavelev.mcb_multiplechoicebattery.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 13.03.2018.
 */

public class ExcelData {

    List<String[][]> sheet = new ArrayList<>();

    public void addSheet(String[][] data) {
        String[][] myData = data.clone();
        for (int i = 0; i < myData.length; i++) {
            for (int j = 0; j < myData[i].length; j++) {
                if (myData[i][j] == null) {
                    myData[i][j] = "";

                }
            }
        }
        sheet.add(myData);




    }


    public int getSheetsCount() {
        return sheet.size();
    }

    public String[][] getSheet(int sheetId) {
        return sheet.get(sheetId);
    }






}
