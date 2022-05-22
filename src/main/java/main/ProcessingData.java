/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package main;

import com.spire.doc.Document;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;


import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessingData {

    public static BaseData baseData;

    private static WordprocessingMLPackage template;

    private static String FIO;

    public static int PersonCaseNumber;

    private static List<Object> texts;

    public static String smkoBoth = "СМКО МИРЭА  8.5.1/03.П.42-20, СМКО МИРЭА  7.5.1/03.П.30-19";

    public static boolean allDocsCheck;
    public static boolean checkTitleDoc;
    public static boolean checkComDoc;
    public static boolean checkAPDoc;

    public static void loadBaseData(BaseData baseDataFromController){
        baseData = baseDataFromController;
    }

    public static void makeDocuments(Student student) throws IOException, Docx4JException {
        FIO = "".concat(student.LastName)
                .concat(" ")
                .concat(student.FirstName)
                .concat(" ").concat(student.FatherName);
        PersonCaseNumber++;
        if(allDocsCheck){
            makeAllDocs(student);
            mergeDocuments();
            return;
        }
        if(checkTitleDoc)
            makePersonCaseDoc(student);
        if(checkComDoc)
            makeCommissionConcludeDoc(student);
        if(checkAPDoc)
            makeConcludeAntiplagiatDoc(student);
        mergeDocuments();
    }


    private static void makeAllDocs(Student student) throws IOException, Docx4JException {
        makePersonCaseDoc(student);
        makeCommissionConcludeDoc(student);
        makeConcludeAntiplagiatDoc(student);
    }

    private static void mergeDocuments() {
        String personCase = "OutDocuments/"+baseData.Group_ID.concat("/")+FIO.concat(".docx");
        String concludeOfCommission = "OutDocuments/"+baseData.Group_ID.concat("/")+FIO.concat("_справка").concat(".docx");
        String concludeOfPlagiat = "OutDocuments/"+baseData.Group_ID.concat("/")+FIO.concat("_заключение").concat(".docx");

        System.out.println("==============================="+PersonCaseNumber+"===============================]");
        if(allDocsCheck)
            System.out.println("Made documents: "+personCase.concat(" \n").concat(concludeOfCommission).concat(" \n").concat(concludeOfPlagiat));

        Document blankDoc = new Document();
        if(checkTitleDoc) {
            System.out.println("Set new doc: " + personCase);

            Document personCaseDoc = new Document(personCase);

            blankDoc.importContent(personCaseDoc);
        }
        if(checkComDoc) {
            System.out.println("Insert new doc: " + concludeOfCommission);

            Document concludeOfCommissionDoc = new Document(concludeOfCommission);

            blankDoc.importContent(concludeOfCommissionDoc);

        }
        if(checkAPDoc) {
            System.out.println("Insert new doc: " + concludeOfPlagiat);

            Document concludeOfAPDoc = new Document(concludeOfPlagiat);

            blankDoc.importContent(concludeOfAPDoc);
        }
        System.out.println("Save document for: "+FIO);

        blankDoc.saveToFile("OutDocuments/"+baseData.Group_ID.concat("/")+"Merged/".concat(FIO).concat(".docx"));

        System.out.println("\u001b[38;5;40m["+PersonCaseNumber+"]"+"Merge documents - done!\u001b[38;5;0m");

        System.out.println("[===============================END===============================]");
    }

    private static void makePersonCaseDoc(Student student) throws Docx4JException {
        template = Main.getPersonCaseTemplate();
        texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        replaceBaseInfo();
        replaceStudentInfo(student);
        replacePlaceholder(student.Date, Placeholders.DATE);
        replacePlaceholder(student.Score, Placeholders.SCORE);
        replacePlaceholder(student.Protocol_ID, Placeholders.PROTOCOL_ID);
        replacePlaceholder(String.valueOf(PersonCaseNumber), Placeholders.PERSON_CASE_NUMBER);

        writeDocxToStream("OutDocuments/"+baseData.Group_ID.concat("/")+FIO.concat(".docx"));
    }

    private static void replaceBaseInfo() {
        replacePlaceholder(baseData.ChairName, Placeholders.CHAIR_NAME);
        replacePlaceholder(baseData.FacultyName, Placeholders.FACULTY_NAME);
        replacePlaceholder(baseData.InstituteName, Placeholders.INSTITUTE_NAME);
        replacePlaceholder(baseData.StudyForm, Placeholders.STUDY_FORM);
        replacePlaceholder(String.valueOf(baseData.YearOfGraduate), Placeholders.YEAR);
    }

    private static void replaceStudentInfo(Student student){
        replacePlaceholder(student.Group_ID, Placeholders.GROUP_ID);
        replacePlaceholder(student.Napravlenie_ID, Placeholders.NAPRAVLENIE_ID);
        replacePlaceholder(student.Person_ID, Placeholders.PERSON_ID);
        replacePlaceholder(FIO, Placeholders.FIO);
    }

    private static void makeCommissionConcludeDoc(Student student) throws Docx4JException {
        template = Main.getConcludeCommissionTemplate();
        texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        replaceBaseInfo();
        replaceStudentInfo(student);
        replacePlaceholder(student.VKRTitle, Placeholders.VKR_TITLE);
        replacePlaceholder(baseData.HeadOfCommissionName, Placeholders.HEAD_OF_COMMISSION_NAME);
        replacePlaceholder(baseData.ChairManName, Placeholders.CHAIRMAN_NAME);
        replacePlaceholder(baseData.ChairNameFull, Placeholders.CHAIR_NAME_FULL);

        writeDocxToStream("OutDocuments/"+baseData.Group_ID.concat("/")
                +student.LastName
                .concat(" ")
                .concat(student.FirstName
                .concat(" ")
                .concat(student.FatherName)
                .concat("_справка").concat(".docx")));
    }

    private static void makeConcludeAntiplagiatDoc(Student student) throws Docx4JException {
        template = Main.getConcludeOfPlagiatTemplate();
        texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        replaceBaseInfo();
        replaceStudentInfo(student);
        replacePlaceholder(baseData.VKRType, Placeholders.VKR_TYPE);
        replacePlaceholder(baseData.AntiplagiatSystem, Placeholders.ANTIPLAGIAT_SYSTEM);
        replacePlaceholder(student.HeadOfVKRName, Placeholders.HEAD_OF_VKR_NAME);
        replacePlaceholder(student.VKRTitle, Placeholders.VKR_TITLE);
        replacePlaceholder(baseData.ChairManName, Placeholders.CHAIRMAN_NAME);
        replacePlaceholder(baseData.ChairNameFull, Placeholders.CHAIR_NAME_FULL);
        replacePlaceholder(student.Originality, Placeholders.ORIGINALITY);
        replacePlaceholder(String.valueOf(PersonCaseNumber), Placeholders.PERSON_CASE_NUMBER);
        replacePlaceholder(baseData.smkoType, Placeholders.SMKO_TYPE);

        if(baseData.smkoType.equals("СМКО МИРЭА  8.5.1/03.П.42-20") || baseData.smkoType.equals(smkoBoth)) {
            replacePlaceholder("Временным", Placeholders.SMKO_TEMPORAL);
        }else{
            replacePlaceholder("", Placeholders.SMKO_TEMPORAL);
        }

        writeDocxToStream("OutDocuments/"+baseData.Group_ID.concat("/")
                +student.LastName.concat(" ").concat(
                        student.FirstName
                                .concat(" ")
                        .concat(student.FatherName)
                        .concat("_заключение")
                        .concat(".docx")
                )
        );
    }

    private static void writeDocxToStream(String target) throws Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    private static void replacePlaceholder(String name, String placeholder) {

        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }


    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }
}
