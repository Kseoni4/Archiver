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
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

/**
 * Класс вредназначен для обработки событий окна, предназначенного для заполнения сведений,
 * которые далее будут использоваться на протяжении работы всей оставшейся программы
 */

public class ChooseInstituteController implements Initializable {

    private HashMap<String, LinkedList<String>> hashMapInstitute;
    private HashMap<String, LinkedList<String>> hashMapChair;
    private HashMap<String, LinkedList<String>> hashMapNapr;

    private LinkedList<GroupData> groupData;

    private LinkedList<MemberGek> memberGeks = new LinkedList<>();


    @FXML protected ComboBox<String> instituteName;

    @FXML protected ComboBox<String> courseNameFull;

    @FXML protected ComboBox<String> chairName;

    @FXML protected TextField predsedatel;

    @FXML protected TextField memberGek1;

    @FXML protected TextField memberGek2;

    @FXML protected TextField memberGek3;

    @FXML protected TextField memberGek4;

    @FXML protected TextField memberGek5;

    @FXML protected TextField secretary;

    @FXML protected TextField protocolNumber;

    @FXML protected DatePicker fullDate;

    @FXML protected TextField pageNumber;

    @FXML protected Label errorLabel1;
    @FXML protected Label errorLabel2;
    @FXML protected Label errorLabel3;
    @FXML protected Label errorLabel4;
    @FXML protected Label errorLabel5;
    @FXML protected Label errorLabel6;
    @FXML protected Label errorLabel7;
    @FXML protected Label errorLabel8;
    @FXML protected Label errorLabel9;
    @FXML protected Label errorLabel10;
    @FXML protected Label errorLabel11;
    @FXML protected Label errorLabel12;

    /**
     *
     * @param url
     * @param resourceBundle
     * Данный Метод инициализирует окно для первого представления пользователю некоторыми данными
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            prepareHashMaps();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instituteName.getItems().addAll(hashMapInstitute.keySet());
        chairName.getItems().addAll(hashMapChair.keySet());
        courseNameFull.getItems().addAll(hashMapNapr.keySet());
        LocalDate tmpDate = LocalDate.now();
        int year = tmpDate.getYear();
        protocolNumber.setText("01/"+(year%100));
        pageNumber.setText("1");

    }

    /**
     * Данный метод при выборе параметра в ComboBox перенастраивает данные в других ComboBox
     * @param event
     */
    @FXML
    public void instituteSelected(ActionEvent event){
        chairName.getItems().clear();
        chairName.getItems().addAll(hashMapInstitute.get(instituteName.getValue()));
    }
    /**
     * Данный метод при выборе параметра в ComboBox перенастраивает данные в других ComboBox
     * @param event
     */
    @FXML
    public void chairSelected(ActionEvent event){
        courseNameFull.getItems().clear();
        courseNameFull.getItems().addAll(hashMapChair.get(chairName.getValue()));
    }

    /**
     * Данный метод позволяет вернуться в предыдущее окно при клике на кнопку
     * @param event
     * @throws IOException
     */
    @FXML
    void changeWindowToChooseFileButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseFileVKRWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    /**
     * Данный метод позволяет при определенных условиях перейти в следующее окно при клике на кнопку
     * и передает в него необходимые данные
     * @param event
     * @throws IOException
     */
    @FXML
    void nextStepButton(ActionEvent event) throws IOException {
        if (isFilled()) {
            memberGeks.add(new MemberGek(memberGek1.getText()));
            memberGeks.add(new MemberGek(memberGek2.getText()));
            memberGeks.add(new MemberGek(memberGek3.getText()));
            memberGeks.add(new MemberGek(memberGek4.getText()));
            memberGeks.add(new MemberGek(memberGek5.getText()));
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseStudent.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ChooseStudentController controller = fxmlLoader.getController();
            controller.initGroupData(groupData);
            LinkedList<String> tmpLinked = new LinkedList<>(Arrays.stream(courseNameFull.getValue().split(";")).toList());
            controller.initCourseData(tmpLinked.get(1),tmpLinked.get(0), instituteName.getValue(), chairName.getValue());
            controller.initGekData(memberGeks, predsedatel.getText(), secretary.getText());
            controller.initOtherData(fullDate.getValue(), protocolNumber.getText(), Integer.parseInt(pageNumber.getText()));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Архивер. Версия 1.2:25/08/2021");
            window.setScene(scene);
            window.show();
        }
    }

    /**
     * Данный метод проверяет заполненные данные, и в случае их отсутствия выводит в в окно сообщение пользователю
     * @return
     */
    public boolean isFilled(){
        int i = 0;
        if (instituteName.getValue()==null){
            errorLabel1.setText("Не выбрано название института");
            i++;
        }
        if (chairName.getValue()==null){
            errorLabel2.setText("Не выбрано название кафедры");
            i++;
        }
        if (courseNameFull.getValue()==null){
            errorLabel3.setText("Не выбрано название направления");
            i++;
        }
        if (predsedatel.getText().isBlank()){
            errorLabel4.setText("Не введен председатель ГЭК");
            i++;
        }
        if (memberGek1.getText().isBlank()){
            errorLabel5.setText("Не введен первый член ГЭК");
            i++;
        }
        if (memberGek2.getText().isBlank()){
            errorLabel6.setText("Не введен второй член ГЭК");
            i++;
        }
        if (memberGek3.getText().isBlank()){
            errorLabel7.setText("Не введен третий член ГЭК");
            i++;
        }
        if (memberGek4.getText().isBlank()){
            errorLabel8.setText("Не введен четвертый член ГЭК");
            i++;
        }
        if (memberGek5.getText().isBlank()){
            errorLabel9.setText("Не введен пятый член ГЭК");
            i++;
        }

        if (secretary.getText().isBlank()){
            errorLabel10.setText("Не введен секретарь ГЭК");
            i++;
        }
        if (protocolNumber.getText().isBlank()){
            errorLabel11.setText("Не введен номер протокола");
            i++;
        }
        if (fullDate.getValue()==null){
            errorLabel12.setText("Не указана дата заседания");
            i++;
        }
        if (i==0){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Данный метод предназначен для получения инфомрации о группах и студентах из предыдущего окна
     * @param aGroupData
     */
    public void initGroupData(LinkedList<GroupData> aGroupData){
        groupData = new LinkedList<>(aGroupData);
    }

    /**
     * данный метод предназначен для подготовки корректной работы трех ComboBox с данными
     * Он заполняет три HashMap, каждый из которых соответствует своему ComboBox необходимой информацией
     * @throws IOException
     */
    private void prepareHashMaps() throws IOException {
        hashMapInstitute = new HashMap<>();

        hashMapNapr = new HashMap<>();
        hashMapChair = new HashMap<>();
        try (
                BufferedReader csvReader = new BufferedReader(new FileReader("csvTableExample.csv"))
        ){
            Optional<String> nextLine = Optional.ofNullable(csvReader.readLine());
            String instName;
            String chaName;
            String napName;
            while (((nextLine.isPresent())&&(!nextLine.get().isEmpty()))) {
                LinkedList<String> forInst = new LinkedList<>(Arrays.stream(nextLine.get().split(";")).toList());
                instName = forInst.remove(0);

                chaName = forInst.remove(0);

                napName = forInst.remove(0)+";"+ forInst.remove(0);


                addChair(instName, chaName);

                addNapr(chaName, napName);

                if (hashMapNapr.containsKey(napName)){
                    hashMapNapr.get(napName).addAll(forInst);
                } else {
                    hashMapNapr.put(napName, new LinkedList<>(forInst));
                }
                nextLine = Optional.ofNullable(csvReader.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Данный метод заполняет HashMap с ключом в виде названия Института
     * @param instituteName - название института
     * @param chairName - название кафедры в этом институте
     */
    private void addChair(String instituteName, String chairName) {
        if (hashMapInstitute.containsKey(instituteName)) {
            if (!(hashMapInstitute.get(instituteName).contains(chairName))) {
                hashMapInstitute.get(instituteName).add(chairName);
            }
        } else {
            hashMapInstitute.put(instituteName, new LinkedList<>(Collections.singleton(chairName)));
        }
    }

    /**
     * Данный метод заполняет HashMap с ключом в виде названия кафедры
     * @param chairName - название кафедры
     * @param napName - название направления у этой кафедры
     */
    private void addNapr(String chairName, String napName) {
        if (hashMapChair.containsKey(chairName)){
            hashMapChair.get(chairName).add(napName);
        } else {
            hashMapChair.put(chairName, new LinkedList<>(Collections.singleton(napName)));
        }

    }

}
