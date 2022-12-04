package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.gaijinkindred.AppointmentManager.ERD.*;
import com.github.zeuschops.AppointmentManager.ERD.Address;
import com.github.zeuschops.AppointmentManager.ERD.City;
import com.github.zeuschops.AppointmentManager.ERD.Country;
import com.github.zeuschops.AppointmentManager.ERD.User;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.ResourceBundle;

public class CustomerSpecificsViewController implements Initializable {
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField countryField;
    @FXML private TextField postalField;
    @FXML private TextField phoneNumberField;

    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label cityLabel;
    @FXML private Label countryLabel;
    @FXML private Label postalLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Button submitButton;

    @FXML private TableView tableView;

    @FXML
    private void submitButtonAction(ActionEvent ae) {
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        Timestamp ts = new Timestamp(date.getTime());
        String addrStr = addressField.getText();
        String city = cityField.getText();
        String ctry = countryField.getText();
        String post = postalField.getText();
        String numb = phoneNumberField.getText();
        User usr = Main.user;

        if(addrStr == null || addrStr.length() < 1 || city.length() < 1
                || ctry.length() < 1 || post.length() < 1 || numb.length() < 1) {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Ne peut être vide", "Les détails du client ne peuvent pas être vides");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("No puede estar en blanco", "Las especificaciones del cliente no pueden" +
                        " en blanco.");
            } else {
                Main.newError("Cannot be blank", "Customer specifics cannot be blank.");
            }
            return;
        }

        if(CustomerSelectionView.customer == null) {
            Address addr = ERDController.getInstance().newAddress(addrStr, "", city, ctry, post, numb, date, usr.userName, ts, usr.userName);
            CustomerSelectionView.customer = ERDController.getInstance().newCustomer(nameField.getText(), addr.getAddressId(), 1, date, usr.userName, ts, usr.userName);
        } else {
            Address addr = null;
            for(Address a : ERDController.getInstance().getAddresses()) {
                if(a.getAddressId() == CustomerSelectionView.customer.getAddressId()) {
                    addr = a;
                    break;
                }
            }
            long time = Calendar.getInstance().getTime().getTime();
            if(addr == null) {
                addr = ERDController.getInstance().newAddress(addrStr, "", city, ctry, post, numb,
                        new Date(time), usr.userName, new Timestamp(time), usr.userName);
            }
            City c = ERDController.getInstance().getCity(addr.getCityId());
            if(c.getCity().equalsIgnoreCase(city))
                ERDController.getInstance().updateAddress(addr, addrStr, addr.getCityId(), numb, new Timestamp(time), usr.userName);
            ERDController.getInstance().updateCustomer(CustomerSelectionView.customer, nameField.getText(), addr.getAddressId(), 1, new Timestamp(time), usr.userName);
        }
        Main.dismissRecentStage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            nameLabel.setText("Nom:");
            addressLabel.setText("Addresse:");
            cityLabel.setText("Ville:");
            countryLabel.setText("Pays:");
            postalLabel.setText("Code Postal:");
            phoneNumberLabel.setText("Numéro de Téléphone:");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            nameLabel.setText("Nombre:");
            addressLabel.setText("Dirección:");
            cityLabel.setText("Ciudad:");
            countryLabel.setText("País:");
            postalLabel.setText("Postal:");
            phoneNumberLabel.setText("Número de Teléfono:");
        }

        if(CustomerSelectionView.customer != null) {
            nameField.setText(CustomerSelectionView.customer.getCustomerName());
            Address addr = ERDController.getInstance().getAddress(CustomerSelectionView.customer.getAddressId());
            if(addr != null)
                addressField.setText(addr.getAddress());
            City c = ERDController.getInstance().getCity(addr.getCityId());
            if(c != null) {
                cityField.setText(c.getCity());
                Country co = ERDController.getInstance().getCountry(c.getCountryId());
                if (co != null) {
                    countryField.setText(co.getCountry() + "");
                }
            }
            if(addr.getPostalCode() != null)
                postalField.setText(addr.getPostalCode());
            phoneNumberField.setText(addr.getPhoneNumber());
        }
    }
}
