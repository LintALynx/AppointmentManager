package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConsultantAppointmentReportDetailViewController implements Initializable {
    @FXML private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(ConsultantAppointmentReportController.selectedCARC != null) {
            ConsultantAppointmentReportController.FormattedCARContent fc = ConsultantAppointmentReportController.selectedCARC;
            ArrayList<Appointment> appointments = new ArrayList<Appointment>();
            for(Appointment as : ERDController.getInstance().getAppointments()) {
                if(as.getCustomerId() == fc.getCustomerId()) {
                    appointments.add(as);
                }
            }
            tableView.setItems(FXCollections.observableList(appointments));
            ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
            ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<Appointment, Date>("createDate"));
            ((TableColumn)(tableView.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<Appointment, Date>("endDate"));
        } else {
            Main.newError("Select a Consultant", "Error displaying information for Consultant, please select one on the previous window then press \"Details\".");
            Main.dismissRecentStage();
        }
    }
}
