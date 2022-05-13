/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package main;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modules.VKRApplication;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    public Tab singleTab;

    @FXML
    public Tab groupTab;

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
    private ChoiceBox<String> smkoType;

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
    private Label fileStatusLabel;

    @FXML
    private Button filePickerBtn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField studentFIO;

    @FXML
    private TextField headOfVKR;

    @FXML
    private DatePicker dateOfDefend;

    @FXML
    private ChoiceBox<String> score;

    @FXML
    private TextField personID;

    @FXML
    private TextField VKRTitle;

    @FXML
    private TextField protocolID;

    @FXML
    private TextField originality;

    @FXML
    public CheckBox allDocsCheck;

    @FXML
    public CheckBox checkTitleDoc;

    @FXML
    public CheckBox checkComDoc;

    @FXML
    public CheckBox checkAPDoc;

    @FXML
    public ChoiceBox<String> charsetBox;

    @FXML
    public Label errorMessage;

    @FXML
    private Button caseProcessingStartBtn;

    private boolean isGroup = true;

    private boolean isChecked = true;

    @FXML
    void checkAllDocs(Event event) {
        isChecked = !isChecked;

        checkTitleDoc.setSelected(isChecked);
        checkTitleDoc.setDisable(isChecked);
        checkComDoc.setSelected(isChecked);
        checkComDoc.setDisable(isChecked);
        checkAPDoc.setSelected(isChecked);
        checkAPDoc.setDisable(isChecked);
    }

    @FXML
    void loadSavedData(ActionEvent event) throws IOException, ClassNotFoundException {
        String groupIDFile = savedGroupIDFile.getText();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(groupIDFile+".sav"));
            BaseData loadSavedData = (BaseData) ois.readObject();
            putAllData(loadSavedData);
            ois.close();
        } catch (WriteAbortedException | FileNotFoundException e){
            fileStatusLabel.setText("Ошибка! Файл не найден!");
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
        smkoType.setValue(savedData.smkoType);
    }

    @FXML
    void makeDocuments(ActionEvent event) throws IOException, Docx4JException, CsvException {
        System.out.println("START PROCESSING DATA");

        ProcessingData.PersonCaseNumber = 0;

        if(!Files.isDirectory(Paths.get("OutDocuments/"+groupID.getText()+"/"))) {
            Files.createDirectory(Paths.get("OutDocuments/" + groupID.getText() + "/"));
            Files.createDirectory(Paths.get("OutDocuments/" + groupID.getText() + "/Merged/"));
        }

        workIndicator.setText("");
        errorMessage.setText("");

        ProcessingData.loadBaseData(aggregateData());

        ProcessingData.allDocsCheck = allDocsCheck.isSelected();
        ProcessingData.checkTitleDoc = checkTitleDoc.isSelected();
        ProcessingData.checkComDoc = checkComDoc.isSelected();
        ProcessingData.checkAPDoc = checkAPDoc.isSelected();
        try {
            if (isGroup) {
                try {
                    ArrayList<Student> studentsList = loadData(pathOfCSVFile.getText(), charsetBox.getValue());
                    for (Student student : studentsList) {
                        Main.loadTemplates();
                        ProcessingData.makeDocuments(student);
                    }
                } catch (IndexOutOfBoundsException e){
                    System.out.println("\u001b[38;5;196m"+e.getLocalizedMessage());
                    errorMessage.setText("Ошибка кодировки файла, выберите другую кодировку");
                    return;
                }
            } else {
                Student student = createNewStudent(getStudentData());
                Main.loadTemplates();
                ProcessingData.makeDocuments(student);
            }
        } catch (IOException e){
            errorMessage.setText("Произошла ошибка при формировании документов!");
            return;
        }

        System.out.println("END OF PROCESSING DATA");

        workIndicator.setText("Готово!");
    }

    private BaseData aggregateData(){
        if(smkoType.getValue().equals("Оба")){
            smkoType.setValue(ProcessingData.smkoBoth);
        }
        return new BaseData(chairName.getText(), chairNameFull.getText(), chairManName.getText(),
                headOfCommissionName.getText(), instituteName.getText(), facultyName.getText(),
                studyForm.getValue(),vkrType.getValue(), antiplagiatSystem.getText(),
                napravlenieID.getText(), groupID.getText(), Integer.parseInt(yearOfGraduate.getText()),
                smkoType.getValue());
    }

    private static ArrayList<Student> loadData(String pathToCSV, String charset) throws IOException, CsvException {

        ArrayList<Student> studentsList = new ArrayList<>();

        Reader reader = Files.newBufferedReader(Paths.get(pathToCSV), Charset.forName(charset));

        CSVReader csvReader = new CSVReader(reader);

        System.out.println("Open file ("+csvReader.getLinesRead()+" lines read)");

        List<String[]> listStrings;

        listStrings = csvReader.readAll();

        System.out.println("Get strings from csv file");

        System.out.println("Open file ("+csvReader.getLinesRead()+" lines read)");

        String[] row = listStrings.get(0);

        String row2 = Arrays.toString(row).substring(1).replaceAll("\\[|\\]", "");

        studentsList.add(createNewStudent(row2.split(";")));

        listStrings.remove(0);

        System.out.println("Created student " + studentsList.get(studentsList.size() - 1).LastName);

        if (!listStrings.isEmpty()) {
            for (String[] infoLine : listStrings) {
                String line = Arrays.toString(infoLine).replaceAll("\\[|\\]", "");
                studentsList.add(createNewStudent(line.split(";")));
                System.out.println("Created student " + studentsList.get(studentsList.size() - 1).LastName);
            }
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

    private String[] getStudentData(){
        String[] outputData = new String[8];
        outputData[0] = studentFIO.getText();
        outputData[1] = personID.getText();
        outputData[2] = VKRTitle.getText();
        outputData[3] = headOfVKR.getText();
        outputData[4] = originality.getText();
        outputData[5] = dateOfDefend.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        outputData[6] = protocolID.getText();
        outputData[7] = score.getValue();
        System.out.println(Arrays.toString(outputData).replaceAll("\\[|\\]", ""));
        return outputData;
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
        try {
            oos.writeObject(baseDataForSave);
        } catch (IOException e){
            fileStatusLabel.setText("Произошла ошибка при сохранении!");
        }
        oos.flush();

        oos.close();

        fileStatusLabel.setText("Файл успешно сохранён!");
    }

    @FXML
    void changeWindowToVKRButton(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(VKRApplication.class.getResource("mainWindowsVKR.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Отчет ВКР");
        window.setScene(scene);
        window.show();
    }

    @FXML
    void changeToGroup(Event event) {
        isGroup = true;
    }

    @FXML
    void changeToSingle(Event event) {
        isGroup = false;
    }


    @FXML
    public void initialize(){
        studyForm.getItems().addAll("Очная", "Заочная", "Очно-заочное");
        vkrType.getItems().addAll("дипломная работа", "бакалаврская работа", "магистрская работа");
        score.getItems().addAll("Отлично", "Хорошо", "Удовлетворительно", "Н/Я");
        smkoType.getItems().addAll("СМКО МИРЭА  8.5.1/03.П.42-20",
                "СМКО МИРЭА  7.5.1/03.П.30-19","Оба");
        charsetBox.getItems().addAll("UTF-8", "Windows-1251");
        charsetBox.setValue(charsetBox.getItems().get(0));

    }

}
