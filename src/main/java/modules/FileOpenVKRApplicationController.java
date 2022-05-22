/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

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

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    void nextStepButton(ActionEvent event) throws IOException {
       /* if ((file!=null)&&getFileExtension(file.getName()).equals("rtf")){*/
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseInstituteVKR.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Архивер. Версия 1.2:25/08/2021");
            window.setScene(scene);
            window.show();
        /*} else if (file==null){
            fileLabel.setText("Выберите файл с расширением rtf");
        } else if (!getFileExtension(file.getName()).equals("rtf")){
            fileLabel.setText("Выбран файл не с расширением rtf");
        }*/
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

    }
