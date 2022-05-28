/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class addQuestionController implements Initializable {

    @FXML protected Button addButton;

    @FXML protected ComboBox<String> memberNames;

    @FXML protected TextArea question;

    private TableView<MemberGek> tableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initMembersData(LinkedList<String> aMemberNames, TableView<MemberGek> aTableView){
        memberNames.getItems().addAll(aMemberNames);
        tableView = aTableView;
    }

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
