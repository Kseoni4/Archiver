/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import main.Main;

import java.io.*;
import java.net.URL;
import java.util.*;


public class FileOpenVKRApplicationController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private Label nameFile;

    @FXML private Button nextButton;
    @FXML private Button backButton;
    @FXML private Button chooseButton;

    @FXML
    private Label fileLabel;

    @FXML protected ProgressBar progressBar = new ProgressBar();

    private ParseTask task;

    /**
     *
     * @param resourceBundle
     * Нечего инициализировать, оставляем как есть
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Нечего инициализировать
    }

    private LinkedList<GroupData> groupData;


    @FXML
    void nextStepButton(ActionEvent event) throws IOException {
        if (groupData!=null){
           FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseInstituteVKR.fxml"));
           Scene scene = new Scene(fxmlLoader.load());
           ChooseInstituteController controller = fxmlLoader.getController();
           controller.initGroupData(groupData);
           Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
           window.setTitle("Архивер. Версия 1.2:25/08/2021");
           window.setScene(scene);
           window.show();
        } else {
            fileLabel.setText("Не загружена информация из файла");
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
        Optional<File> optionalFile = Optional.ofNullable(new FileChooser().showOpenDialog(stage));
        if (optionalFile.isPresent()){
            if (!getFileExtension(optionalFile.get().getName()).equals("rtf")){
                fileLabel.setText("Выбран файл не с расширением rtf");
            } else {
                fileLabel.setText("Начинается обработка выбранного файла, подождите");
                nextButton.setDisable(true);
                backButton.setDisable(true);
                chooseButton.setDisable(true);
                task = new ParseTask(optionalFile.get(), nextButton, backButton, chooseButton, fileLabel, progressBar);
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(task.progressProperty());
                task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        groupData = task.getValue();
                    }
                });
                Thread threadParse = new Thread(task);
                threadParse.start();
                nameFile.setText(optionalFile.get().getName());
            }
        } else {
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
