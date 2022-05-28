/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class VKRController implements Initializable {

    //Полное имя студента в Именительном падеже
    @FXML protected Label studentName;

    //Полное название темы ВКР
    @FXML protected TextArea vkrName;

    //Имя и должность Научного руководителя (д-р. техн. наук, профессор Иванов И.И.)
    @FXML protected Label nauchName;

    //Рецензент для ВКР
    @FXML protected TextField reviewerName;

    //Название института
    @FXML protected TextField instituteName;

    //Название кафедры
    @FXML protected TextField chairName;

    //Номер направления обучения (10.10.10)
    @FXML protected Label courseNumber;

    //Дата заседания (вторник, 14 января, 2022)
    @FXML protected Label dateFull;

    //Номер протокола
    @FXML protected Label protocolNumber;

    //Председатель ГЭК
    @FXML protected Label predsedatelName;

    //Секретарь ГЭК
    @FXML protected Label secretaryName;

    //Первый член ГЭК
    @FXML protected Label memberGekOne;
    //Второй член ГЭК
    @FXML protected Label memberGekTwo;
    //Третий член ГЭК
    @FXML protected Label memberGekThree;
    //Четвертый член ГЭК
    @FXML protected Label memberGekFour;
    //Пятый член ГЭК
    @FXML protected Label memberGekFive;

    //Название направления
    @FXML protected Label courseName;

    //ВКР выполнена в виде:
    @FXML protected ChoiceBox VKRType;

    //Итоговая оценка
    @FXML protected ChoiceBox VKRGrade;

    @FXML protected TableView<MemberGek> memberGekTable;
    @FXML protected TableColumn<MemberGek,String> memberGekName;
    @FXML protected TableColumn<MemberGek,String> memberGekQuestion;


    private LinkedList<Student> students;

    private LinkedList<MemberGek> membersGek;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        VKRType.getItems().addAll("Бакалаврская работа (выпускная квалификационная работа бакалавра",
                    "Дипломный проект", "Дипломная работа", "Магистерская диссертация");
        VKRGrade.getItems().addAll("Неудовлетворительно", "Удовлетворительно", "Хорошо", "Отлично");
        memberGekName.setCellValueFactory(new PropertyValueFactory<MemberGek, String>("name"));
        memberGekQuestion.setCellValueFactory(new PropertyValueFactory<MemberGek,String>("question"));

    }

    @FXML
    void changeWindowToChooseStudentButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void makeDocumentVKR(ActionEvent event) throws Exception {
        LinkedList<String> membersGekTableNames = new LinkedList<>();
        LinkedList<String> membersGekQuestions = new LinkedList<>();
        LinkedList<String> membersGekNames = new LinkedList<>();
        ObservableList<MemberGek> tmpList = memberGekTable.getItems();
        for (int i = 0; i<tmpList.size(); i++){
            membersGekTableNames.add(tmpList.get(i).getName());
            membersGekQuestions.add(tmpList.get(i).getQuestion());
        }
        for (int i =0; i<membersGek.size(); i++){
            membersGekNames.add(membersGek.get(i).getName());
        }
        System.out.println("Start making document");
        if (!Files.isDirectory(Paths.get("OutDocumentsVKR/"))) {
            Files.createDirectory(Paths.get("OutDocumentsVKR/"));
        }
        ProcessingDataVKR processingDataVKR = new ProcessingDataVKR(new VKRData(
                instituteName.getText(),
                chairName.getText(),
                courseNumber.getText(),
                courseName.getText(),
                protocolNumber.getText(),
                predsedatelName.getText(),
                secretaryName.getText(),
                membersGekTableNames,
                membersGekQuestions,
                membersGekNames,
                studentName.getText(),
                vkrName.getText(),
                nauchName.getText(),
                reviewerName.getText(),
                dateFull.getText(),
                VKRGrade.getValue().toString(),
                VKRType.getValue().toString()
        ));

        processingDataVKR.makeDocumentVKR();

    }

    public void initStudentData(Student student, LinkedList<Student> aStudents){
        studentName.setText(student.getName());
        vkrName.setText(student.getVkrName());
        nauchName.setText(student.getNauchName());
        students = new LinkedList<>(aStudents);
        vkrName.setEditable(false);
    }

    public void initCourseData(String aCourseNumber, String aCourseName, String aInstituteName, String aChairName){
        courseNumber.setText(aCourseNumber);
        courseName.setText(aCourseName);
        instituteName.setText(aInstituteName);
        chairName.setText(aChairName);
    }
    public void initGekData(LinkedList<MemberGek> aMembersGek, String aPredsedatel, String aSecretary){
        membersGek = new LinkedList<MemberGek>(aMembersGek);
        predsedatelName.setText(aPredsedatel);
        secretaryName.setText(aSecretary);
        memberGekOne.setText(membersGek.get(0).getName());
        memberGekTwo.setText(membersGek.get(1).getName());
        memberGekThree.setText(membersGek.get(2).getName());
        memberGekFour.setText(membersGek.get(3).getName());
        memberGekFive.setText(membersGek.get(4).getName());
    }
    public void initOtherData(LocalDate aDate, String aProtocolNumber){
        dateFull.setText(aDate.toString());
        protocolNumber.setText(aProtocolNumber);
    }

    @FXML
    public void addQuestionButton(ActionEvent event) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        LinkedList<String> memberNames = new LinkedList<>();
        for (int i = 0; i<membersGek.size(); i++){
            memberNames.add(membersGek.get(i).getName());
        }
        memberNames.add(predsedatelName.getText());

        FXMLLoader loaderSlave = new FXMLLoader(Main.class.getResource("/modules/addQuestion.fxml"));
        Scene scene = new Scene(loaderSlave.load());
        addQuestionController controller = loaderSlave.getController();
        controller.initMembersData(memberNames, memberGekTable);
        window.setScene(scene);
        window.setTitle("Добавить вопрос");
        window.showAndWait();
    }


}
