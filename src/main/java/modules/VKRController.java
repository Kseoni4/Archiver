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
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;


import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class VKRController implements Initializable {

    //Полное имя студента в Именительном падеже
    @FXML protected Label studentName;

    //Полное имя студента в родительном падеже
    @FXML protected TextField studentNameRP;

    //Полное имя студента в дательном падеже
    @FXML protected TextField studentNameDP;

    //Полное название темы ВКР
    @FXML protected TextField vkrName;

    //Имя и должность Научного руководителя (д-р. техн. наук, профессор Иванов И.И.)
    @FXML protected Label nauchName;

    //Рецензент для ВКР
    @FXML protected TextField reviewerName;

    //Название института
    @FXML protected String instituteName;

    //Название кафедры
    @FXML protected TextField chairName;

    //Номер направления обучения (10.10.10)
    @FXML protected Label courseNumber;

    //Дата заседания (вторник, 14 января, 2022)
    @FXML protected Label dateText;

    //Номер протокола
    @FXML protected Label protocolNumber;

    //Присваиваемая квалификация
    @FXML protected TextField qualification;

    //Диплом с отличием или без
    @FXML protected ChoiceBox<String> diplom;

    //Председатель ГЭК
     protected String predsedatelName;

    //Секретарь ГЭК
     protected String secretaryName;

    //Первый член ГЭК
    protected String memberGekOne;

    //Второй член ГЭК
     protected String memberGekTwo;

    //Третий член ГЭК
     protected String memberGekThree;

    //Четвертый член ГЭК
     protected String memberGekFour;

    //Пятый член ГЭК
     protected String memberGekFive;

    //Название направления
    @FXML protected String courseName;

    //ВКР выполнена в виде:
    @FXML protected ChoiceBox<String> vkrType;

    //Характеристика студента
    @FXML protected TextArea specialOpinion;

    //Итоговая оценка
    @FXML protected ChoiceBox<String> vkrGrade;

    //Таблица с вопросами членов ГЭК
    @FXML protected TableView<MemberGek> memberGekTable;

    @FXML protected TableColumn<MemberGek,String> memberGekName;

    @FXML protected TableColumn<MemberGek,String> memberGekQuestion;

    @FXML protected Button createProtocolAttest;

    @FXML protected Button createProtocolVkr;

    @FXML protected Button openInWord;

    @FXML protected Button nextStudent;

    @FXML protected Button addQuest;

    //Данные о текущей группе
    private LinkedList<GroupData> groupData;

    //Данные о членах ГЭК
    private LinkedList<MemberGek> membersGek;

    //Объект для генерации документов
    private Optional<ProcessingDataVKR> processingDataVKR;

    private Optional<File> fileVkr;

    //Текущая дата
    private LocalDate date;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        vkrType.getItems().addAll("Бакалаврская работа (выпускная квалификационная работа бакалавра",
                    "Дипломный проект", "Дипломная работа", "Магистерская диссертация");
        vkrGrade.getItems().addAll("Неудовлетворительно", "Удовлетворительно", "Хорошо", "Отлично");
        memberGekName.setCellValueFactory(new PropertyValueFactory<MemberGek, String>("name"));
        memberGekQuestion.setCellValueFactory(new PropertyValueFactory<MemberGek,String>("question"));
        diplom.getItems().addAll("с отличием", "без отличия");
        specialOpinion.setText(" ");
        qualification.setText(" ");
        reviewerName.setText(" ");
    }

    @FXML
    void changeWindowToChooseStudentButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ChooseStudentController controller = fxmlLoader.getController();
        controller.initCourseData(courseNumber.getText(), courseName, instituteName, chairName.getText());
        controller.initGroupData(groupData);
        controller.initGekData(membersGek, predsedatelName, secretaryName);
        controller.initOtherData(date,incrementProtocolNumber());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.getIcons().add(new Image(getClass().getResourceAsStream("/icon/Archiverlogo.png")));
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void makeDocumentVKR(ActionEvent event) throws Exception {
        if (!isFilledVkr()) {
            disableButtons();
            prepareProcessingData();
            if (processingDataVKR.isPresent()) {
                fileVkr = Optional.ofNullable(processingDataVKR.get().makeDocumentVKR());
            }
            System.out.println("Документ ВКР сделан");
            enableButtons();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Сообщение об ошибке");
            alert.setHeaderText("Указаны не все данные");
            alert.setContentText("Для формирования протокола заполните поле Рецензента, Типа работы и Оценки");

            alert.showAndWait();
        }
    }

    @FXML
    void makeDocumentAtestacii(ActionEvent event) throws Exception {
        if (!isFilledAttest()) {
            disableButtons();
            prepareProcessingData();
            if (processingDataVKR.isPresent()) {
                processingDataVKR.get().makeDocumentAtestacii();
            }
            System.out.println("Документ аттестации сделан");
            enableButtons();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Сообщение об ошибке");
            alert.setHeaderText("Указаны не все данные");
            alert.setContentText("Для формирования протокола заплните поле Оценки, Квалификации и Типа диплома");
            alert.showAndWait();
        }
    }

    @FXML
    void openProtocolVkrInWord() throws IOException {
        if (fileVkr.isPresent()){
            Runtime rt = Runtime.getRuntime();
            fileVkr.get().getAbsolutePath();
            Process ps = rt.exec("rundll32 SHELL32.DLL,ShellExec_RunDLL winword.exe");
            if (ps.isAlive()){
                System.out.println("Вроде запустить должен");
            }
        }
    }

    public void initStudentData(Student student, LinkedList<GroupData> aGroupData){
        studentName.setText(student.getName());
        vkrName.setText(student.getVkrName());
        nauchName.setText(student.getNauchName());
        groupData = new LinkedList<>(aGroupData);
        vkrName.setEditable(false);
        grammaticalCaseName(student.getName());
    }

    public void initCourseData(String aCourseNumber, String aCourseName, String aInstituteName, String aChairName){
        courseNumber.setText(aCourseNumber);
        courseName = aCourseName;
        instituteName = aInstituteName;
        chairName.setText(aChairName);
    }
    public void initGekData(LinkedList<MemberGek> aMembersGek, String aPredsedatel, String aSecretary){
        membersGek = new LinkedList<MemberGek>(aMembersGek);
        predsedatelName = aPredsedatel;
        secretaryName = aSecretary;
        memberGekOne = membersGek.get(0).getName();
        memberGekTwo = membersGek.get(1).getName();
        memberGekThree = membersGek.get(2).getName();
        memberGekFour = membersGek.get(3).getName();
        memberGekFive = membersGek.get(4).getName();
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
        for (MemberGek memberGekTmp: membersGek){
            memberNames.add(memberGekTmp.getName());
        }
        memberNames.add(predsedatelName);

        FXMLLoader loaderSlave = new FXMLLoader(Main.class.getResource("/modules/addQuestion.fxml"));
        window.getIcons().add(new Image(getClass().getResourceAsStream("/icon/Archiverlogo.png")));
        Scene scene = new Scene(loaderSlave.load());
        AddQuestionController controller = loaderSlave.getController();
        controller.initMembersData(memberNames, memberGekTable);
        window.setScene(scene);
        window.setTitle("Добавить вопрос");
        window.showAndWait();
    }

    private void prepareProcessingData() throws IOException {

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

        if (!Files.isDirectory(Paths.get("OutDocumentsVKR/"))) {
            Files.createDirectory(Paths.get("OutDocumentsVKR/"));
        }
        VKRData tmpData = new VKRData(membersGekTableNames, membersGekQuestions,membersGekNames);

        tmpData.setInstituteName(instituteName);
        tmpData.setChairName(chairName.getText());
        tmpData.setCourseNumber(courseNumber.getText());
        tmpData.setCourseName(courseName);
        tmpData.setProtocolNumber(protocolNumber.getText());
        tmpData.setPredsedatelName(predsedatelName);
        tmpData.setSecretaryName(secretaryName);
        tmpData.setStudentName(studentName.getText());
        tmpData.setStudentNameDP(studentNameDP.getText());
        tmpData.setStudentNameRP(studentNameRP.getText());
        tmpData.setVkrName(vkrName.getText());
        tmpData.setNauchName(nauchName.getText());
        tmpData.setReviewerName(reviewerName.getText());
        tmpData.setDate(dateText.getText());
        tmpData.setVkrGrade(vkrGrade.getValue());
        tmpData.setVkrType(vkrType.getValue());
        tmpData.setStudentHar(vkrGrade.getValue());
        tmpData.setSpecialOpinion(specialOpinion.getText());
        tmpData.setDiplom(diplom.getValue());
        tmpData.setQualification(qualification.getText());

        processingDataVKR = Optional.ofNullable(new ProcessingDataVKR(tmpData));

    }
    private void  grammaticalCaseName(String fullName){

        String[] nameParts = fullName.split(" ");

        Petrovich sklonyatel = new Petrovich();

        String lastName = nameParts[0];
        String firstName = nameParts[1];
        String patronymicName;
        if(nameParts.length == 3) {
            patronymicName = nameParts[2];
        }else if(nameParts.length == 2){
            patronymicName = " ";
        }else{
            lastName = fullName;
            firstName = " ";
            patronymicName = " ";
        }

        Gender gender = sklonyatel.gender(patronymicName, Gender.Male);


        System.out.println("Склоняем " + lastName + " " + firstName + " " + patronymicName);
        System.out.println(gender);
        System.out.println("В Родительном падаеже:" +
                sklonyatel.say(lastName, NameType.LastName, gender, Case.Genitive) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Genitive) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Genitive));
        String tmpStudentNameRP = sklonyatel.say(lastName, NameType.LastName, gender, Case.Genitive) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Genitive) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Genitive);
        studentNameRP.setText(tmpStudentNameRP);
        System.out.println("В Дательном падаеже:" +
                sklonyatel.say(lastName, NameType.LastName, gender, Case.Dative) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Dative) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Dative));
        String tmpStudentNameDP = sklonyatel.say(lastName, NameType.LastName, gender, Case.Dative) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Dative) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Dative);
        studentNameDP.setText(tmpStudentNameDP);
    }

    public boolean isFilledAttest(){
        return ((vkrGrade.getValue() == null)||(diplom.getValue() == null)||(qualification.getText() == null));
    }

    public boolean isFilledVkr(){
        return ((reviewerName.getText()==null)||(vkrType.getValue() == null)||(vkrGrade.getValue()==null));
    }

    public void disableButtons(){
        createProtocolAttest.setDisable(true);
        createProtocolVkr.setDisable(true);
        addQuest.setDisable(true);
        nextStudent.setDisable(true);
        openInWord.setDisable(true);
    }

    public void enableButtons(){
        createProtocolAttest.setDisable(false);
        createProtocolVkr.setDisable(false);
        addQuest.setDisable(false);
        nextStudent.setDisable(false);
        openInWord.setDisable(false);
    }

    public String incrementProtocolNumber(){
        String[] strNumber = protocolNumber.getText().split("/");
        Integer intNumber = Integer.parseInt(strNumber[0]);
        intNumber++;
        if (intNumber<10){
            return "0"+intNumber+"/"+strNumber[1];
        } else {
            return intNumber+"/"+strNumber[1];
        }
    }

}
