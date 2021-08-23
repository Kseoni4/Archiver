package main;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    private TextField chairName;

    @FXML
    private TextField chairNameFull;

    @FXML
    private TextField chairManName;

    @FXML
    private TextField instituteName;

    @FXML
    private TextField facultyName;

    @FXML
    private TextField headOfCommissionName;

    @FXML
    private ChoiceBox<String> studyForm;

    @FXML
    private ChoiceBox<String> vkrType;

    @FXML
    private TextField groupID;

    @FXML
    private TextField napravlenieID;

    @FXML
    private TextField antiplagiatSystem;

    @FXML
    private TextField yearOfGraduate;

    @FXML
    private Label workIndicator;

    @FXML
    private Button serializeDataBtn;

    @FXML
    private Button loadSerializedDataBtn;

    @FXML
    private TextField pathOfCSVFile;

    @FXML
    private TextField savedGroupIDFile;

    @FXML
    private Label fileNotFound;

    @FXML
    private Button filePickerBtn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button caseProcessingStartBtn;

    @FXML
    void loadSavedData(ActionEvent event) throws IOException, ClassNotFoundException {
        String groupIDFile = savedGroupIDFile.getText();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(groupIDFile+".sav"));
            BaseData loadSavedData = (BaseData) ois.readObject();
            putAllData(loadSavedData);
            ois.close();
        } catch (WriteAbortedException | FileNotFoundException e){
            fileNotFound.setText("Ошибка! Файл не найден!");
        }
    }

    private void putAllData(BaseData savedData){
        chairName.setText(savedData.ChairName);
        chairNameFull.setText(savedData.ChairNameFull);
        chairManName.setText(savedData.ChairManName);
        instituteName.setText(savedData.InstituteName);
        facultyName.setText(savedData.FacultyName);
        headOfCommissionName.setText(savedData.HeadOfCommissionName);
        groupID.setText(savedData.Group_ID);
        napravlenieID.setText(savedData.Napravlenie_ID);
        antiplagiatSystem.setText(savedData.AntiplagiatSystem);
        yearOfGraduate.setText(String.valueOf(savedData.YearOfGraduate));
        vkrType.setValue(savedData.VKRType);
        studyForm.setValue(savedData.StudyForm);
    }

    @FXML
    void makeDocuments(ActionEvent event) throws IOException, Docx4JException, CsvException, URISyntaxException {
        ProcessingData.loadBaseData(aggregateData());
        ArrayList<Student> studentsList = loadData(pathOfCSVFile.getText());
        for(Student student : studentsList){
            Main.loadTemplates();
            ProcessingData.makeDocuments(student);
        }
        workIndicator.setText("Готово!");
    }

    private BaseData aggregateData(){
        return new BaseData(chairName.getText(), chairNameFull.getText(), chairManName.getText(),
                headOfCommissionName.getText(), instituteName.getText(), facultyName.getText(),
                studyForm.getValue(),vkrType.getValue(), antiplagiatSystem.getText(),
                napravlenieID.getText(), groupID.getText(), Integer.parseInt(yearOfGraduate.getText()));
    }

    private static ArrayList<Student> loadData(String pathToCSV) throws IOException, CsvException {

        ArrayList<Student> studentsList = new ArrayList<>();
        Reader reader = Files.newBufferedReader(Paths.get(pathToCSV), Charset.forName("windows-1251"));

        CSVReader csvReader = new CSVReader(reader);
        List<String[]> listStrings;
        listStrings = csvReader.readAll();
        listStrings.remove(0);
        for(String[] infoLine : listStrings) {
            String line = Arrays.toString(infoLine).replaceAll("\\[|\\]","");
            studentsList.add(createNewStudent(line.split(";")));
            System.out.println("Created student " + studentsList.get(studentsList.size()-1).LastName);
        }
        csvReader.close();
        return studentsList;
    }

    private static Student createNewStudent(String[] data){
        String[] fio = data[0].split(" ");
        return new Student(fio[1], fio[2], fio[0],
                data[1], ProcessingData.baseData.Napravlenie_ID, ProcessingData.baseData.Group_ID,
                data[2], data[3], data[4], data[5], data[6], data[7]);
    }

    @FXML
    void pickCSVFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(Main.mainWindow);
        if(file != null) {
            pathOfCSVFile.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void saveInputData(ActionEvent event) throws IOException {
        BaseData baseDataForSave = aggregateData();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(baseDataForSave.Group_ID+".sav"));

        oos.writeObject(baseDataForSave);

        oos.flush();

        oos.close();
    }

    @FXML
    public void initialize(){
        studyForm.getItems().addAll("Очное", "Заочное");
        vkrType.getItems().addAll("дипломная работа", "бакалаврская работа", "магистрская работа");
    }

}
