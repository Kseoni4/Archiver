/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ChooseStudentController implements Initializable {

    private LinkedList<GroupData> groupData;
    private LinkedList<MemberGek> membersGek;
    private String predsedatelName;
    private String secretaryName;
    private LocalDate date;
    private String courseName;
    private String courseNumber;
    private String protocolNumber;
    private String instituteName;
    private String chairName;



    @FXML protected TableView<GroupData> tableGroup;
    @FXML protected TableColumn<GroupData, String>  groupName;
    @FXML protected TableView<Student> tableStudent;
    @FXML protected TableColumn<Student, String> studentName;
    @FXML protected TableColumn<Student, String> studentVkrName;
    @FXML protected TableColumn<Student, String> studentNauchName;
    @FXML protected TableColumn<Student, String> studentRowColor;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupName.setCellValueFactory(new PropertyValueFactory<GroupData,String>("name"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        studentVkrName.setCellValueFactory(new PropertyValueFactory<Student,String>("VkrName"));
        studentNauchName.setCellValueFactory(new PropertyValueFactory<Student,String>("NauchName"));
        studentRowColor.setCellValueFactory(new PropertyValueFactory<Student, String>("color"));
        studentRowColor.setVisible(false);
    }

    public void initGekData(LinkedList<MemberGek> aMembersGek, String aPredsedatel, String aSecretary){
        membersGek = new LinkedList<>(aMembersGek);
        predsedatelName = aPredsedatel;
        secretaryName = aSecretary;
    }

    public void initCourseData(String aCourseName, String aCourseNumber, String aInstituteName, String aChairName){
        courseNumber = aCourseNumber;
        courseName = aCourseName;
        instituteName = aInstituteName;
        chairName = aChairName;

    }

    public void initOtherData(LocalDate aDate, String aProtocolNumber){
        protocolNumber = aProtocolNumber;
        date = aDate;
    }

    public void initGroupData(LinkedList<GroupData> aGroupData){
        groupData = new LinkedList<>(aGroupData);
        getGroups();
    }

    public void  getGroups(){
        ObservableList<GroupData> groups = FXCollections.observableArrayList();
        for (GroupData group: groupData){
            groups.add(group);
        }
        tableGroup.setItems(groups);
    }

    public void setTableStudents(){
        GroupData group = tableGroup.getSelectionModel().getSelectedItem();
        if (group!=null) {
            ObservableList<Student> students = FXCollections.observableArrayList();
            for (Student student : group.getGroupStudents()) {
                students.add(student);
            }
            tableStudent.setItems(students);
           // styleRowColor();
        }
    }

   /* public void styleRowColor() {
        Callback<TableColumn<Student, String>,TableCell<Student, String>> cellFactory
                = new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
            @Override
            public TableCell<Student, String> call(TableColumn<Student, String> studentStringTableColumn) {
                final TableCell<Student, String> cell = new TableCell<Student, String>(){
                    @Override
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if (empty){
                            setGraphic(null);
                            setText(null);
                        } else {
                            setText(item);
                            TableRow<Student> row = getTableRow();
                            if (row.getItem().getColor().equals("green")){
                                row.getStyleClass().clear();
                                row.getStyleClass().add("\"-fx-background-color: green ;\"");
                            }
                        }
                    }
                };
                return cell;
            }
        };
        studentRowColor.setCellFactory(cellFactory);
    }
    */

    @FXML
    public void nextStepButton(ActionEvent event) throws IOException {
        if (!tableStudent.getSelectionModel().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/mainWindowsVKR.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            VKRController controller = fxmlLoader.getController();
            controller.initStudentData(tableStudent.getSelectionModel().getSelectedItem(), tableGroup.getSelectionModel().getSelectedItem().getGroupStudents(), groupData, tableGroup.getSelectionModel().getSelectedItem());
            controller.initCourseData(courseNumber, courseName, instituteName, chairName);
            controller.initGekData(membersGek, predsedatelName, secretaryName);
            controller.initOtherData(date, protocolNumber);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Архивер. Версия 1.2:25/08/2021");
            window.setScene(scene);
            window.show();
        }

    }
    @FXML
    public void backStepButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseInstituteVKR.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ChooseInstituteController controller = fxmlLoader.getController();
        controller.initGroupData(groupData);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }
}
