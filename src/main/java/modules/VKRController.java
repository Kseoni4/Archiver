/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class VKRController implements Initializable {
    // Полное название института (факультета)
    private HashMap<String, LinkedList<String>> hashMapInstitute;
    private HashMap<String, LinkedList<String>> hashMapChair;
    private HashMap<String, LinkedList<String>> hashMapNapr;


    //Полное имя студента в Именительном падеже
    @FXML
    protected TextField studentName;

    //Полное название темы ВКР
    @FXML
    protected TextField VKRName;

    //Имя и должность Научного руководителя (д-р. техн. наук, профессор Иванов И.И.)
    @FXML
    protected TextField HeadOfVKRName;

    //Рецензент для ВКР
    @FXML
    protected TextField reviewerName;

    //Номер направления обучения (10.10.10)
    @FXML
    protected TextField courseName;

    //Дата заседания (вторник, 14 января, 2022)
    @FXML
    protected TextField dateFull;

    //Номер протокола
    @FXML
    protected TextField protocolNumber;

    //ВКР выполнена в виде:
    @FXML
    protected ChoiceBox VKRType;

    //Итоговая оценка
    @FXML
    protected ChoiceBox VKRGrade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        VKRType.getItems().addAll("Бакалаврская работа (выпускная квалификационная работа бакалавра",
                    "Дипломный проект", "Дипломная работа", "Магистерская диссертация");
        VKRGrade.getItems().addAll("Неудовлетворительно", "Удовлетворительно", "Хорошо", "Отлично");

    }


    @FXML
    void changeWindowToChooseInstituteButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseInstituteVKR.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void makeDocumentVKR(ActionEvent event) throws Exception {
        System.out.println("Start making document");
        if (!Files.isDirectory(Paths.get("OutDocumentsVKR/"))) {
            Files.createDirectory(Paths.get("OutDocumentsVKR/"));
        }
        ProcessingDataVKR processingDataVKR = new ProcessingDataVKR(new VKRData(
                //instituteName.getValue(),
                //courseNameFull.getValue(),
                //chairName.getValue(),
                "123","321","234",
                studentName.getText(),
                VKRName.getText(),
                HeadOfVKRName.getText(),
                reviewerName.getText(),
                courseName.getText(),
                dateFull.getText(),
                protocolNumber.getText(),
                VKRGrade.getValue().toString()
        ));

        processingDataVKR.makeDocumentVKR();

    }

}
