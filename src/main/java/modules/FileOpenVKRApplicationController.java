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
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Main;

import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.services.client.ConversionException;
import org.docx4j.services.client.Converter;
import org.docx4j.services.client.Format;
import org.w3c.dom.ranges.Range;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.rtf.RTFEditorKit;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
        fileInputStream.close();

    }

}
