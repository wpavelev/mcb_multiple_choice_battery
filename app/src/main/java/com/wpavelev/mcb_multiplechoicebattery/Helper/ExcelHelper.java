package com.wpavelev.mcb_multiplechoicebattery.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.wpavelev.mcb_multiplechoicebattery.Data.ExcelData;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vlad on 12.03.2018.
 */


public class ExcelHelper {


    ExcelData excelData = new ExcelData();

    String filePath;

    Context context;


    private static final String TAG = "ExcelHelper";


    ArrayList<String> uploadData = new ArrayList<>();

    ListView lvInternalStorage;
    private int cellLimit;
    private int sheetLimit ;
    private int rowLimit;


    /**
     *  @param context
     * @param file String of the path
     * @param sheetlimit max value of Sheets (Arbeitsblätter) to read
     * @param rowLimit  max value of rows (Zeilen) to read
     * @param cellLimit  max value of cells (Zellen) to read
     */
    public ExcelHelper(Context context, String file, int sheetlimit, int rowLimit, int cellLimit) {
        this.filePath = file;
        this.context = context;

        this.sheetLimit = 3;
        this.rowLimit = 10;
        this.cellLimit = 2;

        readExcelData(filePath);



    }


    private void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //decarle input filePath
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            int sheetCount = workbook.getNumberOfSheets();


            //Für jede Tabelle durchlaufen
            for (int sheetId = 0; sheetId < sheetCount; sheetId++) {

                if (sheetId > sheetLimit) {
                    Log.e(TAG, "readExcelData: ERROR. To many sheets - break after " + String.valueOf(sheetLimit));

                } else {

                    XSSFSheet sheet = workbook.getSheetAt(sheetId);
                    int rowsCount = sheet.getPhysicalNumberOfRows();
                    FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    StringBuilder sb = new StringBuilder();

                    ArrayList<ArrayList<String>> data = new ArrayList<>();


                    for (int r = 0; r < rowsCount; r++) {

                        if (r > rowLimit) {
                            Log.e(TAG, "readExcelData: ERROR. To many cells. Break after " + String.valueOf(cellLimit));
                            break;
                        } else {

                            Row row = sheet.getRow(r);
                            int cellsCount = row.getPhysicalNumberOfCells();

                            data.add(new ArrayList<String>());

                            for (int c = 0; c < cellsCount; c++) {

                                if (c > cellLimit) {
                                    Log.e(TAG, "readExcelData: ERROR. To many cells. Break after " + String.valueOf(cellLimit));
                                    break;
                                } else {
                                    Log.d(TAG, "Sheet: " + String.valueOf(sheetId) + " , Row: " + String.valueOf(r) + " Column: " + String.valueOf(c));
                                    String value = getCellAsString(row, c, formulaEvaluator);
                                    data.get(r).add(value);

                                }

                            }
                        }

                    }


                    int maxColumnSize = 0;

                    for (ArrayList<String> d : data) {
                        maxColumnSize = Math.max(maxColumnSize, d.size());
                    }

                    String[][] dataArray = new String[data.size()][maxColumnSize];

                    for (int i = 0; i < data.size(); i++) {
                        for (int j = 0; j < data.get(i).size(); j++) {
                            dataArray[i][j] = data.get(i).get(j);
                        }
                    }

                    excelData.addSheet(dataArray);

                }

            }


        } catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage());
        }
    }


    public void setCellLimit(int cellLimit) {
        this.cellLimit = cellLimit;
    }

    public void setSheetLimit(int sheetLimit) {
        this.sheetLimit = sheetLimit;
    }

    public void setRowLimit(int rowLimit) {
        this.rowLimit = rowLimit;
    }

    /**
     *
     * @return ExcelData - Check for Nullobjekts, if
     */
    public ExcelData getExcelData() {
        return excelData;
    }



    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);


            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;


                default:
                    value = "";
            }

            if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
                value = "" + cellValue.getStringValue();
            }


        } catch (NullPointerException e) {
            value = "";

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }


}
