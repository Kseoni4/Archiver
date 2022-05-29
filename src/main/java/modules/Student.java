/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.beans.property.SimpleStringProperty;

public class Student {

    private SimpleStringProperty name;
    private SimpleStringProperty vkrName;
    private SimpleStringProperty nauchName;

    private SimpleStringProperty color;

    public Student(){}

    public Student(String aName, String aVkrName, String aNauchName){
        name = new SimpleStringProperty(aName);
        vkrName = new SimpleStringProperty(aVkrName);
        nauchName = new SimpleStringProperty(aNauchName);
        color = new SimpleStringProperty("none");
    }

    public void setName(String aName){
        name = new SimpleStringProperty(aName);
    }

    public void setVkrName(String aVkrName) {
        vkrName = new SimpleStringProperty(aVkrName);
    }

    public void setNauchName(String aName){
        nauchName = new SimpleStringProperty(aName);
    }

    public void setColor(String aColor){ color = new SimpleStringProperty(aColor);}

    public String getName(){
        return name.get();
    }

    public String getVkrName(){
        return vkrName.get();
    }

    public String getNauchName(){
        return nauchName.get();
    }

    public String getColor(){return color.get();}

}
