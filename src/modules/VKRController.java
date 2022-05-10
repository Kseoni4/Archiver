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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VKRController implements Initializable {
    // Полное название института (факультета)
    @FXML
    protected TextField instituteName;

    //Полное название направления обучения
    @FXML
    protected TextField courseNameFull;

    //Полное название кафедры
    @FXML
    protected TextField chairName;

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
    void changeWindowToArchiverButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

}
