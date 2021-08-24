package main;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
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

    public static int PersonCaseNumber = 0;

    private static List<Object> texts;

    public static void loadBaseData(BaseData baseDataFromController){
        baseData = baseDataFromController;
    }

    public static void makeDocuments(Student student) throws IOException, Docx4JException {
        makePersonCaseDoc(student);
        makeCommissionConcludeDoc(student);
        makeConcludeAntiplagiatDoc(student);
        mergeDocuments(student);
    }

    private static void mergeDocuments(Student student) {
        String FIO = "".concat(student.LastName)
                .concat(" ")
                .concat(student.FirstName)
                .concat(" ").concat(student.FatherName);

        String personCase = "OutDocuments/"+FIO.concat(".docx");
        String concludeOfCommission = "OutDocuments/"+FIO.concat("_справка").concat(".docx");
        String concludeOfPlagiat = "OutDocuments/"+FIO.concat("_заключение").concat(".docx");

        System.out.println("==============================="+PersonCaseNumber+"===============================");
        System.out.println("Made documents: "+personCase.concat(" \n").concat(concludeOfCommission).concat(" \n").concat(concludeOfPlagiat));

        System.out.println("Set new doc: "+personCase);

        Document personCaseDoc = new Document(personCase);

        System.out.println("Insert new doc: "+concludeOfCommission);

        personCaseDoc.insertTextFromFile(concludeOfCommission, FileFormat.Docx);

        System.out.println("Insert new doc: "+concludeOfPlagiat);

        personCaseDoc.insertTextFromFile(concludeOfPlagiat, FileFormat.Docx);

        System.out.println("Save document for: "+FIO);

        personCaseDoc.saveToFile("OutDocuments/Merged/".concat(FIO).concat(".docx"));

        System.out.println("["+PersonCaseNumber+"]"+"Merge documents - done!");

        System.out.println("[===============================END===============================");
    }

    private static void makePersonCaseDoc(Student student) throws IOException, Docx4JException {
        template = Main.getPersonCaseTemplate();
        texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        replaceBaseInfo();
        PersonCaseNumber++;

        String FIO = "".concat(student.LastName)
                .concat(" ")
                .concat(student.FirstName)
                .concat(" ").concat(student.FatherName);
        replaceStudentInfo(student);
        replacePlaceholder(student.Date, Placeholders.DATE);
        replacePlaceholder(student.Score, Placeholders.SCORE);
        replacePlaceholder(student.Protocol_ID, Placeholders.PROTOCOL_ID);
        replacePlaceholder(String.valueOf(PersonCaseNumber), Placeholders.PERSON_CASE_NUMBER);

        writeDocxToStream("OutDocuments/"+FIO.concat(".docx"));
    }

    private static void replaceBaseInfo(){
        replacePlaceholder(baseData.ChairName, Placeholders.CHAIR_NAME);
        replacePlaceholder(baseData.FacultyName, Placeholders.FACULTY_NAME);
        replacePlaceholder(baseData.InstituteName, Placeholders.INSTITUTE_NAME);
        replacePlaceholder(baseData.StudyForm, Placeholders.STUDY_FORM);
        replacePlaceholder(String.valueOf(baseData.YearOfGraduate), Placeholders.YEAR);
    }

    private static void replaceStudentInfo(Student student){
        String FIO = "".concat(student.LastName)
                .concat(" ")
                .concat(student.FirstName)
                .concat(" ").concat(student.FatherName);
        replacePlaceholder(student.Group_ID, Placeholders.GROUP_ID);
        replacePlaceholder(student.Napravlenie_ID, Placeholders.NAPRAVLENIE_ID);
        replacePlaceholder(student.Person_ID, Placeholders.PERSON_ID);
        replacePlaceholder(FIO, Placeholders.FIO);
    }

    private static void makeCommissionConcludeDoc(Student student) throws IOException, Docx4JException {
        template = Main.getConcludeCommissionTemplate();
        texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        replaceBaseInfo();
        replaceStudentInfo(student);
        replacePlaceholder(student.VKRTitle, Placeholders.VKR_TITLE);
        replacePlaceholder(baseData.HeadOfCommissionName, Placeholders.HEAD_OF_COMMISSION_NAME);
        replacePlaceholder(baseData.ChairManName, Placeholders.CHAIRMAN_NAME);
        replacePlaceholder(baseData.ChairNameFull, Placeholders.CHAIR_NAME_FULL);

        writeDocxToStream("OutDocuments/"+student.LastName.concat(" ").concat(student.FirstName.concat(" ").concat(student.FatherName).concat("_справка").concat(".docx")));
    }

    private static void makeConcludeAntiplagiatDoc(Student student) throws IOException, Docx4JException {
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

        writeDocxToStream("OutDocuments/"+student.LastName.concat(" ").concat(student.FirstName.concat(" ").concat(student.FatherName).concat("_заключение").concat(".docx")));
    }

    private static void writeDocxToStream(String target) throws IOException, Docx4JException {
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
