/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;

public class GroupData {

    private SimpleStringProperty groupName;

    private LinkedList<Student> groupStudents;

    public GroupData(String aName){
        groupStudents = new LinkedList<>();
        groupName = new SimpleStringProperty(aName);
    }

    public void setName(String aName){
        groupName = new SimpleStringProperty(aName);
    }

    public void addStudent(Student pStudent){
        groupStudents.add(pStudent);
    }

    public String getName(){
        return groupName.get();
    }

    public LinkedList<Student> getGroupStudents(){
        return groupStudents;
    }

}
