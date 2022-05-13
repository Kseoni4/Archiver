/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

public class VKRData {

    private String instituteName;
    private String courseNameFull;
    private String chairName;
    private String studentName;
    private String VKRName;
    private String HeadOfVKRName;
    private String reviewerName;
    private String courseName;
    private String dateFull;
    private String protocolNumber;
    private String VKRGrade;

    public VKRData(String instituteName,
                   String courseNameFull,
                   String chairName,
                   String studentName,
                   String VKRName,
                   String HeadOfVKRName,
                   String reviewerName,
                   String courseName,
                   String dateFull,
                   String protocolNumber,
                   String VKRGrade){
        this.instituteName = instituteName;
        this.courseNameFull = courseNameFull;
        this.chairName = chairName;
        this.studentName = studentName;
        this.VKRName = VKRName;
        this.HeadOfVKRName = HeadOfVKRName;
        this.reviewerName = reviewerName;
        this.courseName = courseName;
        this.dateFull = dateFull;
        this.protocolNumber = protocolNumber;
        this.VKRGrade = VKRGrade;
    }
    //Getters
    public String getCourseNameFull() {
        return courseNameFull;
    }
    public String getInstituteName() {
        return instituteName;
    }

    public String getChairName() {
        return chairName;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getVKRName() {
        return VKRName;
    }

    public String getHeadOfVKRName() {
        return HeadOfVKRName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDateFull() {
        return dateFull;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public String getVKRGrade() {
        return VKRGrade;
    }

}
