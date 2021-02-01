package com.wpavelev.mcb_multiplechoicebattery.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wpavelev.mcb_multiplechoicebattery.Helper.DatabaseHelper;
import com.wpavelev.mcb_multiplechoicebattery.Data.ExcelData;
import com.wpavelev.mcb_multiplechoicebattery.Helper.ExcelHelper;
import com.wpavelev.mcb_multiplechoicebattery.R;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    ExcelHelper excelHelper;
    ExcelData excelData;

    String file, name;

    Context context;

    ArrayList<String> uploadData;

    DatabaseHelper db;

    private final static String[] SPLITTER_ANSWERS = {"A)", "B)", "C)", "D)", "E)"};

    private final static String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = getApplicationContext();
        db = new DatabaseHelper(context);

        uploadData = new ArrayList<>();

        file = getIntent().getStringExtra("file");
        name = getIntent().getStringExtra("name");

        startHeavyProcessing();

    }

    private void startHeavyProcessing(){
        new loadingDataFromExcel().execute();
    }


    class loadingDataFromExcel extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            excelHelper = new ExcelHelper(context, file, 4, 100, 3);
            excelData = excelHelper.getExcelData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String question, answers, correctAnswers;

            int stackId = db.findStackByName(name);

            if (stackId == -1) {
                Log.e(TAG, "onPostExecute: no stack in DB found!");

            } else {

                for (int sheetId = 0; sheetId < excelData.getSheetsCount(); sheetId++) {
                    String[][] sheet = excelData.getSheet(sheetId);

                    for (int row = 0; row < sheet.length; row++) {
                        question = sheet[row][0];
                        answers = sheet[row][1];
                        correctAnswers = sheet[row][2];

                        List<String> answersList = new ArrayList<>(splitAnswers(answers));
                        List<String> correctAnswersList = new ArrayList<>(splitCorrectAnswers(correctAnswers));

                        int rowModified = row + sheet.length * sheetId; //Alle Sheets werden in ein Stack gepresst
                        db.insertTask(stackId, rowModified, question, answersList, correctAnswersList);

                    }
                }
            }


            finish();


        } // Postexecute

        private List<String> splitAnswers(String answerContent) {

            List<String> answers = new ArrayList<>();

            int splitter1 = 0, splitter2 = 0;

            String tempAnswersContent;

            int counterForIndexOf = 0;


            //Anwortoptionen aufteilen und in Liste einordnen
            for (int j = 0; j < 4; j++) {
                splitter1 = answerContent.indexOf(SPLITTER_ANSWERS[j], counterForIndexOf);
                splitter2 = answerContent.indexOf(SPLITTER_ANSWERS[j + 1], splitter1);


                if (splitter1 < 0 || splitter2 < 0) {
                    break;
                }
                tempAnswersContent = answerContent.substring(splitter1, splitter2);
                answers.add(tempAnswersContent);

                counterForIndexOf = splitter2;
            }


            //letzte Antwort abarbeiten
            if (splitter1 > 0 && splitter2 > 0) {
                tempAnswersContent = answerContent.substring(splitter2, answerContent.length() - 1);
                answers.add(tempAnswersContent);

            }

            return answers;

        }

        private List<String> splitCorrectAnswers (String correctAnswer) {
            List<String> correctAnswers = new ArrayList<>();

            //Zahlen und leerzeichen entfernen
            correctAnswer = correctAnswer.toUpperCase();
            char[] tempChar = correctAnswer.replaceAll("[^a-z^A-Z]", "").toCharArray();

            //Vorlage für richtige Antworten zum abgleich

            for (char c : tempChar) {
                switch (c) {
                    case 'A':
                        correctAnswers.add("A");
                        break;
                    case 'B':
                        correctAnswers.add("B");
                        break;
                    case 'C':
                        correctAnswers.add("C");
                        break;
                    case 'D':
                        correctAnswers.add("D");
                        break;
                    case 'E':
                        correctAnswers.add("E");
                        break;

                }

            }

            return correctAnswers;
        }






    } //class Loading Data from Excel


}




