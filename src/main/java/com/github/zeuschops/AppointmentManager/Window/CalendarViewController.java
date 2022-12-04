package com.github.zeuschops.AppointmentManager.Window;

import com.github.gaijinkindred.AppointmentManager.Backend.*;
import com.github.zeuschops.AppointmentManager.Backend.*;
import com.github.zeuschops.AppointmentManager.ERD.Appointment;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class CalendarViewController implements Initializable {
    @FXML private Label cellZeroOne;
    @FXML private Label cellZeroTwo;
    @FXML private Label cellZeroThree;
    @FXML private Label cellZeroFour;
    @FXML private Label cellZeroFive;
    @FXML private Label cellZeroSix;
    @FXML private Label cellOneOne;
    @FXML private Label cellOneTwo;
    @FXML private Label cellOneThree;
    @FXML private Label cellOneFour;
    @FXML private Label cellOneFive;
    @FXML private Label cellOneSix;
    @FXML private Label cellTwoOne;
    @FXML private Label cellTwoTwo;
    @FXML private Label cellTwoThree;
    @FXML private Label cellTwoFour;
    @FXML private Label cellTwoFive;
    @FXML private Label cellTwoSix;
    @FXML private Label cellThreeOne;
    @FXML private Label cellThreeTwo;
    @FXML private Label cellThreeThree;
    @FXML private Label cellThreeFour;
    @FXML private Label cellThreeFive;
    @FXML private Label cellThreeSix;
    @FXML private Label cellFourOne;
    @FXML private Label cellFourTwo;
    @FXML private Label cellFourThree;
    @FXML private Label cellFourFour;
    @FXML private Label cellFourFive;
    @FXML private Label cellFourSix;
    @FXML private Label cellFiveOne;
    @FXML private Label cellFiveTwo;
    @FXML private Label cellFiveThree;
    @FXML private Label cellFiveFour;
    @FXML private Label cellFiveFive;
    @FXML private Label cellFiveSix;
    @FXML private Label cellSixOne;
    @FXML private Label cellSixTwo;
    @FXML private Label cellSixThree;
    @FXML private Label cellSixFour;
    @FXML private Label cellSixFive;
    @FXML private Label cellSixSix;
    @FXML private Label monthLabel;
    @FXML private MenuButton monthMenuButton;
    @FXML private TableView tableView;

    public static ArrayList<TableTimeData> ttds = new ArrayList<TableTimeData>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Opened CalendarViewController.");

        //Set Month Label:
        monthLabel.setText(getMonthTitle(Calendar.getInstance().get(Calendar.MONTH)));

        //Set Calendar dates:
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        //should be millis from 1970 to the first day of the month;
        long firstMillis = cal.getTime().getTime() - ((cal.get(Calendar.DATE) - 1) * 24 * 60 * 60 * 1000);
        cal.setTime(new Date(firstMillis));
        weekFunnel(cal.get(Calendar.DAY_OF_WEEK), month, year);
        cal.setTime(new Date(System.currentTimeMillis()));

        //Set menuButton stuff:
        MenuItem miM = new MenuItem();
        MenuItem miW = new MenuItem();
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            monthMenuButton.setText("Mois");
            miM.setText("Mois");
            miW.setText("La Semaine");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            monthMenuButton.setText("Mes");
            miM.setText("Mes");
            miW.setText("Semana");
        } else {
            miM.setText("Month");
            miW.setText("Week");
        }
        miW.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                monthMenuButton.setText("Week");

                Calendar cal = Calendar.getInstance();
                long offset = ((cal.get(Calendar.DAY_OF_WEEK) - 1) * 24 * 3600000) +
                        (cal.get(Calendar.HOUR_OF_DAY) * 3600000) + (cal.get(Calendar.MINUTE) * 60000) +
                        (cal.get(Calendar.SECOND) * 1000);
                long sundayTime = Calendar.getInstance().getTime().getTime() - offset - 1000;
                long saturdayEndTime = sundayTime + 604801000; //Should be about right
                int hour = 6;
                int minute = -1;

                String[] str = { "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday" };
                ArrayList<Appointment> backlog = new ArrayList<Appointment>();
                long currentTimeCheck = 0;
                cal.setTime(new Date(System.currentTimeMillis()));
                int toDayOfWeek = cal.get(Calendar.DAY_OF_WEEK); //Haha, puns.
                long cts = System.currentTimeMillis();
                long dayLength = 86400000;
                Timestamp beforeTS = new Timestamp(cts - (cts % dayLength) - (dayLength * (toDayOfWeek - 1)));
                Timestamp afterTS = new Timestamp(beforeTS.getTime() + (dayLength * 7));
                for(int i = 0; i < 32; i++) {
                    hour += i % 2 == 0 ? 1 : 0;
                    minute = (i * 30) % 60;
                    TableTimeData ttd = new TableTimeData(hour + ":" + (minute == 0 ? "00" : minute),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "");
                    ttds.add(ttd);
                    for (Appointment a : ERDController.getInstance().getAppointments()) {
                        if(!a.getStartDate().before(beforeTS) && !a.getStartDate().after(afterTS)) {
                            currentTimeCheck = (long) ((hour * 3600 * 1000) + (minute * 60 * 1000));
                            if (a.getStartDate().getTime() % 86400000 == currentTimeCheck || (backlog.indexOf(a) > -1 &&
                                            a.getEndDate().getTime() % 86400000 > currentTimeCheck)) {
                                Calendar.getInstance().setTime(a.getStartDate());
                                if (getDayOfWeek(a.getStartDate()).equals("sunday")) {
                                    ttd.setSunday(a.getTitle());
                                } else if (getDayOfWeek(a.getStartDate()).equals("monday")) {
                                    ttd.setMonday(a.getTitle());
                                } else if (getDayOfWeek(a.getStartDate()).equals("tuesday")) {
                                    ttd.setTuesday(a.getTitle());
                                } else if (getDayOfWeek(a.getStartDate()).equals("wednesday")) {
                                    ttd.setWednesday(a.getTitle());
                                } else if (getDayOfWeek(a.getStartDate()).equals("thursday")) {
                                    ttd.setThursday(a.getTitle());
                                } else if (getDayOfWeek(a.getStartDate()).equals("friday")) {
                                    ttd.setFriday(a.getTitle());
                                } else if (getDayOfWeek(a.getStartDate()).equals("saturday")) {
                                    ttd.setSaturday(a.getTitle());
                                }
                                if (a.getEndDate().getTime() - a.getStartDate().getTime() > 1) {
                                    backlog.add(a);
                                }
                            } else if (backlog.indexOf(a) > -1 &&
                                    a.getEndDate().getTime() % 86400000 < currentTimeCheck) {
                                backlog.remove(a);
                            }
                        }
                    }
                }

                //Main.dismissRecentStage();
                if(Main.langIdent == LanguageIdentifier.FRENCH) {
                    Main.newChildStage("WeeklyCalendarView.fxml",
                            "Calendrier - Vue Hebdomadaire");
                } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                    Main.newChildStage("WeeklyCalendarView.fxml",
                            "Calendario - Vista Semanal");
                } else {
                    Main.newChildStage("WeeklyCalendarView.fxml",
                            "Calendar - Weekly View");
                }
            }
        });
        monthMenuButton.getItems().remove(0); //Remove Action 1
        monthMenuButton.getItems().remove(0); //Remove Action 2
        monthMenuButton.getItems().addAll(miW, miM);

        //TableView, unsorted (because I just don't want to tbh)
        long compareAgainst = Calendar.getInstance().getTime().getTime();
        long firstOfMonth = compareAgainst - (cal.get(Calendar.DAY_OF_MONTH) * 1000 * 60 * 60 * 24) -
                (compareAgainst % (1000 * 60 * 60 * 24));
        long dayLength = 1000 * 60 * 60 * 24;
        long timeInMonth = ((month % 2 == 0 ? 30 :
                (month % 2 == 1 && month != 1 ? 31 : (year % 4 == 0 ? 29 : 28))) * dayLength);
        ArrayList<FormattedCalendarAppointment> appointments = new ArrayList<FormattedCalendarAppointment>();
        for(Appointment a : ERDController.getInstance().getAppointments()) {
            if(a.getStartDate().getTime() > firstOfMonth && a.getStartDate().getTime() < firstOfMonth + timeInMonth)
                appointments.add(new FormattedCalendarAppointment(a.getTitle(), a.getStartDate().toString()));
        }
        tableView.setItems(FXCollections.observableList(appointments));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<FormattedCalendarAppointment, String>("title"));
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<FormattedCalendarAppointment, String>("date"));

        //Done.
        Main.LOGGERINSTANCE.log(Logger.LoggingLevel.LOG, "Loaded CalendarViewController.");
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

    private void weekFunnel(int startDay, int month, int year) {
        if(startDay == Calendar.SUNDAY) {
            weekOne("1", "2", "3", "4",
                    "5", "6", "7");
            weekTwo("8", "9", "10", "11",
                    "12", "13", "14");
            weekThree("15", "16", "17", "18",
                    "19", "20", "21");
            weekFour("22", "23", "24", "25",
                    "26", "27", "28");
            if(month % 2 == 1) {
                //31 Days
                weekFive("29", "30","31","",
                        "","","");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("29", "30","","",
                        "","","");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days, and 28 days is already accounted for here
                    weekFive("29", "","","",
                            "","","");
                }
            }
            weekSix("","","","","","","");
        } else if(startDay == Calendar.MONDAY) {
            weekOne("", "1", "2", "3",
                    "4", "5", "6");
            weekTwo("7", "8", "9", "10",
                    "11", "12", "13");
            weekThree("14", "15", "16", "17",
                    "18", "19", "20");
            weekFour("21", "22", "23", "24",
                    "25", "26", "27");
            if(month % 2 == 1) {
                //31 Days
                weekFive("28", "29", "30", "31",
                        "", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("28", "29", "30", "",
                        "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("28", "29", "", "",
                            "", "", "");
                } else {
                    //28 Days
                    weekFive("28", "", "", "",
                            "", "", "");
                }
            }
            weekSix("","","","",
                    "","","");
        } else if(startDay == Calendar.TUESDAY) {
            weekOne("", "", "1", "2",
                    "3", "4", "5");
            weekTwo("6", "7", "8", "9",
                    "10", "11", "12");
            weekThree("13", "14", "15", "16",
                    "17", "18", "19");
            weekFour("20", "21", "22", "23",
                    "24", "25", "26");
            if(month % 2 == 1) {
                //31 Days
                weekFive("27", "28", "29", "30",
                        "31", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("27", "28", "29", "30",
                        "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("27", "28", "29", "",
                            "", "", "");
                } else {
                    //28 Days
                    weekFive("27", "28", "", "",
                            "", "", "");
                }
            }
            weekSix("", "", "", "",
                    "", "", "");
        } else if(startDay == Calendar.WEDNESDAY) {
            weekOne("", "", "", "1",
                    "2", "3", "4");
            weekTwo("5", "6", "7", "8",
                    "9", "10", "11");
            weekThree("12", "13", "14", "15",
                    "16", "17", "18");
            weekFour("19", "20", "21", "22",
                    "23", "24", "25");
            if(month % 2 == 1) {
                //31 Days
                weekFive("26", "27", "28", "29",
                        "30", "31", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("26", "27", "28", "29",
                        "30", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("26", "27", "28", "29",
                            "", "", "");
                } else {
                    //28 Days
                    weekFive("26", "27", "28", "",
                            "", "", "");
                }
            }
            weekSix("", "", "", "",
                    "", "", "");
        } else if(startDay == Calendar.THURSDAY) {
            weekOne("", "", "", "",
                    "1", "2", "3");
            weekTwo("4", "5", "6", "7",
                    "8", "9", "10");
            weekThree("11", "12", "13", "14",
                    "15", "16", "17");
            weekFour("18", "19", "20", "21",
                    "22", "23", "24");
            if(month % 2 == 1) {
                //31 Days
                weekFive("25", "26", "27", "28",
                        "29", "30", "31");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("25", "26", "27", "28",
                        "29", "30", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("25", "26", "27", "28",
                            "29", "", "");
                } else {
                    //28 Days
                    weekFive("25", "26", "27", "28",
                            "", "", "");
                }
            }
            weekSix("", "" ,"", "",
                    "", "", "");
        } else if(startDay == Calendar.FRIDAY) {
            weekOne("", "", "", "",
                    "", "1", "2");
            weekTwo("3", "4", "5", "6",
                    "7", "8", "9");
            weekThree("10", "11", "12", "13",
                    "14", "15", "16");
            weekFour("17", "18", "19", "20",
                    "21", "22", "23");
            if(month % 2 == 1) {
                //31 Days
                weekFive("24", "25", "26", "27",
                        "28", "29", "30");
                weekSix("31", "", "", "",
                        "", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("24", "25", "26", "27",
                        "28", "29", "30");
                weekSix("", "", "", "",
                        "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("24", "25", "26", "27",
                            "28", "29", "");
                } else {
                    //28 Days
                    weekFive("24", "25", "26", "27",
                            "28", "", "");
                }
                weekSix("", "", "", "",
                        "", "", "");
            }
        } else {
            weekOne("", "", "", "",
                    "", "", "1");
            weekTwo("2", "3", "4", "5",
                    "6", "7", "8");
            weekThree("9", "10", "11", "12",
                    "13", "14", "15");
            weekFour("16", "17", "18", "19",
                    "20", "21", "22");
            if(month % 2 == 1) {
                //31 Days
                weekFive("23", "24", "25", "26",
                        "27", "28", "29");
                weekSix("30", "31", "", "",
                        "", "", "");
            } else if(month % 2 == 0 && month != Calendar.FEBRUARY) {
                //30 Days
                weekFive("23", "24", "25", "26",
                        "27", "28", "29");
                weekSix("30", "", "", "",
                        "", "", "");
            } else {
                //February
                if(year % 4 == 0) {
                    //29 Days
                    weekFive("23", "24", "25", "26",
                            "27", "28", "29");
                } else {
                    //28 Days
                    weekFive("23", "24", "25", "26",
                            "27", "28", "");
                }
                weekSix("", "", "", "",
                        "", "", "");
            }
        }
    }

    private void weekOne(String dayOne, String dayTwo, String dayThree, String dayFour,
                         String dayFive, String daySix, String daySeven) {
        cellZeroOne.setText(dayOne);
        cellOneOne.setText(dayTwo);
        cellTwoOne.setText(dayThree);
        cellThreeOne.setText(dayFour);
        cellFourOne.setText(dayFive);
        cellFiveOne.setText(daySix);
        cellSixOne.setText(daySeven);
    }

    private void weekTwo(String dayOne, String dayTwo, String dayThree, String dayFour,
                         String dayFive, String daySix, String daySeven) {
        cellZeroTwo.setText(dayOne);
        cellOneTwo.setText(dayTwo);
        cellTwoTwo.setText(dayThree);
        cellThreeTwo.setText(dayFour);
        cellFourTwo.setText(dayFive);
        cellFiveTwo.setText(daySix);
        cellSixTwo.setText(daySeven);
    }

    private void weekThree(String dayOne, String dayTwo, String dayThree, String dayFour,
                           String dayFive, String daySix, String daySeven) {
        cellZeroThree.setText(dayOne);
        cellOneThree.setText(dayTwo);
        cellTwoThree.setText(dayThree);
        cellThreeThree.setText(dayFour);
        cellFourThree.setText(dayFive);
        cellFiveThree.setText(daySix);
        cellSixThree.setText(daySeven);
    }

    private void weekFour(String dayOne, String dayTwo, String dayThree, String dayFour,
                          String dayFive, String daySix, String daySeven) {
        cellZeroFour.setText(dayOne);
        cellOneFour.setText(dayTwo);
        cellTwoFour.setText(dayThree);
        cellThreeFour.setText(dayFour);
        cellFourFour.setText(dayFive);
        cellFiveFour.setText(daySix);
        cellSixFour.setText(daySeven);
    }

    private void weekFive(String dayOne, String dayTwo, String dayThree, String dayFour,
                          String dayFive, String daySix, String daySeven) {
        cellZeroFive.setText(dayOne);
        cellOneFive.setText(dayTwo);
        cellTwoFive.setText(dayThree);
        cellThreeFive.setText(dayFour);
        cellFourFive.setText(dayFive);
        cellFiveFive.setText(daySix);
        cellSixFive.setText(daySeven);
    }

    private void weekSix(String dayOne, String dayTwo, String dayThree, String dayFour,
                         String dayFive, String daySix, String daySeven) {
        cellZeroSix.setText(dayOne);
        cellOneSix.setText(dayTwo);
        cellTwoSix.setText(dayThree);
        cellThreeSix.setText(dayFour);
        cellFourSix.setText(dayFive);
        cellFiveSix.setText(daySix);
        cellSixSix.setText(daySeven);
    }

    private String getMonthTitle(int month) {
        switch(month) {
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "January";
        }
    }
}
