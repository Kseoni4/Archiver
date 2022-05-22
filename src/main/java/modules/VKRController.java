/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package modules;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.MultiValuedMap;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class VKRController implements Initializable {
    // Полное название института (факультета)
    private HashMap<String, LinkedList<String>> hashMapInstitute;
    private HashMap<String, LinkedList<String>> hashMapChair;
    private HashMap<String, LinkedList<String>> hashMapNapr;

    @FXML
    protected ComboBox<String> instituteName;

    //Полное название направления обучения
    @FXML
    protected ComboBox<String> courseNameFull;

    //Полное название кафедры
    @FXML
    protected ComboBox<String> chairName;

    //Полное имя студента в Именительном падеже
    @FXML
    protected TextField studentName;

    //Полное название темы ВКР
    @FXML
    protected TextField VKRName;

    //Имя и должность Научного руководителя (д-р. техн. наук, профессор Иванов И.И.)
    @FXML
    protected TextField HeadOfVKRName;

    //Рецензент для ВКР
    @FXML
    protected TextField reviewerName;

    //Номер направления обучения (10.10.10)
    @FXML
    protected TextField courseName;

    //Дата заседания (вторник, 14 января, 2022)
    @FXML
    protected TextField dateFull;

    //Номер протокола
    @FXML
    protected TextField protocolNumber;

    //ВКР выполнена в виде:
    @FXML
    protected ChoiceBox VKRType;

    //Итоговая оценка
    @FXML
    protected ChoiceBox VKRGrade;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareHashMaps();
        instituteName.getItems().addAll(hashMapInstitute.keySet());
        chairName.getItems().addAll(hashMapChair.keySet());
        courseNameFull.getItems().addAll(hashMapNapr.keySet());
        VKRType.getItems().addAll("Бакалаврская работа (выпускная квалификационная работа бакалавра",
                    "Дипломный проект", "Дипломная работа", "Магистерская диссертация");
        VKRGrade.getItems().addAll("Неудовлетворительно", "Удовлетворительно", "Хорошо", "Отлично");

    }

    @FXML
    public void instituteSelected(ActionEvent event){
        chairName.getItems().clear();
        chairName.getItems().addAll(hashMapInstitute.get(instituteName.getValue()));
    }

    @FXML
    public void chairSelected(ActionEvent event){
        courseNameFull.getItems().clear();
        courseNameFull.getItems().addAll(hashMapChair.get(chairName.getValue()));
    }

    @FXML
    void changeWindowToChooseFileButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/chooseFileVKRWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void makeDocumentVKR(ActionEvent event) throws Exception {
        System.out.println("Start making document");
        if (!Files.isDirectory(Paths.get("OutDocumentsVKR/"))) {
            Files.createDirectory(Paths.get("OutDocumentsVKR/"));
        }
        ProcessingDataVKR processingDataVKR = new ProcessingDataVKR(new VKRData(
                instituteName.getValue(),
                courseNameFull.getValue(),
                chairName.getValue(),
                studentName.getText(),
                VKRName.getText(),
                HeadOfVKRName.getText(),
                reviewerName.getText(),
                courseName.getText(),
                dateFull.getText(),
                protocolNumber.getText(),
                VKRGrade.getValue().toString()
        ));

        processingDataVKR.makeDocumentVKR();

    }

    public void prepareHashMaps() {
        BufferedReader csvReader = null;
        hashMapInstitute = new HashMap<>();

        hashMapNapr = new HashMap<>();
        hashMapChair = new HashMap<>();
        try {
            csvReader = new BufferedReader(new FileReader("csvTableExample.csv"));
            String nextLine;
            String instName;
            String chaName;
            String napName;
            while ((nextLine = csvReader.readLine()) != null) {
                LinkedList<String> forInst = new LinkedList<>(Arrays.stream(nextLine.split(";")).toList());
                for (String tmp : forInst){
                    System.out.println(tmp);
                }
                instName = forInst.remove(0);
                chaName = forInst.remove(0);
                napName = forInst.remove(0)+" "+ forInst.remove(0);

                if (hashMapInstitute.containsKey(instName)){
                    if (!(hashMapInstitute.get(instName).contains(chaName))) {
                        hashMapInstitute.get(instName).add(chaName);
                    } else {}
                } else {
                    hashMapInstitute.put(instName, new LinkedList<String>(Collections.singleton(chaName)));
                }

                if (hashMapChair.containsKey(chaName)){
                    hashMapChair.get(chaName).add(napName);
                } else {
                    hashMapChair.put(chaName, new LinkedList<String>(Collections.singleton(napName)));
                }
                if (hashMapNapr.containsKey(napName)){
                    hashMapNapr.get(napName).addAll(forInst);
                } else {
                    hashMapNapr.put(napName, new LinkedList<>(forInst));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
