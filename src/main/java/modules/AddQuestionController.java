/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Класс предназначен для обработки событий окна с добавлением вопросов членов ГЭК
 */
public class AddQuestionController implements Initializable {

    @FXML protected Button addButton;

    @FXML protected ComboBox<String> memberNames;

    @FXML protected TextArea question;

    private TableView<MemberGek> tableView;

    /**
     * Первоначальная инициализация окна
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Нечего инициализировать
    }

    /**
     * Заполнение ComboBox именами членов ГЭК
     * @param aMemberNames - Список имен членов ГЭК
     * @param aTableView - таблица с вопросами членов ГЭК
     */
    public void initMembersData(LinkedList<String> aMemberNames, TableView<MemberGek> aTableView){
        memberNames.getItems().addAll(aMemberNames);
        tableView = aTableView;
    }

    /**
     * Метод предназначен для добавления имени члена ГЭК и его вопроса в таблицу
     * @param event
     */
    @FXML
    public void addQuestionButtonSlave(ActionEvent event){
        if ((memberNames.getValue()!=null)&&(!question.getText().isBlank())) {
            MemberGek myMemberGek = new MemberGek(memberNames.getValue());
            myMemberGek.addQuestion(question.getText());
            tableView.getItems().add(myMemberGek);
            tableView.refresh();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}
