package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ConsultantAppointmentReportController implements Initializable {
    @FXML private TableView tableView;
    @FXML private Button okayButton;
    @FXML private Button cancelButton;
    @FXML private Button detailsButton;

    private ArrayList<FormattedCARContent> fcarcs = new ArrayList<FormattedCARContent>();
    public static FormattedCARContent selectedCARC = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HashMap<Integer, FormattedCARContent> hmFcarcs = new HashMap<Integer, FormattedCARContent>();
        for(Appointment a : ERDController.getInstance().getAppointments()) {
            if(hmFcarcs.getOrDefault(a.getCustomerId(), null) == null) {
                FormattedCARContent fc = new FormattedCARContent(ERDController.getInstance().getCustomer(a.getCustomerId()).getCustomerName(), a.getCustomerId(), 1);
                fcarcs.add(fc);
                hmFcarcs.put(a.getCustomerId(), fc);
            } else {
                FormattedCARContent fc = hmFcarcs.get(a.getCustomerId());
                int i = fcarcs.indexOf(fc); //indicating that the hashmap is being used to keep track of an arraylist... it's horribly inefficient tbqh..
                fc.setAppointmentCount(fc.getAppointmentCount() + 1);
                if(i > -1) {
                    fcarcs.get(i).setAppointmentCount(fc.getAppointmentCount());
                } else {
                    if(Main.langIdent == LanguageIdentifier.FRENCH) {
                        Main.newError("Erreur de Comptage de Rendez-vous", "Échec de la mise à jour du nombre de rendez-vous.");
                    } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                        Main.newError("Error de recuento de citas", "Error al actualizar el recuento de citas.");
                    } else {
                        Main.newError("Appointment Count Error", "Failed to update appointment count.");
                    }
                }
            }
        }
        tableView.setItems(FXCollections.observableList(fcarcs));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<FormattedCARContent, String>("customerName"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<FormattedCARContent, String>("customerId"));
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<FormattedCARContent, Integer>("appointmentCount"));
    }

    @FXML
    private void okayButtonAction(ActionEvent ae) {
        Main.dismissRecentStage();
    }

    @FXML
    private void cancelButtonAction(ActionEvent ae) {
        Main.dismissRecentStage();
    }

    @FXML
    private void detailsButtonAction(ActionEvent ae) {
        selectedCARC = fcarcs.get(tableView.getSelectionModel().getSelectedIndex());
        Main.newChildStage("ConsultantAppointmentReportDetailView.fxml", Main.langIdent == LanguageIdentifier.FRENCH ? "" : ( Main.langIdent == LanguageIdentifier.SPANISH ? "" : "Consultant Appointment Report Detail View"));
    }

    public class FormattedCARContent {
        private String customerName;
        private int customerId;
        private int appointmentCount;

        public FormattedCARContent(String customerName, int customerId) {
            this(customerName, customerId, -1);
        }

        public FormattedCARContent(String customerName, int customerId, int appointmentCount) {
            this.customerName = customerName;
            this.customerId = customerId;
            this.appointmentCount = appointmentCount;
        }

        public String getCustomerName() {
            return customerName;
        }

        public int getCustomerId() {
            return customerId;
        }

        public int getAppointmentCount() {
            return appointmentCount;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public void setAppointmentCount(int appointmentCount) {
            this.appointmentCount = appointmentCount;
        }
    }
}
