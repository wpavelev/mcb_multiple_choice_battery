package com.wpavelev.mcb_multiplechoicebattery.Data;

public class Test {


    int correct;
    int wrong;
    int lastItem;
    String date;
    int stackId;
    int testId;

    public Test() {

        correct = 0;
        wrong = 0;
        lastItem = 0;
        date = "";
    }

    public Test(int stackId, int correct, int wrong, int lastItem, String date) {
        this.stackId = stackId;
        this.correct = correct;
        this.wrong = wrong;
        this.lastItem = lastItem;
        this.date = date;
        testId = -1;
    }


    public Test(int testId, int stackId, int correct, int wrong, int lastItem, String date) {
        this.testId = testId;
        this.stackId = stackId;
        this.correct = correct;
        this.wrong = wrong;
        this.lastItem = lastItem;
        this.date = date;
    }

    public Test(Test test) {
        this.testId = test.testId;
        this.stackId = test.stackId;
        this.correct = test.correct;
        this.wrong = test.wrong;
        this.lastItem = test.lastItem;
        this.date = test.date;
    }

    public void setTestId(int testId) {

        this.testId = testId;
    }

    public int getTestId() {
        return testId;
    }

    public int getStackId() {
        return stackId;
    }

    public void setStackId(int stackId) {
        this.stackId = stackId;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getLastItem() {
        return lastItem;
    }

    public void setLastItem(int lastItem) {
        this.lastItem = lastItem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
