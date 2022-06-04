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


import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import main.Main;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Данный класс необходим для обработки событий из окна выбора студента
 */

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
    private String vkrType;
    private String qualification;
    private int pageNumberVkr;
    private int pageNumberAttest;



    @FXML protected TableView<GroupData> tableGroup;
    @FXML protected TableColumn<GroupData, String>  groupName;
    @FXML protected TableView<Student> tableStudent;
    @FXML protected TableColumn<Student, String> studentName;
    @FXML protected TableColumn<Student, String> studentVkrName;
    @FXML protected TableColumn<Student, String> studentNauchName;
    @FXML protected TableColumn<Student, String> studentRowColor;


    /**
     * Метод проводит первоначальную инициализацию окна перед представлением пользователю
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupName.setCellValueFactory(new PropertyValueFactory<GroupData,String>("name"));
        studentName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        studentVkrName.setCellValueFactory(new PropertyValueFactory<Student,String>("VkrName"));
        studentNauchName.setCellValueFactory(new PropertyValueFactory<Student,String>("NauchName"));
        studentRowColor.setCellValueFactory(new PropertyValueFactory<Student, String>("color"));
        studentRowColor.setVisible(false);
    }

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param aMembersGek - список членов ГЭК
     * @param aPredsedatel - имя председателя ГЭК
     * @param aSecretary - имя секретаря ГЭК
     */
    public void initGekData(LinkedList<MemberGek> aMembersGek, String aPredsedatel, String aSecretary){
        membersGek = new LinkedList<>(aMembersGek);
        predsedatelName = aPredsedatel;
        secretaryName = aSecretary;
    }

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param aCourseName - название направления
     * @param aCourseNumber - номер направления
     * @param aInstituteName - название института
     * @param aChairName - название кафедры
     */
    public void initCourseData(String aCourseName, String aCourseNumber, String aInstituteName, String aChairName){
        courseNumber = aCourseNumber;
        courseName = aCourseName;
        instituteName = aInstituteName;
        chairName = aChairName;

    }

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param aDate - выбранная ранее дата
     * @param aProtocolNumber - номер протокола
     */
    public void initOtherData(LocalDate aDate, String aProtocolNumber, int aPageNumberVkr, int aPageNumberAttest, String aQualification, String aVkrType){
        qualification = aQualification;
        vkrType = aVkrType;
        protocolNumber = aProtocolNumber;
        date = aDate;
        pageNumberVkr = aPageNumberVkr;
        pageNumberAttest = aPageNumberAttest;
    }

    /**
     * Метод предназначен для получения сведений о группах и студентах из предыдущего окна
     * @param aGroupData - список групп и студентов
     */
    public void initGroupData(LinkedList<GroupData> aGroupData){
        groupData = new LinkedList<>(aGroupData);
        getGroups();
    }

    /**
     * Метод предназначен для отображения списка групп в таблице групп
     */
    public void  getGroups(){
        ObservableList<GroupData> groups = FXCollections.observableArrayList();
        groups.addAll(groupData);
        tableGroup.setItems(groups);
    }

    /**
     * Метод предназначен для заполнения таблицы студентов при выборе группы из таблицы групп
     */
    public void setTableStudents(){
        GroupData group = tableGroup.getSelectionModel().getSelectedItem();
        if (group!=null) {
            ObservableList<Student> students = FXCollections.observableArrayList();
            students.addAll(group.getGroupStudents());
            tableStudent.setItems(students);
        }
    }

    /**
     * Метод предназначен для перехода в следующее окно и передачу туда данных
     * @param event
     * @throws IOException
     */
    @FXML
    public void nextStepButton(ActionEvent event) throws IOException {
        if (!tableStudent.getSelectionModel().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/mainWindowsVKR.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            VKRController controller = fxmlLoader.getController();
            controller.initStudentData(tableStudent.getSelectionModel().getSelectedItem(), groupData, qualification, vkrType);
            controller.initCourseData(courseNumber, courseName, instituteName, chairName);
            controller.initGekData(membersGek, predsedatelName, secretaryName);
            controller.initOtherData(date, protocolNumber, pageNumberVkr, pageNumberAttest);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Архивер. Версия 1.2:25/08/2021");
            window.setScene(scene);
            window.show();
        }

    }

    /**
     * Метод предназначен для перехода в предыдущее окно и передачу туда данных
     * @param event
     * @throws IOException
     */
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
