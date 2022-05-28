/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import java.util.LinkedList;

public class VKRData {

    private String instituteName;
    private String chairName;
    private String courseNumber;
    private String courseName;
    private String protocolNumber;
    private String predsedatelName;
    private String secretaryName;
    private LinkedList<String> membersGekTableNames;
    private LinkedList<String> membersGekQuestions;
    private LinkedList<String> membersGekNames;
    private String studentName;
    private String vkrName;
    private String nauchName;
    private String reviewerName;
    private String date;
    private String vkrGrade;
    private String vkrType;

    public VKRData(String aInstituteName,
                   String aChairName,
                   String aCourseNumber,
                   String aCourseName,
                   String aProtocolNumber,
                   String aPredsedatelName,
                   String aSecretaryName,
                   LinkedList<String> aMembersGekTableNames,
                   LinkedList<String> aMembersGekQuestions,
                   LinkedList<String> aMembersGekNames,
                   String aStudentName,
                   String aVkrName,
                   String aNauchName,
                   String aReviewerName,
                   String aDate,
                   String aVkrGrade,
                   String aVkrType){
        instituteName = aInstituteName;
        chairName = aChairName;
        courseNumber = aCourseNumber;
        courseName = aCourseName;
        protocolNumber = aProtocolNumber;
        predsedatelName = aPredsedatelName;
        secretaryName = aSecretaryName;
        membersGekTableNames = new LinkedList<>(aMembersGekTableNames);
        membersGekQuestions = new LinkedList<>(aMembersGekQuestions);
        membersGekNames = new LinkedList<>(aMembersGekNames);
        studentName = aStudentName;
        vkrName = aVkrName;
        nauchName = aNauchName;
        reviewerName = aReviewerName;
        date = aDate;
        vkrGrade = aVkrGrade;
        vkrType = aVkrType;
    }
    //Getters
    public String getPredsedatelName(){return predsedatelName;}
    public LinkedList<String> getMembersGekTableNames(){return membersGekTableNames;}
    public LinkedList<String> getMembersGekQuestions(){return membersGekQuestions;}
    public LinkedList<String> getMembersGekNames(){return membersGekNames;}
    public String getVkrType(){return vkrType;}
    public String getCourseName() {
        return courseName;
    }
    public String getInstituteName() {
        return instituteName;
    }
    public String getSecretaryName(){return secretaryName;}

    public String getChairName() {
        return chairName;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getVkrName() {
        return vkrName;
    }

    public String getNauchName() {
        return nauchName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getDate() {
        return date;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public String getVkrGrade() {
        return vkrGrade;
    }

}
