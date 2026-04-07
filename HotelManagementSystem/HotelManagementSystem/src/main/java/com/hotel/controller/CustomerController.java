package com.hotel.controller;

import com.hotel.model.Customer;
import com.hotel.util.AlertUtil;
import com.hotel.util.HotelDataManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.util.List;
/**
 * CustomerController — Customers tab.
 * Handles: Add customer, view list, search by name.
 */
public class CustomerController implements Initializable {

    @FXML private TextField customerNameField;
    @FXML private TextField contactNumberField;
    @FXML private DatePicker dobPicker;

    @FXML private TableView<Customer>           customerTable;
    @FXML private TableColumn<Customer, String> colCustomerId;
    @FXML private TableColumn<Customer, String> colCustomerName;
    @FXML private TableColumn<Customer, String> colContact;
    @FXML private TableColumn<Customer, String> colDob;

    @FXML private TextField searchCustomerField;

    private final HotelDataManager dm = HotelDataManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dobDisplay"));

        // Disable future dates in the DOB calendar — a birthday cannot be tomorrow!
        dobPicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(java.time.LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #bbb;");
                }
            }
        });

        refreshTable();
    }

    @FXML
    private void handleAddCustomer() {
        String name    = customerNameField.getText().trim();
        String contact = contactNumberField.getText().trim();
        LocalDate dob  = dobPicker.getValue();

        if (name.isEmpty() || contact.isEmpty() || dob == null) {
            AlertUtil.showWarning("Validation", "Please fill in all customer fields.");
            return;
        }

        if (!contact.matches("\\d{7,15}")) {
            AlertUtil.showError("Validation", "Contact number must be 7–15 digits.");
            return;
        }

        if (dob.isAfter(LocalDate.now())) {
            AlertUtil.showError("Validation", "Date of birth cannot be in the future.");
            return;
        }

        String id = dm.generateCustomerId();
        Customer customer = new Customer(id, name, contact, dob);
        dm.addCustomer(customer);
        refreshTable();
        clearCustomerFields();
        AlertUtil.showInfo("Success", "Customer '" + name + "' added with ID: " + id);
    }

    @FXML
    private void handleSearch() {
        String query = searchCustomerField.getText().trim();
        List<Customer> results = dm.searchCustomers(query);
        customerTable.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleClearSearch() {
        searchCustomerField.clear();
        refreshTable();
    }

    public void refreshTable() {
        customerTable.setItems(
                FXCollections.observableArrayList(dm.getAllCustomers()));
    }

    private void clearCustomerFields() {
        customerNameField.clear();
        contactNumberField.clear();
        dobPicker.setValue(null);
    }
}
