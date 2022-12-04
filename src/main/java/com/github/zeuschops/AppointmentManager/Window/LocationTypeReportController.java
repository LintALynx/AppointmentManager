package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LocationTypeReportController implements Initializable {
    @FXML private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<LTRObject> ltros = new ArrayList<LTRObject>();
        HashMap<String, LTRObject> hLtros = new HashMap<String, LTRObject>(); //idk why I used a hashmap here, but I did..
        for(Appointment a : ERDController.getInstance().getAppointments()) {
            if(hLtros.getOrDefault(a.getLocation(), null) != null) {
                hLtros.get(a.getLocation()).appointmentCount += 1;
            } else {
                LTRObject ltro = new LTRObject(a.getLocation());
                hLtros.put(a.getLocation(), ltro);
            }
        }
        for(String s : hLtros.keySet()) {
            ltros.add(hLtros.get(s));
        }
        tableView.setItems(FXCollections.observableList(ltros));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<LTRObject, String>("location"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<LTRObject, Integer>("appointmentCount"));
    }

    public class LTRObject {
        private String location;
        public int appointmentCount;

        public LTRObject(String location) {
            this.location = location;
            this.appointmentCount = 1;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setAppointmentCount(int appointmentCount) {
            this.appointmentCount = appointmentCount;
        }

        public String getLocation() {
            return location;
        }

        public int getAppointmentCount() {
            return appointmentCount;
        }

        public String toString() {
            return location + ": " + appointmentCount;
        }
    }
}
