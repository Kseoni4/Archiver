/*
 * Copyright (c) Ксенофонтов Николай Валерьевич
 * Кафедра КБ-4
 */

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.logging.impl.SLF4JLog;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>
 *      Java-приложение для формирования личных документов для архива
 *      Список формируемых документов:
 *      <list>
 *          <br>
 *          * Титульный лист
 *          <br>
 *          * Справка о допуске к защите
 *          <br>
 *          * Заключение по анти-плагиату
 *          <br>
 *      </list>
 *      Подоготовленные шаблоны содержат специальный текст-маркер для заполнения соответствующими данными.
 *      @see Placeholders
 *
 * </p>
 * @author Ксенофонтов Николай Валерьевич
 * @author Кафедра КБ-4
 * @version 1.2
 */
public class Main extends Application {

    private static WordprocessingMLPackage personCaseFile;

    private static WordprocessingMLPackage concludeCommissionFile;

    private static WordprocessingMLPackage concludeAntiplagiatFile;

    public static Stage mainWindow;

    //public static Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        char c = 'A'+1;
        String s1 = "abc";
        String s2 = "bca";
        System.out.println("Java" + 1 + 2 + 3);
        Parent root = FXMLLoader.load(getClass().getResource("/main/mainWindow.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/Archiverlogo.png")));
        if(!Files.isDirectory(Paths.get("OutDocuments"))) {
            Files.createDirectory(Paths.get("OutDocuments"));
        }
        primaryStage.setTitle("Архивер. Версия 1.2:25/08/2021");
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
