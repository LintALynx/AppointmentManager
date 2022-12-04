package com.github.zeuschops.AppointmentManager;

import com.github.zeuschops.AppointmentManager.Backend.LMBIdentifier;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.Backend.Logger;
import com.github.zeuschops.AppointmentManager.ERD.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.github.zeuschops.AppointmentManager.Backend.DatabaseConnector;

public class Main extends Application {

    public static ArrayList<Stage> childStages = new ArrayList<Stage>();

    public static Stage mainStage;
    public static DatabaseConnector DATABASEINSTANCE;
    public static LMBIdentifier lmbIdent = LMBIdentifier.PHOENIX;
    public static LanguageIdentifier langIdent = LanguageIdentifier.ENGLISH;
    public static User user = null;

    public static Logger LOGGERINSTANCE = new Logger("user-activity.txt");

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/fxml/MainWindow.fxml");
        Parent root = FXMLLoader.load(file.toURI().toURL());
        Main.mainStage = primaryStage;
        primaryStage.setTitle("AppointmentManager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void newChildStage(String stageName, String windowTitle) {
        LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Created Window: " + stageName + ", with title: " + windowTitle);
        try {
            Stage stage = new Stage();
            childStages.add(stage);
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/fxml/" + stageName);
            Parent root = FXMLLoader.load(file.toURI().toURL());
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(windowTitle);
            stage.showAndWait();
        } catch(IOException ioe) {
            childStages.remove(childStages.size() - 1);
            newError("IOException", "Cannot make new window, " + ioe.getCause());
            ioe.printStackTrace();
        }
    }

    public static void dismissRecentStage() {
        if(childStages.size() > 0) {
            Stage s = childStages.get(childStages.size() - 1);
            s.close();
            childStages.remove(s);
        }
    }

    public static void newError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setContentText(message);
        //This lambda saves about 3 lines of code, but the usefulness of this is about a 7/10 given its placement and
        //  purpose. Generally, I don't really like lambdas but this is simple enough and doesn't cause any threading
        //  issues so I'm happy enough with it.
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
            LOGGERINSTANCE.log(Logger.LoggingLevel.EXCEPTION, message);
        });
    }

    public static void newAlert(String alertTitle, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(alertTitle);
        alert.setContentText(message);
        alert.showAndWait();
        LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, message);
    }

    public static void main(String[] args) {
    	DATABASEINSTANCE = DatabaseConnector.getInstance();
        launch(args);
    }
}
