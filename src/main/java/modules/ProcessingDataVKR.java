/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import jakarta.xml.bind.JAXBException;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.model.structure.HeaderFooterPolicy;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Класс предназначен для формирования протоколов ВКР и аттестации
 */
public class ProcessingDataVKR {


    private WordprocessingMLPackage templateDocument;

    private HashMap<String,String> mappings;

    /**
     * Конструктор, который на основании данных из объекта VKRData формирует HashMap
     * @param vkrData - объект с данными для замены
     */
    public ProcessingDataVKR(VKRData vkrData){
        mappings = new HashMap<>();
        mappings.put("insitut_name",vkrData.getInstituteName());
        mappings.put("kafedra_name",(vkrData.getChairName()));
        mappings.put("num_prot",vkrData.getProtocolNumber());
        mappings.put("kod_napr",vkrData.getCourseNumber());
        mappings.put("napr_full_name",vkrData.getCourseName());
        mappings.put("predsedatel_name",vkrData.getPredsedatelName());
        mappings.put("full_date",vkrData.getDate());
        mappings.put("date",vkrData.getDate());
        mappings.put("student_name",vkrData.getStudentName());
        mappings.put("vkr_theme",vkrData.getVkrName());
        mappings.put("type_of_vkr", vkrData.getVkrType());
        mappings.put("rukovoditel_vkr", vkrData.getNauchName());
        mappings.put("recenzent_vkr", vkrData.getReviewerName());
        mappings.put("ocenka", vkrData.getVkrGrade());
        mappings.put("secretar_name", vkrData.getSecretaryName());
        mappings.put("harakteristika", vkrData.getStudentHar());
        mappings.put("osoboe_mnenie", vkrData.getSpecialOpinion());
        mappings.put("student_name_RP", vkrData.getStudentNameRP());
        mappings.put("student_name_DP", vkrData.getStudentNameDP());
        mappings.put("diplom",vkrData.getDiplom());
        mappings.put("kvalificacia",vkrData.getQualification());
        mappings.put("strnum1", String.valueOf(vkrData.getPageNumberVkr()));
        mappings.put("strnum2", String.valueOf(vkrData.getPageNumberVkr()+1));
        mappings.put("strnum3", String.valueOf(vkrData.getPageNumberVkr()+2));
        mappings.put("str_num1", String.valueOf(vkrData.getPageNumberAttest()));
        mappings.put("str_num2", String.valueOf(vkrData.getPageNumberAttest()+1));
        for (int i = 1; i<=6; i++){
            mappings.put("id"+i,"");
            mappings.put("question_chlen"+i+"_name", "");
            mappings.put("question_"+i, "");
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
            mappings.replace("question_" + i, membersGekQuestions.get(i-1));
        }
    }

    /**
     * Загружает документ для формирования протокола ВКР
     * @throws Docx4JException
     */
    public void loadTemplatesVKR() throws Docx4JException, IOException {
        templateDocument = WordprocessingMLPackage.load(ProcessingDataVKR.class.getResource("/Protocol_VKR.docx").openStream());
    }

    /**
     * Загружает документ для формирования протокола Аттестации
     * @throws Docx4JException
     */
    public void loadTemplatesAtestacii() throws Docx4JException, IOException {
        templateDocument = WordprocessingMLPackage.load(ProcessingDataVKR.class.getResource("/Protocol_atestacii.docx").openStream());
    }

    /**
     * Метод заменяет в ранее подгруженном документе все плэйсхолдеры на подготовленные данные,
     * сохраняет получившийся документ, а затем возвращает ссылку на сформированный файл
     * @return
     * @throws Exception
     */
    public File makeDocumentVKR() throws Exception {
        loadTemplatesVKR();
        VariablePrepare.prepare(templateDocument);
        templateDocument.getMainDocumentPart().variableReplace(mappings);
        List<SectionWrapper> sectionWrappers = templateDocument.getDocumentModel().getSections();
        HeaderFooterPolicy hfp = null;
        for (SectionWrapper sw : sectionWrappers) {
            hfp = sw.getHeaderFooterPolicy();
        }
        if (hfp != null) {
            hfp.getDefaultFooter().variableReplace(mappings);
            hfp.getEvenFooter().variableReplace(mappings);
            hfp.getFirstFooter().variableReplace(mappings);
        }
        File outputFile = new File("OutDocumentsVKR/"+mappings.get("student_name").replace(" ", "_")+"_Протокол_ВКР.docx");
        templateDocument.save(outputFile);
        return outputFile;
    }
    /**
     * Метод заменяет в ранее подгруженном документе все плэйсхолдеры на подготовленные данные и
     * сохраняет получившийся документ
     * @return
     * @throws Exception
     */
    public void makeDocumentAtestacii() throws Exception {
        loadTemplatesAtestacii();
        VariablePrepare.prepare(templateDocument);
        templateDocument.getMainDocumentPart().variableReplace(mappings);
        List<SectionWrapper> sectionWrappers = templateDocument.getDocumentModel().getSections();
        HeaderFooterPolicy hfp = null;
        for (SectionWrapper sw : sectionWrappers) {
            hfp = sw.getHeaderFooterPolicy();
        }
        if (hfp != null) {
            hfp.getDefaultFooter().variableReplace(mappings);
            hfp.getFirstFooter().variableReplace(mappings);
        }
            File outputFile = new File("OutDocumentsVKR/" + mappings.get("student_name").replace(" ", "_") + "_Протокол_Аттестации.docx");
            templateDocument.save(outputFile);
    }
}
