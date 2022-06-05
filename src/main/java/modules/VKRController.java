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


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

/**
 * Класс предназначен для обработки событий с основного окна. В нем реализовано добавление вопросов членов ГЭК,
 * Формирование документов, заполнение последней необходимой информации и так далее
 */

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
    private String qualification;

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
    private String vkrType;

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

    private GroupData group;

    //Данные о членах ГЭК
    private LinkedList<MemberGek> membersGek;

    //Объект для генерации документов
    private Optional<ProcessingDataVKR> processingDataVKR;

    private Optional<File> fileVkr;

    //Текущая дата
    private LocalDate date;

    private int pageNumberVkr;

    private int pageNumberAttest;

    /**
     * Первоначальная инициализация окна
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        vkrGrade.getItems().addAll("Неудовлетворительно", "Удовлетворительно", "Хорошо", "Отлично");
        memberGekName.setCellValueFactory(new PropertyValueFactory<MemberGek, String>("name"));
        memberGekQuestion.setCellValueFactory(new PropertyValueFactory<MemberGek,String>("question"));
        diplom.getItems().addAll("с отличием", "без отличия");
        specialOpinion.setText(" ");
        reviewerName.setText(" ");
    }

    /**
     * Метод предназначен для возврата к выбору студентов и передачи туда необходимых данных
     * @param event
     * @throws IOException
     */
    @FXML
    void changeWindowToChooseStudentButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ChooseStudentController controller = fxmlLoader.getController();
        controller.initCourseData(courseName, courseNumber.getText(), instituteName, chairName.getText());
        controller.initGroupData(groupData);
        controller.initGekData(membersGek, predsedatelName, secretaryName);
        controller.initOtherData(date,incrementProtocolNumber(), pageNumberVkr, pageNumberAttest, qualification, vkrType);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.getIcons().add(new Image(getClass().getResourceAsStream("/icon/Archiverlogo.png")));
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    /**
     * Метод предназначен для обработки события клика по кнопке. Метод запускает формирование протокола ВКР,
     * при определенных условиях.
     * Если условия не выполнены, то появляется окно с сообщением об ошибке
     * @param event
     * @throws Exception
     */
    @FXML
    void makeDocumentVKR(ActionEvent event) throws Exception {
        if (!isFilledVkr()) {
            disableButtons();
            prepareProcessingData();
            if (processingDataVKR.isPresent()) {
                fileVkr = Optional.ofNullable(processingDataVKR.get().makeDocumentVKR());
                System.out.println("Документ ВКР сделан");
                pageNumberVkr+=3;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация для пользователя");
            alert.setHeaderText("Успех");
            alert.setContentText("Протокол ВКР успешно сформирован");
            alert.showAndWait();
            enableButtons();
            createProtocolVkr.setVisible(false);
            addToCsvFile();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Сообщение об ошибке");
            alert.setHeaderText("Указаны не все данные");
            alert.setContentText("Для формирования протокола заполните поле Рецензента и Оценки");
            alert.showAndWait();
        }
    }

    /**
     *  Метод предназначен для обработки события клика по кнопке. Метод запускает формирование протокола Атестации,
     *  при определенных условиях.
     *  Если условия не выполнены, то появляется окно с сообщением об ошибке
     * @param event
     * @throws Exception
     */
    @FXML
    void makeDocumentAtestacii(ActionEvent event) throws Exception {
        if (!isFilledAttest()) {
            disableButtons();
            prepareProcessingData();
            if (processingDataVKR.isPresent()) {
                processingDataVKR.get().makeDocumentAtestacii();
                System.out.println("Документ аттестации сделан");
                pageNumberAttest+=2;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация для пользователя");
            alert.setHeaderText("Успех");
            alert.setContentText("Протокол аттестации успешно сформирован");
            alert.showAndWait();
            createProtocolAttest.setVisible(false);
            enableButtons();
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Сообщение об ошибке");
            alert.setHeaderText("Указаны не все данные");
            alert.setContentText("Для формирования протокола заплните поле Оценки и Типа диплома");
            alert.showAndWait();
        }
    }

    /**
     * Метод предназначен для открытия только что сформированного документа (протокол ВКР) в Microsoft Word
     * @throws IOException
     */
    @FXML
    void openProtocolVkrInWord() throws IOException {
        if (fileVkr.isPresent()){
            Runtime rt = Runtime.getRuntime();
            fileVkr.get().getAbsolutePath();
            String path = new String(fileVkr.get().getAbsolutePath());
            System.out.println(path);
            Process ps = rt.exec("rundll32 SHELL32.DLL,ShellExec_RunDLL winword.exe " + path);
            //Process ps = rt.exec(fileVkr.get().getAbsolutePath());
            if (ps.isAlive()){
                System.out.println("Вроде запустить должен");
            }
        }
    }

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param student - объект студент
     * @param aGroupData - полный список групп
     */
    public void initStudentData(Student student, LinkedList<GroupData> aGroupData, GroupData aGroup, String aQualification, String aVkrType){
        qualification = aQualification;
        vkrType = aVkrType;
        studentName.setText(student.getName());
        vkrName.setText(student.getVkrName());
        nauchName.setText(student.getNauchName());
        groupData = new LinkedList<>(aGroupData);
        group = aGroup;
        vkrName.setEditable(false);
        grammaticalCaseName(student.getName());
    }

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param aCourseNumber - номер направления
     * @param aCourseName - название направления
     * @param aInstituteName - название института
     * @param aChairName - название кафедры
     */
    public void initCourseData(String aCourseNumber, String aCourseName, String aInstituteName, String aChairName){
        courseNumber.setText(aCourseNumber);
        courseName = aCourseName;
        instituteName = aInstituteName;
        chairName.setText(aChairName);
    }

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param aMembersGek - список членов ГЭК
     * @param aPredsedatel - имя Председателя ГЭК
     * @param aSecretary - имя секретаря ГЭК
     */
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

    /**
     * Метод предназначен для получения данных из предыдущего окна
     * @param aDate - выбранная ранее дата
     * @param aProtocolNumber - номер протокола
     */
    public void initOtherData(LocalDate aDate, String aProtocolNumber, int aPageNumberVkr, int aPageNumberAttest){
        dateText.setText(aDate.toString());
        protocolNumber.setText(aProtocolNumber);
        date = aDate;
        pageNumberVkr = aPageNumberVkr;
        pageNumberAttest = aPageNumberAttest;
    }

    /**
     * Метод при клике на кнопку "Добавить вопрос" запускает новое окно добавления вопросов,
     * от членов ГЭК
     * @param event
     * @throws IOException
     */
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

    /**
     * Метод подготавливает данные для формирования протоколов
     * @throws IOException
     */
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
        tmpData.setVkrType(vkrType);
        tmpData.setStudentHar(vkrGrade.getValue());
        tmpData.setSpecialOpinion(specialOpinion.getText());
        tmpData.setDiplom(diplom.getValue());
        tmpData.setQualification(qualification);
        tmpData.setPageNumberVkr(pageNumberVkr);
        tmpData.setPageNumberAttest(pageNumberAttest);

        processingDataVKR = Optional.ofNullable(new ProcessingDataVKR(tmpData));

    }

    /**
     * Метод предназначен для генерации ФИО студента в Дательном и Родительном падежах
     * @param fullName
     */
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
        String tmpStudentNameRP = sklonyatel.say(lastName, NameType.LastName, gender, Case.Genitive) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Genitive) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Genitive);
        studentNameRP.setText(tmpStudentNameRP);

        String tmpStudentNameDP = sklonyatel.say(lastName, NameType.LastName, gender, Case.Dative) + " " +
                sklonyatel.say(firstName, NameType.FirstName, gender, Case.Dative) + " " +
                sklonyatel.say(patronymicName, NameType.PatronymicName, gender, Case.Dative);
        studentNameDP.setText(tmpStudentNameDP);
    }

    /**
     * Метод возвращает true или false в зависимости от того, выполнены ли условия для формирования
     * протокола Аттестации
     * @return
     */
    public boolean isFilledAttest(){
        return ((vkrGrade.getValue() == null)||(diplom.getValue() == null));
    }

    /**
     * Метод возвращает true или false в зависимости от того, выполнены ли условия для формирования
     * протокола ВКР
     * @return
     */
    public boolean isFilledVkr(){
        return ((reviewerName.getText()==null)||(vkrGrade.getValue()==null));
    }

    /**
     * Метод отключает все кнопки на окне
     */
    public void disableButtons(){
        createProtocolAttest.setDisable(true);
        createProtocolVkr.setDisable(true);
        addQuest.setDisable(true);
        nextStudent.setDisable(true);
        openInWord.setDisable(true);
    }

    /**
     * Метод включает все кнопки на окне
     */
    public void enableButtons(){
        createProtocolAttest.setDisable(false);
        createProtocolVkr.setDisable(false);
        addQuest.setDisable(false);
        nextStudent.setDisable(false);
        openInWord.setDisable(false);
    }

    /**
     * Метод увеличивает номер протокола на единицу
     * @return
     */
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

    public void addToCsvFile() throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(group.getName() + ".csv", true));) {
            bufferedWriter.write(studentName.getText() + ";" + "0" + ";" + vkrName.getText() + ";" + nauchName.getText() + ";" + "0" + ";" + protocolNumber.getText() + ";" + vkrGrade.getValue()+"\n");
            bufferedWriter.flush();
        }
    }

}
