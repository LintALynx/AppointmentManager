package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.Logger;
import com.github.zeuschops.AppointmentManager.Backend.TableTimeData;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class WeeklyCalendarViewController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private MenuButton weekMenuButton;
    @FXML private TableView tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loading Weekly Calendar View");
        long start = System.nanoTime();
        Calendar.getInstance().setTime(new Date(System.currentTimeMillis()));
        String day = getDayOfWeek(Calendar.getInstance().getTime());
        day = day.substring(0,1).toUpperCase() + day.substring(1);
        titleLabel.setText(day);
        MenuItem miM = new MenuItem();
        miM.setText("Month");
        miM.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Main.dismissRecentStage();
                Main.newChildStage("CalendarView.fxml", "Calendar View");
            }
        });
        MenuItem miW = new MenuItem();
        miW.setText("Week");
        weekMenuButton.getItems().addAll(miM, miW);
        //Remove the "Action 1" and "Action 2" options from the DropDown button;
        weekMenuButton.getItems().remove(0);
        weekMenuButton.getItems().remove(0);

        tableView.setItems(FXCollections.observableList(CalendarViewController.ttds));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("time"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("sunday"));
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("monday"));
        ((TableColumn)(tableView.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("tuesday"));
        ((TableColumn)(tableView.getColumns().get(4))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("wednesday"));
        ((TableColumn)(tableView.getColumns().get(5))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("thursday"));
        ((TableColumn)(tableView.getColumns().get(6))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("friday"));
        ((TableColumn)(tableView.getColumns().get(7))).setCellValueFactory(new PropertyValueFactory<TableTimeData, String>("saturday"));
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded Weekly Calendar View");
    }

    private String getDayOfWeek(Date date) {
        Calendar.getInstance().setTime(date);
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Calendar.getInstance().setTime(new Date(System.currentTimeMillis()));
        switch(dayOfWeek) {
            case Calendar.SUNDAY:
                return "sunday";
            case Calendar.MONDAY:
                return "monday";
            case Calendar.TUESDAY:
                return "tuesday";
            case Calendar.WEDNESDAY:
                return "wednesday";
            case Calendar.THURSDAY:
                return "thursday";
            case Calendar.FRIDAY:
                return "friday";
            case Calendar.SATURDAY:
                return "saturday";
            default:
                return null;
        }
    }
}
