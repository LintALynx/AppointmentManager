package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class PostLoginWindowController implements Initializable {
    @FXML private Button calButton;
    @FXML private Button appButton;
    @FXML private Button recButton;
    @FXML private Button consultantReportButton;
    @FXML private Button appointmentReportButton;

    //@Override
    public void initialize(URL location, ResourceBundle resources) {
        ERDController.getInstance(); //Force it to load in and cache data
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            calButton.setText("Calendrier");
            appButton.setText("Rendez-vous");
            recButton.setText("Dossier Client");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            calButton.setText("Calendario");
            appButton.setText("Cita");
            recButton.setText("Registros de clientes");
        }
        //else: don't touch anything
    }

    @FXML
    private void calendarButton(ActionEvent ae) {
        Main.newChildStage("CalendarView.fxml", Main.langIdent == LanguageIdentifier.SPANISH ? "Calendario" : (Main.langIdent == LanguageIdentifier.FRENCH ? "Calendrier" : "Calendar"));
    }

    @FXML
    private void consultantReportButtonAction(ActionEvent ae) {
        Main.newChildStage("ConsultantAppointmentReport.fxml", Main.langIdent == LanguageIdentifier.FRENCH ? "Rapport de Nomination du Consultant" : (Main.langIdent == LanguageIdentifier.SPANISH ? "Informe de nombramiento de consultor" : "Consultant Appointment Report"));
    }

    @FXML
    private void appointmentsButton(ActionEvent ae) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("AppointmentsView.fxml", "Rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("AppointmentsView.fxml", "Cita");
        } else {
            Main.newChildStage("AppointmentsView.fxml", "Appointments");
        }
    }

    @FXML
    private void customerRecordsButton(ActionEvent ae) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("CustomerSelectionView.fxml", "Dossier Client");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("CustomerSelectionView.fxml", "Registros de Clientes");
        } else {
            Main.newChildStage("CustomerSelectionView.fxml", "Customer Records");
        }
    }

    @FXML
    private void generateReportAction(ActionEvent ae) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("AppointmentTypeReport.fxml", "Rapport sur le type de rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("AppointmentTypeReport.fxml", "Informe de tipo de cita");
        } else {
            Main.newChildStage("AppointmentTypeReport.fxml", "Appointment Type Report");
        }
    }

    @FXML
    private void locationReportButtonAction(ActionEvent ae) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("LocationTypeReport.fxml", "Rapport de type d'emplacement");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("LocationTypeReport.fxml", "Informe de tipo de ubicación");
        } else {
            Main.newChildStage("LocationTypeReport.fxml", "Location Type Report");
        }
    }
}
