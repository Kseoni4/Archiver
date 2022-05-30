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
    private String studentHar;

    public VKRData(LinkedList<String> aMembersGekTableNames,
                   LinkedList<String> aMembersGekQuestions,
                   LinkedList<String> aMembersGekNames){
        membersGekTableNames = new LinkedList<>(aMembersGekTableNames);
        membersGekQuestions = new LinkedList<>(aMembersGekQuestions);
        membersGekNames = new LinkedList<>(aMembersGekNames);
    }
    //Getters
    public String getPredsedatelName(){return predsedatelName;}
    public void setPredsedatelName(String aName){predsedatelName = aName;}

    public LinkedList<String> getMembersGekTableNames(){return membersGekTableNames;}
    public LinkedList<String> getMembersGekQuestions(){return membersGekQuestions;}
    public LinkedList<String> getMembersGekNames(){return membersGekNames;}

    public String getVkrType(){return vkrType;}
    public void setVkrType(String aType) {vkrType = aType;}

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String aName){courseName = aName;}

    public String getInstituteName() {
        return instituteName;
    }
    public void setInstituteName(String aName){instituteName = aName;}

    public String getSecretaryName(){return secretaryName;}
    public void setSecretaryName(String aName){secretaryName = aName;}

    public String getChairName() {
        return chairName;
    }
    public void setChairName(String aName){chairName = aName;}

    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String aName){studentName=aName;}

    public String getVkrName() {
        return vkrName;
    }
    public void setVkrName(String aName){vkrName = aName;}

    public String getNauchName() {
        return nauchName;
    }
    public void setNauchName(String aName){nauchName =aName;}

    public String getReviewerName() {
        return reviewerName;
    }
    public void setReviewerName(String aName){reviewerName = aName;}

    public String getCourseNumber() {
        return courseNumber;
    }
    public void setCourseNumber(String courseNumber) {this.courseNumber = courseNumber;}

    public String getDate() {
        return date;
    }
    public void setDate(String aDate){date = aDate;}

    public String getProtocolNumber() {
        return protocolNumber;
    }
    public void setProtocolNumber(String aName){protocolNumber = aName;}

    public String getVkrGrade() {
        return vkrGrade;
    }
    public void setVkrGrade(String aName){vkrGrade = aName;}

    public String getStudentHar(){return studentHar;}
    public void setStudentHar(String aName){studentHar = aName;}

}
