package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {

    private static WordprocessingMLPackage personCaseFile;

    private static WordprocessingMLPackage concludeCommissionFile;

    private static WordprocessingMLPackage concludeAntiplagiatFile;

    public static Stage mainWindow;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        if(!Files.isDirectory(Paths.get("OutDocuments"))) {
            Files.createDirectory(Paths.get("OutDocuments"));
            Files.createDirectory(Paths.get("OutDocuments/Merged"));
        }
        primaryStage.setTitle("Архивер. Версия 1.1:24/08/2021");
        primaryStage.setScene(new Scene(root));
        mainWindow = primaryStage;
        primaryStage.show();
    }

    public static void loadTemplates() throws Docx4JException {

        personCaseFile = Docx4J.load(Main.class.getResourceAsStream("personal_file_template.docx"));
        concludeCommissionFile = Docx4J.load(Main.class.getResourceAsStream("concludeOfComission_template.docx"));
        concludeAntiplagiatFile = Docx4J.load(Main.class.getResourceAsStream("conclusionOfPlagiat_template.docx"));
    }

    public static WordprocessingMLPackage getPersonCaseTemplate(){
        return  personCaseFile;
    }

    public static WordprocessingMLPackage getConcludeCommissionTemplate(){
        return concludeCommissionFile;
    }

    public static WordprocessingMLPackage getConcludeOfPlagiatTemplate(){
        return concludeAntiplagiatFile;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
