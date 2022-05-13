/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class ProcessingDataVKR {

    private VKRData vkrData;

    private WordprocessingMLPackage templateVKR;

    public ProcessingDataVKR(VKRData vkrData) throws FileNotFoundException, Docx4JException {
        this.vkrData = vkrData;
        mappings = new HashMap<>();
        mappings.put("insitut_name",vkrData.getInstituteName());
        mappings.put("kafedra_name",(vkrData.getChairName()));
        mappings.put("num_prot",vkrData.getProtocolNumber());
        mappings.put("kod_napr",vkrData.getCourseName());
        mappings.put("napr_full_name",vkrData.getCourseNameFull());
        mappings.put("full_date",vkrData.getDateFull());
        mappings.put("student_name",vkrData.getStudentName());
        mappings.put("vkr_name",vkrData.getVKRName());
        templateVKR = WordprocessingMLPackage.load(new FileInputStream("temlate1.docx"));
    }

    private HashMap<String,String> mappings;

    public void loadTemplatesVKR() throws FileNotFoundException, Docx4JException {
        templateVKR = WordprocessingMLPackage.load(new FileInputStream("template1.docx"));
    }


    public void makeDocumentVKR() throws Exception {
        VariablePrepare.prepare(templateVKR);
        templateVKR.getMainDocumentPart().variableReplace(mappings);
        System.out.println("End of making Document");
        File outputFile = new File("OutDocumentsVKR/"+"templateVKR.docx");
        templateVKR.save(outputFile);
        System.out.println("Document is written");
    }

}
