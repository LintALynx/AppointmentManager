package com.github.zeuschops.AppointmentManager.Window;

import com.github.zeuschops.AppointmentManager.Backend.ERDController;
import com.github.zeuschops.AppointmentManager.Backend.FormattedCustomer;
import com.github.zeuschops.AppointmentManager.Backend.LanguageIdentifier;
import com.github.zeuschops.AppointmentManager.ERD.Address;
import com.github.zeuschops.AppointmentManager.ERD.Customer;
import com.github.zeuschops.AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerSelectionView implements Initializable {
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private TableView tableView;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;

    public static Customer customer = null;

    @FXML
    private void okButtonAction(ActionEvent event) {
        if(tableView.getSelectionModel().getSelectedItem() != null)
            customer = ERDController.getInstance().getCustomers().get(tableView.getSelectionModel().getSelectedIndex());
        Main.dismissRecentStage();
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        Main.dismissRecentStage();
    }

    @FXML private void addCustomer(ActionEvent event) {
        customer = null;
        ArrayList<Customer> cs = ERDController.getInstance().getCustomers(); //This might duplicate ths list, I'm not sure
        if(Main.langIdent == LanguageIdentifier.FRENCH) {
            Main.newChildStage("CustomerSpecificsView.fxml","Ajouter des données Client");
        } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
            Main.newChildStage("CustomerSpecificsView.fxml", "Agregar datos del Cliente");
        } else {
            Main.newChildStage("CustomerSpecificsView.fxml", "Add Customer Data");
        }
        if(customer != null) {
            Address addr = ERDController.getInstance().getAddress(customer.getAddressId());
            FormattedCustomer fc = new FormattedCustomer(customer.getCustomerName(), addr.getAddress(),
                    addr.getPhoneNumber(), customer.getCustomerId(), addr.getAddressId());
            tableView.getItems().add(fc);
        }
        tableView.requestFocus();
        tableView.refresh();
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete this customer?");
        alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> { //Lambda #2, does something other than what Main.newError does..
            Customer customer = ERDController.getInstance().getCustomers().get(tableView.getSelectionModel().getSelectedIndex());
            ERDController.getInstance().deleteCustomer(customer.getCustomerId());
            ObservableList<FormattedCustomer> fcs = tableView.getItems();
            for(FormattedCustomer fc : fcs) {
                if(fc.getCustomerId() == customer.getCustomerId()) {
                    fcs.remove(fc);
                    break;
                }
            }
        });
        tableView.requestFocus();
        tableView.refresh();
    }

    @FXML
    private void modifyCustomer(ActionEvent event) {
        if(tableView.getSelectionModel().getSelectedIndex() > -1) {
            FormattedCustomer fc = formattedCustomers.get(tableView.getSelectionModel().getSelectedIndex());
            customer = ERDController.getInstance().getCustomer(fc.customerId);
            if (Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newChildStage("CustomerSpecificsView.fxml", "Modifier les données client");
            } else if (Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newChildStage("CustomerSpecificsView.fxml", "Modificar datos del Cliente");
            } else {
                Main.newChildStage("CustomerSpecificsView.fxml", "Modify Customer Data");
            }
        } else {
            if(Main.langIdent == LanguageIdentifier.FRENCH) {
                Main.newError("Aucun client sélectionné", "Aucun client sélectionné pour modifier. Veuillez en sélectionner un et réessayer.");
            } else if(Main.langIdent == LanguageIdentifier.SPANISH) {
                Main.newError("Ningún cliente seleccionado", "Ningún cliente seleccionado para modificar. Seleccione uno e intente nuevamente.");
            } else {
                Main.newError("No Customer Selected", "No customer selected to modify. Please select one and try again.");
            }
        }
        tableView.requestFocus();
        tableView.refresh();
    }

    private ArrayList<FormattedCustomer> formattedCustomers = new ArrayList<FormattedCustomer>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Customer> customers = ERDController.getInstance().getCustomers();
        for(Customer c : customers) {
            if(c.getAddressId() != -1) {
                Address addr = ERDController.getInstance().getAddress(c.getAddressId());
                formattedCustomers.add(new FormattedCustomer(c.getCustomerName(), addr.getAddress(), addr.getPhoneNumber(), c.getCustomerId(), c.getAddressId()));
            }
        }
        tableView.setItems(FXCollections.observableList(formattedCustomers));
        ((TableColumn)(tableView.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<FormattedCustomer, String>("customerName")); //name
        ((TableColumn)(tableView.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<FormattedCustomer, String>("address")); //location
        ((TableColumn)(tableView.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<FormattedCustomer, String>("phoneNumber")); //contact

        if(Main.langIdent == LanguageIdentifier.SPANISH) {
            addCustomerButton.setText("Agregar Cliente");
            modifyCustomerButton.setText("Modificar Cliente");
            deleteCustomerButton.setText("Borrar");
        } else if(Main.langIdent == LanguageIdentifier.FRENCH) {
            addCustomerButton.setText("Ajouter un Client");
            modifyCustomerButton.setText("Modifier le Client");
            deleteCustomerButton.setText("Supprimer");
        } else {
            addCustomerButton.setText("Add Customer");
            modifyCustomerButton.setText("Modify Customer");
            deleteCustomerButton.setText("Delete");
        }
    }
}
