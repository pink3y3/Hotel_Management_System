package com.hotel.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * DashboardController — root controller for the main TabPane.
 *
 * Injects sub-controllers declared via fx:include in Dashboard.fxml
 * and triggers a data-refresh whenever a tab is selected so each
 * tab always shows the latest data.
 */
public class DashboardController implements Initializable {

    @FXML private TabPane mainTabPane;

    /* Sub-controllers injected by FXML fx:include */
    @FXML private RoomController     roomTabController;
    @FXML private CustomerController customerTabController;
    @FXML private BookingController  bookingTabController;
    @FXML private PackageController  packageTabController;
    @FXML private BillingController  billingTabController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Refresh relevant combos/tables when the user switches tabs
        mainTabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            switch (newIdx.intValue()) {
                case 2 -> bookingTabController.refreshCombos();   // Booking tab
                case 3 -> packageTabController.refreshCombos();   // Packages tab
                case 4 -> billingTabController.refreshCombos();   // Billing tab
            }
        });
    }
}
