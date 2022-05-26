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
import javafx.scene.Parent;
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
import java.util.LinkedList;
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
    private LinkedList<GroupData> groupData;

    @FXML
    void nextStepButton(ActionEvent event) throws IOException {
        if ((file!=null)&&getFileExtension(file.getName()).equals("rtf")){
           rtfParse(file);
           int i = 0;
           for (i=0;i<groupData.size();i++){
               System.out.println(groupData.get(i).getName());
           }

           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseInstituteVKR.fxml"));
           Scene scene = new Scene(fxmlLoader.load());
           ChooseInstituteController controller = fxmlLoader.getController();
           controller.getData(groupData);
           Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
           window.setTitle("Архивер. Версия 1.2:25/08/2021");
           window.setScene(scene);
           window.show();
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


    private void rtfParse(File file) throws IOException {

        Document document1 = new Document();
        document1.loadFromFile(file.getAbsolutePath(), FileFormat.Rtf);
        document1.saveToFile("test.docx", FileFormat.Docx);
        FileInputStream fileInputStream = new FileInputStream("test.docx");
        XWPFDocument document = new XWPFDocument(fileInputStream);

        groupData = new LinkedList<>();

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

        //System.out.println("Найденные группы в документе - " + groups);

        for(i = 0; i < tables.size(); i++) {
            if (tables.get(i).getRows().get(0).getCell(0).getText().equalsIgnoreCase("ФИО студента")) {

                //int studentNumber = 1;
                //System.out.println("Группа - " + groups.get(indexGroup));

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
                System.out.println();
            }
        }
        fileInputStream.close();
    }
}
