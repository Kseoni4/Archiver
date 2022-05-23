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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChooseInstituteController implements Initializable {

    private HashMap<String, LinkedList<String>> hashMapInstitute;
    private HashMap<String, LinkedList<String>> hashMapChair;
    private HashMap<String, LinkedList<String>> hashMapNapr;

    //Полное название института
    @FXML
    protected ComboBox<String> instituteName;

    //Полное название направления обучения
    @FXML
    protected ComboBox<String> courseNameFull;

    //Полное название кафедры
    @FXML
    protected ComboBox<String> chairName;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareHashMaps();
        instituteName.getItems().addAll(hashMapInstitute.keySet());
        chairName.getItems().addAll(hashMapChair.keySet());
        courseNameFull.getItems().addAll(hashMapNapr.keySet());

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
    void nextStepButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/modules/mainWindowsVKR.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Архивер. Версия 1.2:25/08/2021");
        window.setScene(scene);
        window.show();
    }

    private void prepareHashMaps() {
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
                /*for (String tmp : forInst){
                    System.out.println(tmp);
                }*/
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
