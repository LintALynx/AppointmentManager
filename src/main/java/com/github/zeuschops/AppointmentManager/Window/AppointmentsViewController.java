package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.FormattedAppointment;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.Backend.Logger;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import com.github.zeuschops.AppointmentManager.ERD.Customer;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentsViewController implements Initializable {
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private TableView tableView;

    public static Appointment selectedAppointment;

    @FXML
    private void addAppointment(ActionEvent event) {
        selectedAppointment = null;
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Ajouter un rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Añadir Cita");
        } else {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Add Appointment");
        }
        if(selectedAppointment != null) {
            Appointment sa = selectedAppointment;
            Customer cust = ERDController.getInstance().getCustomer(sa.getCustomerId());
            long length = (sa.getEndDate().getTime() - sa.getStartDate().getTime()) / (1000 * 60);
            FormattedAppointment f = new FormattedAppointment(sa.getUserId(), sa.getAppointmentId(),
                    sa.getType(), cust.getCustomerName(), length + " minutes",
                    sa.getStartDate().toString());
            fa.add(f);
        }
        tableView.requestFocus();
        tableView.refresh();
    }

    @FXML
    private void updateAppointment(ActionEvent event) {
        if(tableView.getSelectionModel().getSelectedIndex() < 0) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Aucun rendez-vous sélectionné", "Vous devez sélectionner un rendez-vous " +
                        "à modifier avant d'essayer de modifier un rendez-vous");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Ninguna cita seleccionada", "Debe seleccionar una cita para modificar " +
                        "antes de intentar modificar una cita");
            } else {
                Main.newError("No Appointment Selected", "You need to select an appointment to modify " +
                        "before attempting to modify an appointment");
            }
            return;
        }
        FormattedAppointment fas = (FormattedAppointment)(tableView.getSelectionModel().getSelectedItem());
        selectedAppointment = ERDController.getInstance().getAppointment(fas.getAppointmentId());
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Modifier un rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Modificar Cita");
        } else {
            Main.newChildStage("AppointmentSpecificsView.fxml", "Modify Appointment");
        }
        Appointment sa = selectedAppointment;
        long length = (sa.getEndDate().getTime() - sa.getStartDate().getTime()) / (1000 * 60);
        fas.setType(sa.getType());
        fas.setLength(length + " minutes");
        fas.setTime(sa.getStartDate().toString() + " minutes");
        Customer cust = ERDController.getInstance().getCustomer(sa.getCustomerId());
        fas.setCustomerName(cust.getCustomerName());
        tableView.requestFocus();
        tableView.refresh();
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            alert.setContentText("Está seguro de que desea eliminiar esta cita?");
        } else {
            alert.setContentText("Are you sure you would like to delete this appointment?");
        }
        //Similar to the Lambda in Main.java, but this one will check if the user selects yes then handle removing an
        //  appointment from the view. This saves quite a few more lines of code than Main.java with a similar lambda.
        //  Usefulness; 9/10.
        alert.showAndWait().filter(response -> response == ButtonType.YES).ifPresent(response -> {
            FormattedAppointment fApp = (FormattedAppointment)(tableView.getSelectionModel().getSelectedItem());
            Appointment app = ERDController.getInstance().getAppointment(fApp.getAppointmentId());
            ERDController.getInstance().deleteAppointment(app.getAppointmentId());
            for(int i = 0; i < fa.size(); i++) {
                if(fa.get(i).getAppointmentId() == app.getAppointmentId()) {
                    fa.remove(i);
                    break;
                }
            }
            tableView.requestFocus();
            tableView.refresh();
        });
    }

    private ArrayList<FormattedAppointment> fa = new ArrayList<FormattedAppointment>();

    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loading Appointments View");
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            addButton.setText("Ajouter");
            updateButton.setText("Modifier");
            deleteButton.setText("Supprimer");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            addButton.setText("Añadir");
            updateButton.setText("Actualizer");
            deleteButton.setText("Borrar");
        }

        ArrayList<Appointment> appointments = ERDController.getInstance().getAppointments();
        for(Appointment a : appointments) {
            Customer c = ERDController.getInstance().getCustomer(a.getCustomerId());
            String customerName = c.getCustomerName();
            long length = ((a.getEndDate().getTime() - a.getStartDate().getTime())/1000)/60;
            length = length - (length % 1);
            FormattedAppointment formattedAppointment = new FormattedAppointment(a.getUserId(), a.getAppointmentId(), a.getType(), customerName, length + " minutes", a.getStartDate().toString());
            fa.add(formattedAppointment);
        }

        tableView.setItems(FXCollections.observableList(fa));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<FormattedAppointment, String>("type"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<FormattedAppointment, String>("customerName"));
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<FormattedAppointment, String>("length"));
        ((TableColumn)(tableView.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<FormattedAppointment, String>("time"));
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded Appointments View");
    }
}
