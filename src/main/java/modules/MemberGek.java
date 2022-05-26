/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;

public class MemberGek {
    private SimpleStringProperty name;
    private LinkedList<String> questions;

    public MemberGek(){
        questions = new LinkedList<>();
    }
    public MemberGek(String aName){
        questions = new LinkedList<>();
        name = new SimpleStringProperty(aName);
    }

    public void setName(String aName){
        name = new SimpleStringProperty(aName);
    }

    public void addQuestion(String question){
        questions.add(question);
    }

    public String getName(){
        return name.get();
    }

    public LinkedList<String> getQuestions(){
        return questions;
    }
}
