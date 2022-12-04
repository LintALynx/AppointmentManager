package com.github.zeuschops.AppointmentManager.Window;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

import com.github.zeuschops.AppointmentManager.Backend.DatabaseConnector;
import com.github.zeuschops.AppointmentManager.Backend.LMBIdentifier;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.Backend.Logger;
import com.github.zeuschops.AppointmentManager.ERD.User;
import com.github.zeuschops.AppointmentManager.Main;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class MainWindowController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private SplitMenuButton locationMenuButton;
    @FXML private SplitMenuButton languageSelector;

    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label locLabel;
    @FXML private Label langLabel;
    @FXML private Button submitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Opened MainWindowController");
        MenuItem miARHI = new MenuItem("Phoenix, Arizona");
        miARHI.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Main.lmbIdent = LMBIdentifier.PHOENIX;
                locationMenuButton.setText("Phoenix, Arizona");
            }
        });
        MenuItem miNY = new MenuItem("New York, New York");
        miNY.setOnAction(new EventHandler() {
            @Override public void handle(Event e) {
                Main.lmbIdent = LMBIdentifier.NEWYORK;
                locationMenuButton.setText("New York, New York");
            }
        });
        MenuItem miLond = new MenuItem("London, England");
        miLond.setOnAction(new EventHandler() {
            @Override public void handle(Event e) {
                Main.lmbIdent = LMBIdentifier.LONDON;
                locationMenuButton.setText("London, England");
            }
        });
        locationMenuButton.getItems().addAll(miARHI, miNY, miLond);

        MenuItem miEng = new MenuItem("English");
        miEng.setOnAction(new EventHandler() {
           @Override public void handle(Event e) {
               Main.langIdent = LanguageIdentifier.ENGLISH;
               langLabel.setText("Language:");
               locLabel.setText("Location:");
               usernameLabel.setText("Username:");
               passwordLabel.setText("Password:");
               languageSelector.setText("English");
           }
        });
        MenuItem miFre = new MenuItem("Francis");
        miFre.setOnAction(new EventHandler() {
           @Override public void handle(Event e) {
               Main.langIdent = LanguageIdentifier.FRENCH;
               langLabel.setText("Langue:");
               locLabel.setText("Localisation:");
               usernameLabel.setText("Nom d'utilisateur:");
               passwordLabel.setText("Mot de passe:");
               languageSelector.setText("Francis");
               submitButton.setText("Soumettre");
           }
        });
        MenuItem miSpan = new MenuItem("Espoñal");
        miSpan.setOnAction(new EventHandler() {
            @Override public void handle(Event e) {
                Main.langIdent = LanguageIdentifier.SPANISH;
                langLabel.setText("Lengua:");
                languageSelector.setText("Espoñal");
                locLabel.setText("Localización:");
                usernameLabel.setText("Nombre de usuario:");
                passwordLabel.setText("Contraseña:");
                submitButton.setText("Enviar");
            }
        });
        languageSelector.getItems().addAll(miEng, miFre, miSpan);

        Locale locale = Locale.getDefault();
        String lang = locale.getDisplayLanguage().substring(0,2);
        if(lang.equalsIgnoreCase("Fr")) {
            locationMenuButton.setText("London, England");
            Main.langIdent = LanguageIdentifier.FRENCH;
            langLabel.setText("Langue:");
            locLabel.setText("Localisation:");
            usernameLabel.setText("Nom d'utilisateur:");
            passwordLabel.setText("Mot de passe:");
            languageSelector.setText("Francis");
            submitButton.setText("Soumettre");
        } else if(lang.equalsIgnoreCase("Sp") || lang.equalsIgnoreCase("Es")) {
            Main.langIdent = LanguageIdentifier.SPANISH;
            langLabel.setText("Lengua:");
            languageSelector.setText("Espoñal");
            locLabel.setText("Localización:");
            usernameLabel.setText("Nombre de usuario:");
            passwordLabel.setText("Contraseña:");
            submitButton.setText("Enviar");
        }

        String loc = locale.getDisplayCountry();
        if(loc.equalsIgnoreCase("England")) {
            locationMenuButton.setText("London, England");
            Main.lmbIdent = LMBIdentifier.LONDON;
        }

        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded MainWindowController");
    }

    @FXML
    private void submitButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = xor(passwordField.getText());

        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Attempting Login");

        ResultSet rs = DatabaseConnector.getInstance().request("SELECT userId, userName, password FROM user" +
                " WHERE userName=\"" + username + "\" AND password=\"" + password + "\"");
        try {
            if(rs.next()) {
                if (rs.getString("username").equalsIgnoreCase(username) && rs.getString("password").equals(password)) { //There's a reason I'm calling this a temporary thing..
                    File file = new File(System.getProperty("user.dir") + "/src/main/resources/fxml/PostLoginWindow.fxml");
                    Parent root = FXMLLoader.load(file.toURI().toURL());
                    Main.mainStage.setScene(new Scene(root));
                } else {
                    usernameNotFound();
                }
            } else {
                usernameNotFound();
            }

            User usr = new User();
            usr.userName = rs.getString("username");
            usr.userId = rs.getInt("userId");
            Main.user = usr;
            Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Logged in.");
        } catch(Exception e) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Erreur", "Euh oh! Quelque chose d'inattendu vient de se passer. Essayez " +
                        "de vous reconnecter dans une minute.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Error", "¡Uh oh! Algo inesperado acaba de suceder. Intente iniciar " +
                        "sesión nuevamente en un minuto.");
            } else {
                Main.newError("Error", "Uh oh! Something unexpected just happened. Try logging in " +
                        "again in a minute...");
            }
            e.printStackTrace();
        }
    }

    private void usernameNotFound() {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newError("Nom d'utilisateur ou mot de passe invalide","Nom d'utilisateur ou mot de " +
                    "passe invalide, veuillez réessayer.");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newError("Usuario o contraseña invalido", "Nombre de usuario o contraseña no válidos," +
                    " intente nuevamente.");
        } else {
            Main.newError("Invalid Username/Password", "Username or Password invalid, please try again.");
        }
    }
    private String xor(String toXOR) {
        String toReturn = "";
        //This (xord) is in plain text, within the application and can be decrypted very, Very, VERY easily.. Please
        //      update uCertify to allocate space for passwords to be hashed, kthx.
        //   In case you don't understand, please change the datatype from varchar to binary for the user's password
        //      column in the MySQL database.
        String xord = "W3sternG0vern0r5Un1v3rsi7y!";
    	for(int i = 0; i < toXOR.length(); i++) {
    		toReturn += (char) (toXOR.charAt(i) ^ xord.charAt(i % xord.length()));
    	}
    	return toReturn;
    }
}
