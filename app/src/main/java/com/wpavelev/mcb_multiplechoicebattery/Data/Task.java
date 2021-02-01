package com.wpavelev.mcb_multiplechoicebattery.Data;

import java.util.ArrayList;
import java.util.List;

public class Task {

    String question;
    List<String> answers;
    List<Integer> correctAnswers;
    int id;


    /**
     *
     * Erstellt eine neue Aufgabe
     *
     * @param question  Frage für als String
     * @param answers  Liste mit Antworten String (Formatierung beachten)
     * @param correctAnswers  Liste der richtigen Antworten (Integer mit Zahlen, die für die nummer der Antwort stehen
     * @param id  zugehörige ID -> Identifikation des Tasks, können sich in verschieden Stacks doppeln, jedoch nicht im gleichen!
     */
    public Task(String question, List<String> answers, List<Integer> correctAnswers, int id) {
        this.question = question;
        this.answers = new ArrayList<>(answers);
        this.correctAnswers = new ArrayList<>(correctAnswers);
        this.id = id;
    }

    public Task(Task task) {
        this.question = task.getQuestion();
        this.answers = new ArrayList<>(task.getAnswers());
        this.correctAnswers = new ArrayList<>(task.getCorrectAnswers());
        this.id = task.getId();
    }


    public int getId() {
        return id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }




}
