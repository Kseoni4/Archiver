/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import com.github.petrovich4j.Case;
import com.github.petrovich4j.Gender;
import com.github.petrovich4j.NameType;
import com.github.petrovich4j.Petrovich;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML protected Label dateText;

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
    @FXML protected ChoiceBox<String> vkrType;

    //Характеристика студента
    @FXML protected TextArea studentHar;

    //Итоговая оценка
    @FXML protected ChoiceBox<String> vkrGrade;

    @FXML protected TableView<MemberGek> memberGekTable;
    @FXML protected TableColumn<MemberGek,String> memberGekName;
    @FXML protected TableColumn<MemberGek,String> memberGekQuestion;


    private LinkedList<GroupData> groupData;

    private LinkedList<MemberGek> membersGek;

    private Optional<ProcessingDataVKR> processingDataVKR = Optional.ofNullable(null);


    private LocalDate date;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        vkrType.getItems().addAll("Бакалаврская работа (выпускная квалификационная работа бакалавра",
                    "Дипломный проект", "Дипломная работа", "Магистерская диссертация");
        vkrGrade.getItems().addAll("Неудовлетворительно", "Удовлетворительно", "Хорошо", "Отлично");
        memberGekName.setCellValueFactory(new PropertyValueFactory<MemberGek, String>("name"));
        memberGekQuestion.setCellValueFactory(new PropertyValueFactory<MemberGek,String>("question"));

    }

    @FXML
    void changeWindowToChooseStudentButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ChooseStudentController controller = fxmlLoader.getController();
        controller.initCourseData(courseNumber.getText(), courseName.getText(), instituteName.getText(), chairName.getText());
        controller.initGroupData(groupData);
        controller.initGekData(membersGek, predsedatelName.getText(), secretaryName.getText());
        controller.initOtherData(date,protocolNumber.getText());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void makeDocumentVKR(ActionEvent event) throws Exception {
        prepareProcessingData();
        processingDataVKR.get().makeDocumentVKR();
    }

    @FXML
    void makeDocumentAtestacii(ActionEvent event) throws Exception {
        prepareProcessingData();
        processingDataVKR.get().makeDocumentAtestacii();

    }

    public void initStudentData(Student student, LinkedList<GroupData> aGroupData){
        studentName.setText(student.getName());
        vkrName.setText(student.getVkrName());
        nauchName.setText(student.getNauchName());
        groupData = new LinkedList<>(aGroupData);
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
        dateText.setText(aDate.toString());
        protocolNumber.setText(aProtocolNumber);
        date = aDate;
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
        AddQuestionController controller = loaderSlave.getController();
        controller.initMembersData(memberNames, memberGekTable);
        window.setScene(scene);
        window.setTitle("Добавить вопрос");
        window.showAndWait();
    }

    private void prepareProcessingData() throws IOException {
        if (processingDataVKR.isEmpty()) {
            LinkedList<String> membersGekTableNames = new LinkedList<>();
            LinkedList<String> membersGekQuestions = new LinkedList<>();
            LinkedList<String> membersGekNames = new LinkedList<>();
            ObservableList<MemberGek> tmpList = memberGekTable.getItems();
            for (int i = 0; i < tmpList.size(); i++) {
                membersGekTableNames.add(tmpList.get(i).getName());
                membersGekQuestions.add(tmpList.get(i).getQuestion());
            }
            for (int i = 0; i < membersGek.size(); i++) {
                membersGekNames.add(membersGek.get(i).getName());
            }
            //Main.logger.debug("Start mking document");
            if (!Files.isDirectory(Paths.get("OutDocumentsVKR/"))) {
                Files.createDirectory(Paths.get("OutDocumentsVKR/"));
            }
            VKRData tmpData = new VKRData(membersGekTableNames, membersGekQuestions,membersGekNames);
            tmpData.setInstituteName(instituteName.getText());
            tmpData.setChairName(chairName.getText());
            tmpData.setCourseNumber(courseNumber.getText());
            tmpData.setCourseName(courseName.getText());
            tmpData.setProtocolNumber(protocolNumber.getText());
            tmpData.setPredsedatelName(predsedatelName.getText());
            tmpData.setSecretaryName(secretaryName.getText());
            tmpData.setStudentName(studentName.getText());
            tmpData.setVkrName(vkrName.getText());
            tmpData.setNauchName(nauchName.getText());
            tmpData.setReviewerName(reviewerName.getText());
            tmpData.setDate(dateText.getText());
            tmpData.setVkrGrade(vkrGrade.getValue());
            tmpData.setVkrType(vkrType.getValue());
            tmpData.setStudentHar(studentHar.getText());
            processingDataVKR = Optional.ofNullable(new ProcessingDataVKR(tmpData));
        }

    }
    private void  grammaticalCaseName(String fullName){

        String[] nameParts = fullName.split(" ");

        Petrovich sklonyatel = new Petrovich();

        String lastName = nameParts[0];
        String firstName = nameParts[1];
        String patronymicName = nameParts[2];

        Gender gender = sklonyatel.gender(patronymicName, Gender.Both);

        System.out.println("Склоняем " + lastName + " " + firstName + " " + patronymicName);
        System.out.println(gender);
        System.out.println("В Родительном падаеже:" +
                sklonyatel.say(lastName, NameType.LastName, gender, Case.Genitive) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Genitive) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Genitive));
        System.out.println("В Дательном падаеже:" +
                sklonyatel.say(lastName, NameType.LastName, gender, Case.Dative) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Dative) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Dative));
    }

}
