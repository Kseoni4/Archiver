package main;

public class Student {

    public String FirstName;
    public String FatherName;
    public String LastName;

    public String Person_ID;
    public String Napravlenie_ID;
    public String Group_ID;

    public String Score;

    public String Date;

    public String Protocol_ID;

    public String VKRTitle;

    public String HeadOfVKRName;

    public String Originality;

    public String AntiplagiatSystem;

    public Student(){

    }

    public Student(String firstName, String fatherName, String lastName,
                   String person_ID, String napravlenie_ID, String group_ID,
                   String VKRTitle, String headOfVKRName, String originality,
                   String date, String protocol_ID, String score) {
        FirstName = firstName;
        FatherName = fatherName;
        LastName = lastName;
        Person_ID = person_ID;
        Napravlenie_ID = napravlenie_ID;
        Group_ID = group_ID;
        Originality = originality;
        this.VKRTitle = VKRTitle;
        HeadOfVKRName = headOfVKRName;
        Score = score;
        Date = date;
        Protocol_ID = protocol_ID;
    }
}
