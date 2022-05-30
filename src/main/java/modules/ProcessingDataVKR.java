/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import main.Main;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessingDataVKR {


    private WordprocessingMLPackage templateDocument;

    private HashMap<String,String> mappings;

    public ProcessingDataVKR(VKRData vkrData){
        mappings = new HashMap<>();
        mappings.put("insitut_name",vkrData.getInstituteName());
        mappings.put("kafedra_name",(vkrData.getChairName()));
        mappings.put("num_prot",vkrData.getProtocolNumber());
        mappings.put("kod_napr",vkrData.getCourseNumber());
        mappings.put("napr_full_name",vkrData.getCourseName());
        mappings.put("predsedatel_name",vkrData.getPredsedatelName());
        mappings.put("full_date",vkrData.getDate());
        mappings.put("student_name",vkrData.getStudentName());
        mappings.put("vkr_theme",vkrData.getVkrName());
        mappings.put("type_of_vkr", vkrData.getVkrType());
        mappings.put("rukovoditel_vkr", vkrData.getNauchName());
        mappings.put("recenzent_vkr", vkrData.getReviewerName());
        mappings.put("ocenka", vkrData.getVkrGrade());
        mappings.put("secretar_name", vkrData.getSecretaryName());
        mappings.put("harakteristika", vkrData.getStudentHar());
        for (int i = 1; i<=6; i++){
            mappings.put("id"+i,"");
            mappings.put("question_chlen"+i+"_name", "");
            mappings.put("question"+i, "");
        }
        LinkedList<String> membersGekNames = vkrData.getMembersGekNames();
        for (int i = 1; i<=membersGekNames.size(); i++){
            mappings.put("chlen"+i+"_name", membersGekNames.get(i-1));
        }
        LinkedList<String> membersGekTableNames = vkrData.getMembersGekTableNames();
        LinkedList<String> membersGekQuestions = vkrData.getMembersGekQuestions();
        for (int i = 1; i<=membersGekTableNames.size(); i++){
            mappings.replace("id"+i, String.valueOf(i));
            mappings.replace("question_chlen"+i+"_name", membersGekTableNames.get(i-1));
            mappings.replace("question"+i, membersGekQuestions.get(i-1));
            //Main.logger.debug(membersGekQuestions.get(i-1));
        }
    }


    public void loadTemplatesVKR() throws Docx4JException {
        templateDocument = WordprocessingMLPackage.load(new File("Protocol_VKR.docx"));
    }

    public void loadTemplatesAtestacii() throws Docx4JException {
        templateDocument = WordprocessingMLPackage.load(new File("Protocol_atestacii.docx"));
    }


    public void makeDocumentVKR() throws Exception {
        loadTemplatesVKR();
        VariablePrepare.prepare(templateDocument);
        templateDocument.getMainDocumentPart().variableReplace(mappings);
        //Main.logger.debug("End of making Document");
        File outputFile = new File("OutDocumentsVKR/"+"templateVKR.docx");
        templateDocument.save(outputFile);
        //Main.logger.debug("Document is written");
    }

    public void makeDocumentAtestacii() throws Exception {
        loadTemplatesAtestacii();
        VariablePrepare.prepare(templateDocument);
        templateDocument.getMainDocumentPart().variableReplace(mappings);
        //Main.logger.debug("End of making Document");
        File outputFile = new File("OutDocumentsVKR/"+"templateAtestacii.docx");
        templateDocument.save(outputFile);
       // Main.logger.debug("Document is written");
    }

}
