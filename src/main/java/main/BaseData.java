/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package main;

import java.io.Serializable;

public class BaseData implements Serializable {

    public String ChairName;

    public String ChairNameFull;

    public String ChairManName;

    public String HeadOfCommissionName;

    public String InstituteName;

    public String FacultyName;

    public String StudyForm;

    public String VKRType;

    public String AntiplagiatSystem;

    public String Napravlenie_ID;

    public String Group_ID;

    public String smkoType;

    public int YearOfGraduate;

    public BaseData(String chairName, String chairNameFull, String chairManName,
                    String headOfCommissionName, String instituteName, String facultyName,
                    String studyForm, String VKRType, String antiplagiatSystem,
                    String napravlenie_ID, String group_ID, int yearOfGraduate, String smkoType) {
        ChairName = chairName;
        ChairNameFull = chairNameFull;
        ChairManName = chairManName;
        HeadOfCommissionName = headOfCommissionName;
        InstituteName = instituteName;
        FacultyName = facultyName;
        StudyForm = studyForm;
        this.VKRType = VKRType;
        AntiplagiatSystem = antiplagiatSystem;
        Napravlenie_ID = napravlenie_ID;
        Group_ID = group_ID;
        YearOfGraduate = yearOfGraduate;
        this.smkoType = smkoType;
    }
}
