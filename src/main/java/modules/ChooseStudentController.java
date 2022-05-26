/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ChooseStudentController implements Initializable {

    private LinkedList<GroupData> groupData;

    @FXML protected TableView<GroupData> tableGroup;
    @FXML protected TableColumn<GroupData, String>  groupName;
    @FXML protected TableView<Student> tableStudent;
    @FXML protected TableColumn<Student, String> studentName;
    @FXML protected TableColumn<Student, String> studentVkrName;
    @FXML protected TableColumn<Student, String> studentNauchName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupName.setCellValueFactory(new PropertyValueFactory<GroupData,String>("name"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        studentVkrName.setCellValueFactory(new PropertyValueFactory<Student,String>("VkrName"));
        studentNauchName.setCellValueFactory(new PropertyValueFactory<Student,String>("NauchName"));
    }

    public void initGroupData(LinkedList<GroupData> aGroupData){
        groupData = new LinkedList<>();
        while (aGroupData.size()>0){
            groupData.add(aGroupData.remove(0));
        }
        System.out.println("Студенты данные получили");
        for (int i =0; i<groupData.size(); i++){
            System.out.println(groupData.get(i).getName());
        }
        getGroups();
    }

    public void  getGroups(){
        ObservableList<GroupData> groups = FXCollections.observableArrayList();
        for (int i =0; i<groupData.size(); i++){
            System.out.println(groupData.get(i).getName());
        }
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
        }
    }
}
