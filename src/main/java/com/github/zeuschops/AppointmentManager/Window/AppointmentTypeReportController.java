package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

public class AppointmentTypeReportController implements Initializable {
    @FXML private Label januaryLabel; //1
    @FXML private Label februaryLabel; //2
    @FXML private Label marchLabel; //3
    @FXML private Label aprilLabel; //4
    @FXML private Label mayLabel; //5
    @FXML private Label juneLabel; //6
    @FXML private Label julyLabel; //7
    @FXML private Label augustLabel; //8
    @FXML private Label septemberLabel; //9
    @FXML private Label octoberLabel; //10
    @FXML private Label novemberLabel; //11
    @FXML private Label decemberLabel; //12

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HashMap<String, Integer> monthCount = new HashMap<String, Integer>();
        monthCount.put("january",0);
        monthCount.put("february",0);
        monthCount.put("march",0);
        monthCount.put("april",0);
        monthCount.put("may",0);
        monthCount.put("june",0);
        monthCount.put("july",0);
        monthCount.put("august",0);
        monthCount.put("september",0);
        monthCount.put("october",0);
        monthCount.put("november",0);
        monthCount.put("december",0);

        for(Appointment a : ERDController.getInstance().getAppointments()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(a.getStartDate());
            switch(cal.get(cal.MONTH)) {
                case Calendar.JANUARY:
                    monthCount.replace("january", monthCount.get("january") + 1);
                    break;
                case Calendar.FEBRUARY:
                    monthCount.replace("february", monthCount.get("february") + 1);
                    break;
                case Calendar.MARCH:
                    monthCount.replace("march", monthCount.get("march") + 1);
                    break;
                case Calendar.APRIL:
                    monthCount.replace("april", monthCount.get("april") + 1);
                    break;
                case Calendar.MAY:
                    monthCount.replace("may", monthCount.get("may") + 1);
                    break;
                case Calendar.JUNE:
                    monthCount.replace("june", monthCount.get("june") + 1);
                    break;
                case Calendar.JULY:
                    monthCount.replace("july", monthCount.get("july") + 1);
                    break;
                case Calendar.AUGUST:
                    monthCount.replace("august", monthCount.get("august") + 1);
                    break;
                case Calendar.SEPTEMBER:
                    monthCount.replace("september", monthCount.get("september") + 1);
                    break;
                case Calendar.OCTOBER:
                    monthCount.replace("october", monthCount.get("october") + 1);
                    break;
                case Calendar.NOVEMBER:
                    monthCount.replace("november", monthCount.get("november") + 1);
                    break;
                case Calendar.DECEMBER:
                    monthCount.replace("december", monthCount.get("december") + 1);
                    break;
                default:
                    break;
            }
        }

        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            januaryLabel.setText("Janvier: " + monthCount.get("january") + " rendez-vous");
            februaryLabel.setText("Février: " + monthCount.get("february") + " rendez-vous");
            marchLabel.setText("Mars: " + monthCount.get("march") + " rendez-vous");
            aprilLabel.setText("Avril: " + monthCount.get("april") + " rendez-vous");
            mayLabel.setText("Mai: " + monthCount.get("may") + " rendez-vous");
            juneLabel.setText("Juin: " + monthCount.get("june") + " rendez-vous");
            julyLabel.setText("Julliet: " + monthCount.get("july") + " rendez-vous");
            augustLabel.setText("Aoút: " + monthCount.get("august") + " rendez-vous");
            septemberLabel.setText("Septembre: " + monthCount.get("september") + " rendez-vous");
            octoberLabel.setText("Octobre: " + monthCount.get("october") + " rendez-vous");
            novemberLabel.setText("Novembre: " + monthCount.get("november") + " rendez-vous");
            decemberLabel.setText("Décembre: " + monthCount.get("december") + " rendez-vous");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            januaryLabel.setText("Enero: " + monthCount.get("january") + " equipo");
            februaryLabel.setText("Febrero: " + monthCount.get("february") + " equipo");
            marchLabel.setText("Marzo: " + monthCount.get("march") + " equipo");
            aprilLabel.setText("Abril: " + monthCount.get("april") + " equipo");
            mayLabel.setText("Mayo: " + monthCount.get("may") + " equipo");
            juneLabel.setText("Junio: " + monthCount.get("june") + " equipo");
            julyLabel.setText("Julio: " + monthCount.get("july") + " equipo");
            augustLabel.setText("Agosto: " + monthCount.get("august") + " equipo");
            septemberLabel.setText("Septiembre: " + monthCount.get("september") + " equipo");
            octoberLabel.setText("Octubre: " + monthCount.get("october") + " equipo");
            novemberLabel.setText("Noviember: " + monthCount.get("november") + " equipo");
            decemberLabel.setText("Diciembre: " + monthCount.get("december") + " equipo");
        } else {
            januaryLabel.setText("January: " + monthCount.get("january") + " appointments");
            februaryLabel.setText("February: " + monthCount.get("february") + " appointments");
            marchLabel.setText("March: " + monthCount.get("march") + " appointments");
            aprilLabel.setText("April: " + monthCount.get("april") + " appointments");
            mayLabel.setText("May: " + monthCount.get("may") + " appointments");
            juneLabel.setText("June: " + monthCount.get("june") + " appointments");
            julyLabel.setText("July: " + monthCount.get("july") + " appointments");
            augustLabel.setText("August: " + monthCount.get("august") + " appointments");
            septemberLabel.setText("September: " + monthCount.get("september") + " appointments");
            octoberLabel.setText("October: " + monthCount.get("october") + " appointments");
            novemberLabel.setText("November: " + monthCount.get("november") + " appointments");
            decemberLabel.setText("December: " + monthCount.get("december") + " appointments");
        }
    }
}
