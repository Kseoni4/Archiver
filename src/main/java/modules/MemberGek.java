/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.beans.property.SimpleStringProperty;

/**
 * Класс предназначен для создания объектов, которые представляют членов ГЭК
 */
public class MemberGek {
    private SimpleStringProperty name;
    private SimpleStringProperty question;

    public MemberGek(String aName){
        name = new SimpleStringProperty(aName);
    }

    public void setName(String aName){
        name = new SimpleStringProperty(aName);
    }

    public void addQuestion(String aQuestion){
        question = new SimpleStringProperty(aQuestion);
    }

    public String getName(){
        return name.get();
    }

    public String getQuestion(){
        return question.get();
    }
}
