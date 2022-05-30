/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import main.Main;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTask extends Task<LinkedList<GroupData>> {

    private File file;
    @FXML private Button nextButton;
    @FXML private Button backButton;
    @FXML private Button chooseButton;
    @FXML private Label fileLabel;
    @FXML private ProgressBar progressBar;

    public ParseTask(File aFile, Button aNextButton, Button aBackButton, Button aChooseButton, Label aFileLabel, ProgressBar aProgressBar){
        file = aFile;
        nextButton = aNextButton;
        backButton = aBackButton;
        chooseButton = aChooseButton;
        fileLabel = aFileLabel;
        progressBar = aProgressBar;
    }

    LinkedList<GroupData> groupData = new LinkedList<>();

    @Override
    protected LinkedList<GroupData> call() throws Exception {
        //Main.logger.debug("Начинаю выполнение");
        System.out.println("Начинаю выполнение");

        Document document1 = new Document();
        document1.loadFromFile(file.getAbsolutePath(), FileFormat.Rtf);
        document1.saveToFile("test.docx", FileFormat.Docx);
        FileInputStream fileInputStream = new FileInputStream("test.docx");
        XWPFDocument document = new XWPFDocument(fileInputStream);

        List<XWPFTable> tables = document.getTables();
        List<IBodyElement> objects = document.getBodyElements();
        ArrayList<String> groups = new ArrayList<>();

        System.out.println("Выполняютсь здесь");

        int tableCount = 0;
        int i = 0;
        int indexGroup = 0;

        String studentName;
        String studentVkrName;
        String studentNauchName;

        Pattern groupIndexPattern = Pattern.compile("\\b[А-ЯЁ]{4}[-][0-9]{2}[-][0-9]{2}\\b");

        while (tableCount != 2) {
            if (objects.get(i).getElementType().name().equalsIgnoreCase("TABLE")) {
                tableCount++;
            } else {
                String text = objects.get(i).getBody().getParagraphs().get(i).getText();
                Matcher match = groupIndexPattern.matcher(text);
                if (match.find()) {
                    groups.add(match.group());
                    groupData.add(new GroupData(match.group()));
                }
            }
            i++;
        }
        System.out.println("А теперь здесь");


        for(i = 0; i < tables.size(); i++) {
            if (tables.get(i).getRows().get(0).getCell(0).getText().equalsIgnoreCase("ФИО студента")) {

                for (int j = 1; j < tables.get(i).getRows().size(); j++) {

                    studentName = tables.get(i).getRows().get(j).getCell(0).getText();
                    studentVkrName = tables.get(i).getRows().get(j).getCell(1).getText();
                    studentNauchName = tables.get(i).getRows().get(j).getCell(2).getText();

                    groupData.get(indexGroup).getGroupStudents().add(new Student(studentName, studentVkrName, studentNauchName));

                    /*
                    System.out.println(studentNumber + " - студент:\n" +
                            "ФИО: " + tables.get(i).getRows().get(j).getCell(0).getText() + "\n" +
                            "Тема ВКР: " + tables.get(i).getRows().get(j).getCell(1).getText() + "\n" +
                            "Научный руководитель: " + tables.get(i).getRows().get(j).getCell(2).getText());
                    studentNumber++;
                     */
                }
                indexGroup++;
            }
        }
        System.out.println("заканчиваю выполнение");
        //Main.logger.debug("Заканчиваю выполнение");
        fileInputStream.close();
        Platform.runLater(()-> {
                nextButton.setDisable(false);
                backButton.setDisable(false);
                chooseButton.setDisable(false);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(1);
                fileLabel.setText("Обработка файла завершилась успешно");
            });
        return groupData;
    }

    public List<GroupData> getGroupData(){
        return groupData.stream().toList();
    }
}
