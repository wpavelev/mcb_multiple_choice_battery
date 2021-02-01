package com.wpavelev.mcb_multiplechoicebattery.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wpavelev.mcb_multiplechoicebattery.Data.Stat;
import com.wpavelev.mcb_multiplechoicebattery.Data.Task;
import com.wpavelev.mcb_multiplechoicebattery.Data.Test;
import com.wpavelev.mcb_multiplechoicebattery.Data.TestItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "mcbdatabase.db";

    public final static String TABLE_STACKS = "stacks";
    public final static String TABLE_TASKS = "tasks";
    public final static String TABLE_ANSWERS = "answers";
    public final static String TABLE_CORRECT_ANSWERS = "correctanswers";
    public final static String TABLE_TESTS = "test";
    public final static String TABLE_TEST_ITEMS = "testitems";


    public final static String COL_ID = "id";
    public final static String COL_STACKNAME = "stack";
    public final static String COL_OPENED = "opened";
    public final static String COL_SCORE = "score";
    public final static String COL_ID_STACKINTASKTABLE = "stackid";
    public final static String COL_ID_TASKINTASKTABLE = "taskid";

    public final static String COL_QUESTION = "question";

    public final static String COL_ANSEWRS[] = {"answer1", "answer2", "answer3", "answer4", "answer5"};

    public final static String COL_T_DATUM = "datum";
    public final static String COL_T_STACK = "stack";
    public final static String COL_T_CORRECT = "testrichtig";
    public final static String COL_T_WRONG = "testfalsch";
    public final static String COL_T_LASTITEM = "lastitem";


    public final static String COL_TI_TASK = "titask";
    public final static String COL_TI_TESTID = "titestid";
    public final static String COL_TI_SELECTED = "selecteditems";
    public final static String COL_TI_CORRECT = "correctitems";

    public final static String TAG = "DatabaseHelper";
    private int counter = 0;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 11);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "";

        query = "CREATE TABLE stacks(id INTEGER PRIMARY KEY, stack TEXT);";
        db.execSQL(query);

        //TABELLE mit Stats anlegen
        query = "CREATE TABLE tasks(" +
                "id INTEGER PRIMARY KEY, " +
                "stackid INTEGER, " +
                "taskid INTEGER, " +
                "question TEXT, " +
                "answers TEXT, " +
                "canswer TEXT, " +
                "opened INTEGER, " +
                "score INTEGER);";
        db.execSQL(query);

        //TABELLE mit Stats anlegen
        query = "CREATE TABLE answers(" +
                "id INTEGER PRIMARY KEY, " +
                COL_ANSEWRS[0] + " TEXT, " +
                COL_ANSEWRS[1] + " TEXT, " +
                COL_ANSEWRS[2] + " TEXT, " +
                COL_ANSEWRS[3] + " TEXT, " +
                COL_ANSEWRS[4] + " TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE correctanswers(" +
                "id INTEGER PRIMARY KEY, " +
                COL_ANSEWRS[0] + " TEXT, " +
                COL_ANSEWRS[1] + " TEXT, " +
                COL_ANSEWRS[2] + " TEXT, " +
                COL_ANSEWRS[3] + " TEXT, " +
                COL_ANSEWRS[4] + " TEXT);";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_TESTS + " (" +
                "id INTEGER PRIMARY KEY, " +
                COL_T_DATUM + " TEXT, " +
                COL_T_STACK + " INTEGER, " +
                COL_T_CORRECT + " TEXT, " +
                COL_T_WRONG + " TEXT, " +
                COL_T_LASTITEM + " INTEGER);";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_TEST_ITEMS + " (" +
                "id INTEGER PRIMARY KEY, " +
                COL_TI_TESTID + " INTEGER, " +
                COL_TI_TASK + " INTEGER, " +
                COL_TI_SELECTED + " INTEGER, " +
                COL_TI_CORRECT + " INTEGER);";
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRECT_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_ITEMS);
        onCreate(db);
    }



    /**
     * @param file
     * @return return Stack, -1 if no Stack Found
     */
    public int findStackByName(String file) {

        int output = -1;
        SQLiteDatabase db = this.getReadableDatabase();


        try {
            String query = "SELECT * FROM " + TABLE_STACKS + " WHERE " + COL_STACKNAME + "=?";

            Cursor cursor = db.rawQuery(query, new String[]{file});

            if (cursor == null) {

                output = -1;

            } else {

                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                output = id;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;

    }

    public void insertStack(String file) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STACKNAME, file);
        db.insertWithOnConflict(TABLE_STACKS, null, contentValues, SQLiteDatabase.CONFLICT_ROLLBACK);
    }

    public void updateStack(String file, String newName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STACKNAME, newName);

        String selection = "id LIKE ?";
        String[] selectionArgs = {String.valueOf(findStackByName(file))};

        db.update(
                TABLE_STACKS,
                contentValues,
                selection,
                selectionArgs);

    }

    public ArrayList<String> getStacks() {
        Log.d(TAG, "getStacks: ");
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STACKS, null);
            cursor.moveToFirst();
            names.add(cursor.getString(cursor.getColumnIndex(COL_STACKNAME)));
            while (cursor.moveToNext()) {
                names.add(cursor.getString(cursor.getColumnIndex(COL_STACKNAME)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return names;
    }

    public String getStackById(int id) {
        Log.d(TAG, "getStackById: ");
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        try {
            String query = "SELECT * FROM " + TABLE_STACKS + " WHERE " + COL_ID + "=?";
            Cursor cursor = db.rawQuery(query, new String[]{"" + id});
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(COL_STACKNAME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return name;
    }

    public void deleteStack(int stackid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STACKS, "id=?", new String[]{String.valueOf(stackid)});

        ArrayList<Integer> taskIds = new ArrayList<>(findTask(stackid));

        for (Integer taskId : taskIds) {
            db.delete(TABLE_ANSWERS, COL_ID + "=?", new String[]{String.valueOf(taskId)});
            db.delete(TABLE_CORRECT_ANSWERS, COL_ID + "=?", new String[]{String.valueOf(taskId)});
            db.delete(TABLE_TASKS, COL_ID_STACKINTASKTABLE + "=?", new String[]{String.valueOf(stackid)});

        }

        List<Integer> testIds = new ArrayList<>(findTestIds(stackid));

        for (Integer testId : testIds) {
            db.delete(TABLE_TEST_ITEMS, COL_TI_TESTID + "=?", new String[]{String.valueOf(testId)});
            db.delete(TABLE_TESTS, COL_ID + "=?", new String[]{String.valueOf(testId)});
        }


    }




    /**
     * überprüft, ob die Tasks vorhanden sind
     *
     * @param stackId
     * @param taskId
     * @return int mit Task-ID des zu findenden Tasks; -1 wenn nicht gefunden
     */
    public Integer findTask(int stackId, int taskId) {
        ArrayList<Integer> taskIds = new ArrayList<>();
        String stackid_s = String.valueOf(stackId);
        String statId_s = String.valueOf(taskId);

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_TASKS +
                    " WHERE " + COL_ID_STACKINTASKTABLE + "=?" +
                    " AND " + COL_ID_TASKINTASKTABLE + "=?";
            Cursor cursor = db.rawQuery(
                    query,
                    new String[]{stackid_s, statId_s}
            );


            cursor.moveToFirst();
            do {

                int cIndex = cursor.getColumnIndex(COL_ID);
                int idStat = cursor.getInt(cIndex);
                taskIds.add(idStat);

            } while (cursor.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (taskIds.size() == 0) {
            return -1;
        }

        return taskIds.get(0);

    }

    /**
     * findet die Tasks mit der Id des stacks
     *
     * @param stackId Stack-Id
     * @return Liste mit Tasks, die gefunden wurden, wenn keine gefunden null
     */
    public ArrayList<Integer> findTask(int stackId) {
        String stackid_s = String.valueOf(stackId);

        ArrayList<Integer> taskIds = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();


        try {
            String query = "SELECT * FROM " + TABLE_TASKS +
                    " WHERE " + COL_ID_STACKINTASKTABLE + "=?";
            Cursor cursor = db.rawQuery(query, new String[]{stackid_s});


            cursor.moveToFirst();
            do {

                int cIndex = cursor.getColumnIndex(COL_ID);
                int idStat = cursor.getInt(cIndex);
                taskIds.add(idStat);

            } while (cursor.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (taskIds.size() == 0) {
            return null;
        }

        return taskIds;

    }

    public void insertTask(int stackId, int taskId, String question, List<String> answers, List<String> correctAnswers) {

        SQLiteDatabase db = this.getWritableDatabase();

        String stackid_s = "" + stackId;
        String taskId_s = "" + taskId;

        try {
            ContentValues content = new ContentValues();
            content.put(COL_ID_STACKINTASKTABLE, stackId);
            content.put(COL_ID_TASKINTASKTABLE, taskId);
            content.put(COL_QUESTION, question);

            db.insert(TABLE_TASKS, null, content);


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            ContentValues content = new ContentValues();
            for (int i = 0; i < COL_ANSEWRS.length; i++) {
                int size = answers.size();
                if (i < size) {
                    content.put(COL_ANSEWRS[i], answers.get(i));

                } else {
                    content.put(COL_ANSEWRS[i], "");

                }
            }

            Integer uniqueId = findTask(stackId, taskId);

            content.put(COL_ID, uniqueId);
            db.insert(TABLE_ANSWERS, null, content);


        } catch (Exception e) {
            Log.d(TAG, "insertTask: ERROR at inserting answers");
            e.printStackTrace();
        }


        try {

            ContentValues content = new ContentValues();


            for (int i = 0; i < COL_ANSEWRS.length; i++) {
                int size = correctAnswers.size();
                if (i < size) {
                    content.put(COL_ANSEWRS[i], correctAnswers.get(i));

                } else {
                    content.put(COL_ANSEWRS[i], "");

                }
            }

            int uniqueId = findTask(stackId, taskId);

            content.put(COL_ID, uniqueId);
            db.insert(TABLE_CORRECT_ANSWERS, null, content);


        } catch (Exception e) {

            Log.d(TAG, "insertTask: Error at inserting correct answers");
            e.printStackTrace();
        }


    }



    public Task getTask(int taskId) {

        String question = "";

        SQLiteDatabase db = this.getWritableDatabase();

        //Question
        try {


            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS +
                    " WHERE " + COL_ID + "=" + String.valueOf(taskId), null);
            cursor.moveToFirst();
            question = cursor.getString(cursor.getColumnIndex(COL_QUESTION));


        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));

        }

        //Answers
        List<String> answers = new ArrayList<>();
        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ANSWERS +
                    " WHERE " + COL_ID + "=" + String.valueOf(taskId), null);

            cursor.moveToFirst();

            for (int i = 0; i < 5; i++) {
                String text = cursor.getString(cursor.getColumnIndex(COL_ANSEWRS[i]));
                answers.add(text);
            }


        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));

        }

        //Correct Answers
        List<Integer> correctAnswers = new ArrayList<>();
        try {


            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CORRECT_ANSWERS +
                    " WHERE " + COL_ID + "=" + String.valueOf(taskId), null);

            cursor.moveToFirst();

            for (int i = 0; i < 5; i++) {

                String correctAnswer = cursor.getString(cursor.getColumnIndex(COL_ANSEWRS[i]));
                switch (correctAnswer) {
                    case "A":
                        correctAnswers.add(0);
                        break;
                    case "B":
                        correctAnswers.add(1);
                        break;
                    case "C":
                        correctAnswers.add(2);
                        break;
                    case "D":
                        correctAnswers.add(3);
                        break;
                    case "E":
                        correctAnswers.add(4);
                        break;
                }


            }


        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));

        }



        return new Task(question, answers, correctAnswers, taskId);


    }

    public int getTaskCount(int stackId) {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + COL_ID_STACKINTASKTABLE + " = " + String.valueOf(stackId);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        Log.d(TAG, "getTaskCount: count=" + count);
        return count;
    }



    public void insertStat(int stackId, int taskId, Stat stat) {

        Log.d(TAG, "insertStat: s" + stackId + " t" + taskId +
                " opened: " + stat.opened + " score: " + stat.score);

        SQLiteDatabase db = this.getWritableDatabase();

        int opened = stat.opened;
        int score = stat.score;

        ContentValues content = new ContentValues();
        content.put(COL_OPENED, opened);
        content.put(COL_SCORE, score);


        int uniqueId = findTask(stackId, taskId);

        int u = db.update(TABLE_TASKS, content, "id=?", new String[]{"" + uniqueId});
        if (u == 0) {
            db.insertWithOnConflict(TABLE_ANSWERS, null, content, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }

    public Stat getStat(int stackId, int taskId) {

        Log.d(TAG, "getStat: s" + stackId + " & t" + taskId);

        ArrayList<Stat> stats = new ArrayList();

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS +
                    " WHERE " + COL_ID_STACKINTASKTABLE + "=" + String.valueOf(stackId) +
                    " AND " + COL_ID_TASKINTASKTABLE + "=" + String.valueOf(taskId), null);

            cursor.moveToFirst();


            do {

                int opened = cursor.getInt(cursor.getColumnIndex(COL_OPENED));
                int score = cursor.getInt(cursor.getColumnIndex(COL_SCORE));

                stats.add(new Stat(opened, score));
            } while (cursor.moveToNext());


        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));

        }


        if (stats == null) {
            Log.e(TAG, "getStat: stats is null at " + stackId + " & " + taskId);

            return new Stat(0, 0);

        } else if (stats.size() == 0) {
            Log.e(TAG, "getStat: stats.size is 0 at " + stackId + " & " + taskId);
            return new Stat(0, 0);
        }

        return stats.get(0);
    }


    public List<Test> getTests() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Test> tests = new ArrayList<>();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TESTS, null);
            cursor.moveToFirst();
            tests.add(getTest(cursor.getInt(cursor.getColumnIndex(COL_ID))));
            while (cursor.moveToNext()) {
                tests.add(getTest(cursor.getInt(cursor.getColumnIndex(COL_ID))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tests;

    }

    public int insertTest(Test test) {
        int newTestId = test.getTestId();
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        content.put(COL_T_STACK, test.getStackId());
        content.put(COL_T_DATUM, dateFormat.format(date));
        content.put(COL_T_CORRECT, test.getCorrect());
        content.put(COL_T_WRONG, test.getWrong());
        content.put(COL_T_LASTITEM, test.getLastItem());

        if (newTestId == -1) {
            db.insertWithOnConflict(TABLE_TESTS, null, content, SQLiteDatabase.CONFLICT_REPLACE);
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TESTS, null);

            cursor.moveToLast();
            newTestId = cursor.getInt(cursor.getColumnIndex(COL_ID));
        } else {
            int u = db.update(TABLE_TESTS, content, "id=?", new String[]{"" + newTestId});
            if (u == 0) {
                db.insertWithOnConflict(TABLE_TESTS, null, content, SQLiteDatabase.CONFLICT_REPLACE);
            }

        }


        return newTestId;

    }

    private int findTestId(int stackId) {
        int output = -1;
        SQLiteDatabase db = this.getReadableDatabase();


        try {
            String query = "SELECT * FROM " + TABLE_TESTS + " WHERE " + COL_T_STACK + "=?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(stackId)});

            if (cursor == null) {

                output = -1;

            } else {

                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                output = id;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    private List<Integer> findTestIds(int stackId) {
        List<Integer> testIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        try {
            String query = "SELECT * FROM " + TABLE_TESTS + " WHERE " + COL_T_STACK + "=?";

            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(stackId)});

            if (cursor == null) {

                return null;

            } else {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                testIds.add(id);
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                    testIds.add(id);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return testIds;
    }

    public void deleteTest(int testId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_TESTS, COL_ID + "=?", new String[]{String.valueOf(testId)});
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (testId != -1) {

            try {
                db.delete(TABLE_TEST_ITEMS, COL_TI_TESTID + "=?", new String[]{String.valueOf(testId)});
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Log.e(TAG, "deleteTest: konnte Test Items nicht löschen. StackId=" + testId);
        }



    }

    public Test getTest(int testId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Test test = new Test();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TESTS +
                    " WHERE " + COL_ID + "=" + String.valueOf(testId), null);

            cursor.moveToFirst();

            test.setCorrect(cursor.getInt(cursor.getColumnIndex(COL_T_CORRECT)));
            test.setWrong(cursor.getInt(cursor.getColumnIndex(COL_T_WRONG)));
            test.setLastItem(cursor.getInt(cursor.getColumnIndex(COL_T_LASTITEM)));
            test.setDate(cursor.getString(cursor.getColumnIndex(COL_T_DATUM)));
            test.setStackId(cursor.getInt(cursor.getColumnIndex(COL_T_STACK)));
            test.setTestId(testId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (test == null) {

            Log.e(TAG, "getTest: test with testId=" + testId + "is null");
        }

        return test;



    }



    public void insertTestItem(int testId, int taskId, int selectedItems, int correctItems) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();

        content.put(COL_TI_TESTID, testId);
        content.put(COL_TI_TASK, taskId);
        content.put(COL_TI_SELECTED, selectedItems);
        content.put(COL_TI_CORRECT, correctItems);

        db.insert(TABLE_TEST_ITEMS, null, content);
    }

    public ArrayList<TestItem> getTestItems(int testId) {
        Log.d(TAG, "getStacks: ");
        ArrayList<TestItem> testItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        int taskId, correct, selected;
        TestItem testItem;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TEST_ITEMS +
                    " WHERE " + COL_TI_TESTID + " = " +
                    String.valueOf(testId), null);

            cursor.moveToFirst();


            taskId = cursor.getInt(cursor.getColumnIndex(COL_TI_TASK));
            correct = cursor.getInt(cursor.getColumnIndex(COL_TI_CORRECT));
            selected = cursor.getInt(cursor.getColumnIndex(COL_TI_SELECTED));
            testItem = new TestItem(testId, taskId, correct, selected);
            testItems.add(testItem);

            while (cursor.moveToNext()) {
                taskId = cursor.getInt(cursor.getColumnIndex(COL_TI_TASK));
                correct = cursor.getInt(cursor.getColumnIndex(COL_TI_CORRECT));
                selected = cursor.getInt(cursor.getColumnIndex(COL_TI_SELECTED));
                testItem = new TestItem(testId, taskId, correct, selected);
                testItems.add(testItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return testItems;
    }



    public boolean dataExist(String tableName, String dbfield, String fieldValue) {
        boolean output = false;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + dbfield + "=?", new String[]{fieldValue});

            int count = cursor.getCount();

            if (count <= 0) {

                output = false;
            } else {
                output = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "dataExist: TABLE=" + tableName +
                " FIELD=" + dbfield + " VALUE=" + fieldValue + " -> " + output);

        return output;
    }

    public boolean dataExist(String tableName) {
        Log.d(TAG, "dataExist: tableName: " + tableName);

        boolean output = false;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

            int count = cursor.getCount();

            if (count <= 0) {

                output = false;
            } else {
                output = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "dataExist: " + output);
        return output;
    }

    public Integer deleteTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, null, null);
    }

    public Integer deleteRows(String tableName, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName, "id=?", new String[]{String.valueOf(id)});
    }



}

