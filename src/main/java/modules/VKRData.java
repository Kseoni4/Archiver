/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;



import java.io.*;
import java.nio.charset.Charset;

import java.util.LinkedList;


/**
 * Метод предназначен для хранения данных, необходимых для формирования протокола ВКР,
 * протокола Аттестации, содержит все необходимые методы для получения из внутренних полей и установки данных
 */
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
    private String studentNameRP;
    private String studentNameDP;
    private String studentHar;
    private String vkrName;
    private String nauchName;
    private String reviewerName;
    private String dateVkr;
    private String dateAttest;
    private String vkrGrade;
    private String vkrType;
    private String specialOpinion;
    private String diplom;
    private String qualification;
    private int pageNumberVkr;
    private int pageNumberAttest;

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

    public String getDiplom(){return diplom;}
    public void setDiplom(String aName){diplom=aName;}

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

    public String getStudentNameDP(){return studentNameDP;}
    public void setStudentNameDP(String aName){studentNameDP = aName;}

    public String getStudentNameRP(){return studentNameRP;}
    public void setStudentNameRP(String aName){studentNameRP = aName;}

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

    public String getDateVkr() {
        return dateVkr;
    }
    public void setDateVkr(String aDate){dateVkr = aDate;}

    public String getDateAttest() {
        return dateAttest;
    }
    public void setDateAttest(String aDate){dateAttest = aDate;}

    public String getProtocolNumber() {
        return protocolNumber;
    }
    public void setProtocolNumber(String aName){protocolNumber = aName;}

    public String getVkrGrade() {
        return vkrGrade;
    }
    public void setVkrGrade(String aName){vkrGrade = aName;}

    public String getSpecialOpinion(){return specialOpinion;}
    public void setSpecialOpinion(String aName){specialOpinion = aName;}

    public String getStudentHar(){return studentHar;}
    public void setStudentHar(String aName) throws IOException {
        try(BufferedInputStream csvReader = new BufferedInputStream(new FileInputStream("Harakteristiki.csv"))) {
            String[] nextLine = new String(csvReader.readAllBytes(), Charset.forName("UTF-8")).split("\n");
            studentHar = "";
            for (String s : nextLine){
                String ocenka = s.split(";")[0];
                String tmpHar = s.split(";")[1];
                if (ocenka.equals(aName)){
                    studentHar = tmpHar;
                }
            }
        }
    }

    public String getQualification() {return qualification;}
    public void setQualification(String qualification) {this.qualification = qualification;}

    public int getPageNumberVkr(){return pageNumberVkr;}
    public void setPageNumberVkr(int aPageNumber){pageNumberVkr = aPageNumber;}

    public int getPageNumberAttest(){return pageNumberAttest;}
    public void setPageNumberAttest(int aPageNumber){pageNumberAttest = aPageNumber;}

}
