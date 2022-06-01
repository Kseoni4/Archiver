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
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import javafx.scene.control.Button;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс предназначен для обработки выбранного ранее файла.
 *
 */

public class ParseTask extends Task<LinkedList<GroupData>> {

    private final File file;
    @FXML private final Button nextButton;
    @FXML private final Button backButton;
    @FXML private final Button chooseButton;
    @FXML private final Label fileLabel;
    @FXML private final ProgressBar progressBar;

    /**
     *
     * @param aFile - объект File выбранного файла
     * @param aNextButton - кнопка перехода в следующее окно
     * @param aBackButton - кнопка возврата в предыдущее окно
     * @param aChooseButton - кнопка выбора файла
     * @param aFileLabel - поле для вывода информации
     * @param aProgressBar - Прогресс бар, для демонстрации пользователю идущего процесса
     */
    public ParseTask(File aFile, Button aNextButton, Button aBackButton, Button aChooseButton, Label aFileLabel, ProgressBar aProgressBar){
        file = aFile;
        nextButton = aNextButton;
        backButton = aBackButton;
        chooseButton = aChooseButton;
        fileLabel = aFileLabel;
        progressBar = aProgressBar;
    }

    LinkedList<GroupData> groupData = new LinkedList<>();

    /**
     * Основной метод, в котором происходит обработка файла.
     * @return - Возвращается информация о группах, студентах, темах ВКР и научных руководителях
     * @throws Exception
     */
    @Override
    protected LinkedList<GroupData> call() throws Exception {

        Document document1 = new Document();
        document1.loadFromFile(file.getAbsolutePath(), FileFormat.Rtf);
        document1.saveToFile("test.docx", FileFormat.Docx);

        try (FileInputStream fileInputStream = new FileInputStream("test.docx");
             XWPFDocument document = new XWPFDocument(fileInputStream)){

            List<XWPFTable> tables = document.getTables();
            List<IBodyElement> objects = document.getBodyElements();
            ArrayList<String> groups = new ArrayList<>();


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

            for (i = 0; i < tables.size(); i++) {
                if (tables.get(i).getRows().get(0).getCell(0).getText().equalsIgnoreCase("ФИО студента")) {

                    for (int j = 1; j < tables.get(i).getRows().size(); j++) {
                        studentName = tables.get(i).getRows().get(j).getCell(0).getText();
                        studentVkrName = tables.get(i).getRows().get(j).getCell(1).getText();
                        studentNauchName = tables.get(i).getRows().get(j).getCell(2).getText();
                        groupData.get(indexGroup).getGroupStudents().add(new Student(studentName, studentVkrName, studentNauchName));
                    }

                    indexGroup++;
                }
            }
            Platform.runLater(() -> {
                nextButton.setDisable(false);
                backButton.setDisable(false);
                chooseButton.setDisable(false);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(1);
                fileLabel.setText("Обработка файла завершилась успешно");
            });
        }
        return groupData;
    }
}
