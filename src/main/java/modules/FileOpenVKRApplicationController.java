/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;



import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;

import org.apache.poi.xwpf.usermodel.*;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOpenVKRApplicationController implements Initializable {

    private File file;
    @FXML
    private AnchorPane ap;

    @FXML
    private Label nameFile;

    @FXML
    private Label fileLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void nextStepButton(ActionEvent event) throws IOException, XMLStreamException {
        if ((file!=null)&&getFileExtension(file.getName()).equals("rtf")){
            rtfParse(file);
           /* FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseInstituteVKR.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Архивер. Версия 1.2:25/08/2021");
            window.setScene(scene);
            window.show();*/
        } else if (file==null){
            fileLabel.setText("Выберите файл с расширением rtf");
        } else if (!getFileExtension(file.getName()).equals("rtf")){
            fileLabel.setText("Выбран файл не с расширением rtf");
        }
    }

    @FXML
    void changeWindowToArchiverButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void chooseRTFFile(ActionEvent event){
        Stage stage = (Stage) ap.getScene().getWindow();
        file = new FileChooser().showOpenDialog(stage);
        if (file!=null){
            nameFile.setText(file.getName());
        } else{
            nameFile.setText("Файл не выбран");
        }
    }

    private String getFileExtension(String fileName){
        if (fileName.lastIndexOf(".")!=-1 && fileName.lastIndexOf(".")!=0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else
            return "";
    }


    private void rtfParse(File file) throws IOException, XMLStreamException {

        Document document1 = new Document();
        document1.loadFromFile(file.getAbsolutePath(), FileFormat.Rtf);
        document1.saveToFile("test.docx", FileFormat.Docx);
        FileInputStream fileInputStream = new FileInputStream("test.docx");
        XWPFDocument document = new XWPFDocument(fileInputStream);

        //Твой изначальный метод
        /*
        List<XWPFTable> tables = document.getTables();

        System.out.println("Total number of parags:: " +tables.size());
        System.out.println("Total number of rows in table 0:: " +tables.get(0).getRows().size());
        System.out.println("Total Number of TableCells in row 0:: "+tables.get(0).getRows().get(0).getTableCells().size());
        for (int i = 0; i<tables.size(); i++){
            for (int j = 0; j<tables.get(i).getRows().size(); j++){
                for (XWPFTableCell cell : tables.get(i).getRows().get(j).getTableCells()){
                    System.out.println("Table index:: "+i +" row index:: "+j+" " + cell.getText());
                }
            }
        }
        */

        //Можно посмареть скока и каких объектов в доке
        /*
        List<IBodyElement> tables = document.getBodyElements();
        System.out.println("Total number of parags:: " +tables.size());
        for (int i = 0; i<tables.size(); i++){
            System.out.println(tables.get(i).getElementType());
         */

        // По факту твой же метод, но выводящий чисто метаданные студентов групп

        List<XWPFTable> tables = document.getTables();

        System.out.println("Найдены следующие таблицы групп");
        for (int i = 0; i < tables.size(); i++) {
            if (tables.get(i).getRows().get(0).getCell(0).getText().equalsIgnoreCase("ФИО студента")) {
                for (int j = 0; j < tables.get(i).getRows().size(); j++) {
                    int studentNumber = 1;
                    int checkHelper = 0;
                    for (XWPFTableCell cell : tables.get(i).getRows().get(j).getTableCells()) {
                        if (cell.getText().equalsIgnoreCase("ФИО студента") ||
                                cell.getText().equalsIgnoreCase("Тема выпускной квалификационной работы") ||
                                cell.getText().equalsIgnoreCase("Руководитель \n" +
                                        "выпускной квалификационной работы\n")) {
                        } else {
                            System.out.println("Table index:: " + i + " row index:: " + j + " " + cell.getText());
                        }
                    }
                }
            }
        }

        fileInputStream.close();
    }

    //Метод для поиска групп
    private void findingGroups(File file) throws IOException, XMLStreamException {

        Document document1 = new Document();
        document1.loadFromFile(file.getAbsolutePath(), FileFormat.Rtf);
        document1.saveToFile("test.docx", FileFormat.Docx);
        FileInputStream fileInputStream = new FileInputStream("test.docx");
        XWPFDocument document = new XWPFDocument(fileInputStream);

        List<IBodyElement> tables = document.getBodyElements();
        ArrayList<String> groups = new ArrayList<>();
        System.out.println("Total number of parags:: " +tables.size());
        int tableCount = 0;
        Pattern groupIndexPattern = Pattern.compile("\\b[А-ЯЁ]{4}[-][0-9]{2}[-][0-9]{2}\\b");
        int i = 0;
        while (tableCount != 2) {
            //System.out.println(tables.get(i).getElementType().name());
            if (tables.get(i).getElementType().name().equalsIgnoreCase("TABLE")) {
                tableCount++;
            } else {
                String text = tables.get(i).getBody().getParagraphs().get(i).getText();
                //System.out.println(text);
                Matcher match = groupIndexPattern.matcher(text);
                if (match.find()) {
                    //System.out.println("Вы нашли группу - " + match.group());
                    groups.add(match.group());
                }
            }
            i++;
        }
        System.out.println("Найденные группы в документе - " + groups);

        fileInputStream.close();
    }
}
