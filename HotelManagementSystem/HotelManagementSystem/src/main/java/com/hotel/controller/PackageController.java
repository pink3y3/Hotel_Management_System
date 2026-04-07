package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.model.HotelPackage;
import com.hotel.model.HotelPackage.PackageType;
import com.hotel.util.AlertUtil;
import com.hotel.util.HotelDataManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * PackageController — Packages tab.
 * Displays package info and allows assigning packages to bookings.
 */
public class PackageController implements Initializable {

    // Package info cards labels
    @FXML private Label basicDescLabel;
    @FXML private Label basicCostLabel;
    @FXML private Label premiumDescLabel;
    @FXML private Label premiumCostLabel;
    @FXML private Label luxuryDescLabel;
    @FXML private Label luxuryCostLabel;

    // Assignment section
    @FXML private ComboBox<Booking>     bookingCombo;
    @FXML private ComboBox<PackageType> packageTypeCombo;
    @FXML private Label                 packageDescLabel;
    @FXML private Label                 packageCostLabel;

    // Assignment history table
    @FXML private TableView<Booking>           assignTable;
    @FXML private TableColumn<Booking, String> colAssignBookingId;
    @FXML private TableColumn<Booking, String> colAssignCustomer;
    @FXML private TableColumn<Booking, String> colAssignRoom;
    @FXML private TableColumn<Booking, String> colAssignPackage;

    private final HotelDataManager dm = HotelDataManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate package info cards
        basicDescLabel.setText(PackageType.BASIC.getDescription());
        basicCostLabel.setText(String.format("$%.2f", PackageType.BASIC.getCost()));
        premiumDescLabel.setText(PackageType.PREMIUM.getDescription());
        premiumCostLabel.setText(String.format("$%.2f", PackageType.PREMIUM.getCost()));
        luxuryDescLabel.setText(PackageType.LUXURY.getDescription());
        luxuryCostLabel.setText(String.format("$%.2f", PackageType.LUXURY.getCost()));

        // Combo boxes
        packageTypeCombo.setItems(FXCollections.observableArrayList(PackageType.values()));
        packageTypeCombo.setOnAction(e -> updatePackagePreview());

        // Table columns
        colAssignBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colAssignCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAssignRoom.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colAssignPackage.setCellValueFactory(new PropertyValueFactory<>("packageName"));

        refreshCombos();
        refreshTable();
    }

    private void updatePackagePreview() {
        PackageType type = packageTypeCombo.getValue();
        if (type == null) return;
        packageDescLabel.setText(type.getDescription());
        packageCostLabel.setText(String.format("Cost: $%.2f", type.getCost()));
    }

    @FXML
    private void handleAssignPackage() {
        Booking booking     = bookingCombo.getValue();
        PackageType pkgType = packageTypeCombo.getValue();

        if (booking == null || pkgType == null) {
            AlertUtil.showWarning("Validation", "Please select a booking and a package.");
            return;
        }

        booking.setHotelPackage(new HotelPackage(pkgType));
        dm.saveBookings();
        refreshTable();
        bookingCombo.setValue(null);
        packageTypeCombo.setValue(null);
        packageDescLabel.setText("");
        packageCostLabel.setText("");

        AlertUtil.showInfo("Package Assigned",
                pkgType.getDisplayName() + " package assigned to " + booking.getBookingId() + ".");
    }

    @FXML
    private void handleRefresh() {
        refreshCombos();
        refreshTable();
    }

    public void refreshCombos() {
        bookingCombo.setItems(FXCollections.observableArrayList(dm.getActiveBookings()));
    }

    public void refreshTable() {
        assignTable.setItems(FXCollections.observableArrayList(dm.getAllBookings()));
    }
}
