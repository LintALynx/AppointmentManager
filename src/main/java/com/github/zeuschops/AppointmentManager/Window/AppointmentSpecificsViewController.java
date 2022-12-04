package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.LMBIdentifier;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.ERD.Address;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class AppointmentSpecificsViewController implements Initializable {
    @FXML private TextField typeField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;

    @FXML private Label typeLabel;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label customerLabel;
    @FXML private Label startTimeLabel;
    @FXML private Label endTimeLabel;

    @FXML private Button selectCustomerButton;
    @FXML private MenuButton selectStartTime;
    @FXML private MenuButton selectEndTime;
    @FXML private DatePicker datePicker;

    @FXML private Button submitButton;

    private ArrayList<MenuItem> timeMenuButtonOptions = new ArrayList<MenuItem>();

    @FXML
    private void submitButtonAction(ActionEvent event) {
        if(CustomerSelectionView.customer != null) {
            int cusId = CustomerSelectionView.customer.getCustomerId();
            int usrId = Main.user.userId;
            String title = titleField.getText();
            String desc = descriptionField.getText();
            String loc = Main.lmbIdent == LMBIdentifier.PHOENIX ? "Phoenix, Arizona" : (Main.lmbIdent == LMBIdentifier.LONDON ? "London, England" : "New York, New York");
            Address add = ERDController.getInstance().getAddress(CustomerSelectionView.customer.getAddressId());
            String cont = add.getPhoneNumber();
            String type = typeField.getText();
            String url = "";
            Timestamp start = null;
            Timestamp end = null;
            if(selectStartTime.getText().indexOf(":") > -1 && selectEndTime.getText().indexOf(":") > -1) {
                Calendar cal = Calendar.getInstance();
                java.util.Date date = Date.from(Instant.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault())));
                cal.setTime(date);
                if(AppointmentsViewController.selectedAppointment != null && selectStartTime.getText().contains(" "))
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), Integer.parseInt(selectStartTime.getText().split(" ")[1].split(":")[0]), Integer.parseInt(selectStartTime.getText().split(" ")[1].split(":")[1]));
                else
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), Integer.parseInt(selectStartTime.getText().split(":")[0]), Integer.parseInt(selectStartTime.getText().split(":")[1]));
                start = new Timestamp(cal.getTime().getTime());
                if(AppointmentsViewController.selectedAppointment != null && selectEndTime.getText().contains(" "))
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), Integer.parseInt(selectEndTime.getText().split(" ")[1].split(":")[0]), Integer.parseInt(selectEndTime.getText().split(" ")[1].split(":")[1]));
                else
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), Integer.parseInt(selectEndTime.getText().split(":")[0]), Integer.parseInt(selectEndTime.getText().split(":")[1]));
                end = new Timestamp(cal.getTime().getTime());
            } else {
                //error out
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Soumission", "Les heures de début ou de fin ne sont pas sélectionnées");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error de Envío", "Las horas de inicio o finalización no están seleccionadas");
                } else {
                    Main.newError("Submission Error", "Start or End times are not selected");
                }
                return;
            }
            for(Appointment a : ERDController.getInstance().getAppointments()) {
                if((a.getStartDate().getTime() >= start.getTime() && a.getLocation().equalsIgnoreCase(Main.lmbIdent.toString()))
                        || (a.getStartDate().getTime() <= start.getTime() && a.getEndDate().getTime() >= start.getTime())
                        || (a.getStartDate().getTime() <= end.getTime() && a.getEndDate().getTime() >= end.getTime())) {
                    if(Main.langIdent == LanguageIdentifier.FRENCH) {
                        Main.newError("Rendez-vous Préexistant", "Rendez-vous préablable trouvé, " +
                                "impossible d'en ajouter un autre!");
                    } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                        Main.newError("Cita Previa!", "Cita previa encontrada, no se puede agregar otra!");
                    } else {
                        Main.newError("Pre-Existing appointment", "Prior appointment found, cannot add another!");
                    }
                    return;
                }
            }
            Date create = new Date(System.currentTimeMillis());
            String createdBy;
            if(Main.user != null) {
                createdBy = Main.user.userName;
            } else {
                //error out
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("Erreur de Soumission", "Impossible de trouver l'utilisateur connecté.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("Error de Envío", "No se pudo encontrar el usuario conectado.");
                } else {
                    Main.newError("Submission Error", "Could not find logged in user in Main.java.");
                }
                return;
            }
            long hour = 1000 * 60 * 60;
            long day = hour * 24;
            String hourStr = selectStartTime.getText();
            long tod = 0;
            if(hourStr.indexOf(" ") > 0) {
                tod = (Integer.parseInt(hourStr.split(" ")[1].split(":")[0]) * hour)
                    + (Integer.parseInt(hourStr.split(" " )[1].split(":")[1]) * 1000 * 60);
            } else {
                tod = (Integer.parseInt(hourStr.split(":")[0]) * hour)
                        + (Integer.parseInt(hourStr.split(":")[1]) * 1000 * 60);
            }

            long toe = 0;
            hourStr = selectEndTime.getText();
            if(hourStr.indexOf(" ") > 0) {
                toe = (Integer.parseInt(hourStr.split(" ")[1].split(":")[0]) * hour)
                        + (Integer.parseInt(hourStr.split(" " )[1].split(":")[1]) * 1000 * 60);
            } else {
                toe = (Integer.parseInt(hourStr.split(":")[0]) * hour)
                        + (Integer.parseInt(hourStr.split(":")[1]) * 1000 * 60);
            }

            if(tod < hour * 9 || tod > hour * 17 || toe < hour * 9 || toe % day > hour * 17) {
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newError("En dehors des heures d'ouverture", "Vous essayez de planifier un" +
                            " rendez-vous en dehors de heures d'ouverture de 9h00 a 17h00. Veuillez reessayer.");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newError("", "Esta tratando de programar una cita fuera del horario comercial " +
                            "de 9:00 a 17:00. Por favor intente eso de nuevo.");
                } else {
                    Main.newError("Outside of Business Hours", "You are trying to schedule an " +
                            "appointment outside of business hours of 9am to 5pm. Please try that again.");
                }
                return;
            }

            Appointment app = null;
            for(Appointment a : ERDController.getInstance().getAppointments()) {
                if(start.getTime() <= a.getStartDate().getTime() && end.getTime() >= a.getStartDate().getTime()) {
                    app = a;
                    break;
                }
            }
            if(app != null) {
                if(Main.langIdent == LanguageIdentifier.FRENCH)
                    Main.newError("Rendez-vous qui se chevauchent", "Vous essayez de planifier des " +
                            "rendez-vous qui se chevauchent à" + app.getStartDate().toString() + ".");
                else if(Main.langIdent == LanguageIdentifier.SPANISH)
                    Main.newError("Citas superpuestas", "Está intentando programar citas superpuestas" +
                            " en " + app.getStartDate().toString() + ".");
                else //English
                    Main.newError("Overlapping Appointments", "You are trying to schedule overlapping" +
                            " appointments at " + app.getStartDate().toString() + ".");
                return;
            }

            Timestamp last = new Timestamp(System.currentTimeMillis());
            if(AppointmentsViewController.selectedAppointment != null) {
                ERDController.getInstance().updateAppointment(AppointmentsViewController.selectedAppointment, cusId,
                        usrId, title, desc, loc, type, start, end, last, createdBy);
            } else {
                Appointment appointment = ERDController.getInstance().newAppointment(cusId, usrId, title, desc, loc,
                        cont, type, url, start, end, create, createdBy, last, createdBy);
                AppointmentsViewController.selectedAppointment = appointment;
            }
        } else {
            //error
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Erreur de Soumission", "Aucun client sélectionné.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Error de Envío", "Ningún cliente seleccionado.");
            } else {
                Main.newError("Submission Error", "No customer selected.");
            }
            return;
        }
        Main.dismissRecentStage();
    }

    @FXML
    private void selectCustomerWindow(ActionEvent event) {
        Main.newChildStage("CustomerSelectionView.fxml","Customer Selection");
        if(CustomerSelectionView.customer != null && CustomerSelectionView.customer.getCustomerName() != null)
            selectCustomerButton.setText(CustomerSelectionView.customer.getCustomerName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            typeLabel.setText("Catégorie:");
            customerLabel.setText("Client:");
            titleLabel.setText("Titre:");
            descriptionLabel.setText("La Description:");
            startTimeLabel.setText("Heure de début:");
            endTimeLabel.setText("Heure de fin:");

            selectCustomerButton.setText("Sélectionner un Client");
            selectStartTime.setText("Sélectionner l'heure de début");
            selectEndTime.setText("Sélectionner l'heure de fin");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            typeLabel.setText("Tipos:");
            customerLabel.setText("Cliente:");
            titleLabel.setText("Título:");
            descriptionLabel.setText("Descripción:");
            startTimeLabel.setText("Hora de inicio:");
            endTimeLabel.setText("Hora de finalización:");

            selectCustomerButton.setText("Seleccione Cliente");
            selectStartTime.setText("Seleccione hora de inicio");
            selectEndTime.setText("Seleccione hora de finalización");
        }
        if(AppointmentsViewController.selectedAppointment != null) {
            Appointment appo = ERDController.getInstance().getAppointment(AppointmentsViewController.selectedAppointment.getAppointmentId());
            typeField.setText(appo.getType());
            titleField.setText(appo.getTitle());
            descriptionField.setText(appo.getDescription());
            selectCustomerButton.setText(ERDController.getInstance().getCustomer(appo.getCustomerId()).getCustomerName());
            selectStartTime.setText(appo.getStartDate().toString());
            selectEndTime.setText(appo.getEndDate().toString());
            Appointment sa = AppointmentsViewController.selectedAppointment;
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(sa.getStartDate().getTime()));
            datePicker.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));
            CustomerSelectionView.customer = ERDController.getInstance().getCustomer(appo.getCustomerId());
        }

        //Business Hours were not specified, assuming 6am to 10pm since that should cover the majority of the range.
        //  Best-case Exception Control: Don't even let the user enter an invalid time outside of business hours for
        //  that location.
        int hours = 6;
        for(int i = 0; i < 32; i++) {
            if(i % 2 == 0 && i > 0)
                hours++;
            int minutes = (30 * i) % 60;
            String temp = hours + ":" + (minutes == 0 ? "00" : minutes);
            MenuItem mi = new MenuItem();
            mi.setText(temp);
            mi.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    String button = mi.getText();
                    selectStartTime.setText(button);
                    int hours = Integer.parseInt(button.split(":")[0]);
                    int minutes = Integer.parseInt(button.split(":")[1]);
                    ArrayList<MenuItem> endTimeMenuOptions = new ArrayList<MenuItem>();
                    for(int j = ((hours - 7)*2) + (minutes == 30 ? 0 : 1); j < 32; j++) {
                        //Add some times to the endTimeMenuButton
                        if(j % 2 == 0) {
                            hours += 1;
                        }
                        minutes = (30 * j) % 60;
                        MenuItem mis = new MenuItem();
                        mis.setText(hours + ":" + (minutes == 0 ? "00" : minutes));
                        mis.setOnAction(new EventHandler() {
                            @Override
                            public void handle(Event event) {
                                selectEndTime.setText(mis.getText());
                            }
                        });
                        endTimeMenuOptions.add(mis);
                    }
                    selectEndTime.getItems().setAll(endTimeMenuOptions);
                }
            });
            timeMenuButtonOptions.add(mi);
        }
        selectStartTime.getItems().setAll(timeMenuButtonOptions);
    }
}
