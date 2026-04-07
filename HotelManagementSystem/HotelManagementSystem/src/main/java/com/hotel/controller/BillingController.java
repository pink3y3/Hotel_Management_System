package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.util.AlertUtil;
import com.hotel.util.EmailService;
import com.hotel.util.HotelDataManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * BillingController — Billing tab.
 *
 * Features:
 *  - Load bill details for any booking
 *  - Print bill to console
 *  - Send e-bill via multithreaded EmailService
 *  - Shows birthday discount note when applicable
 */
public class BillingController implements Initializable {

    @FXML private ComboBox<Booking> billingBookingCombo;

    /* Bill detail labels */
    @FXML private Label bookingIdLabel;
    @FXML private Label customerLabel;
    @FXML private Label roomLabel;
    @FXML private Label nightsLabel;
    @FXML private Label roomChargesLabel;
    @FXML private Label packageCostLabel;
    @FXML private Label subTotalLabel;
    @FXML private Label discountLabel;
    @FXML private Label finalBillLabel;
    @FXML private Label birthdayNoteLabel;

    @FXML private TextArea billTextArea;
    @FXML private Label    emailStatusLabel;
    @FXML private Button   sendEmailButton;

    private final HotelDataManager dm = HotelDataManager.getInstance();
    private Booking selectedBooking;

    // ── Lifecycle ────────────────────────────────────────────────────────────

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshCombos();
        billingBookingCombo.setOnAction(e -> loadBill());
    }

    // ── Bill loading ─────────────────────────────────────────────────────────

    private void loadBill() {
        selectedBooking = billingBookingCombo.getValue();
        if (selectedBooking == null) {
            clearBill();
            return;
        }

        bookingIdLabel.setText(selectedBooking.getBookingId());
        customerLabel.setText(selectedBooking.getCustomerName());

        String roomInfo = selectedBooking.getRoomNumber();
        if (selectedBooking.getRoom() != null) {
            roomInfo += "  (" + selectedBooking.getRoom().getRoomType() + ")";
        }
        roomLabel.setText(roomInfo);
        nightsLabel.setText(selectedBooking.getNumberOfNights() + " night(s)");
        roomChargesLabel.setText(String.format("$%.2f", selectedBooking.getRoomCharges()));
        packageCostLabel.setText(String.format("$%.2f  (%s)",
                selectedBooking.getPackageCost(), selectedBooking.getPackageName()));
        subTotalLabel.setText(String.format("$%.2f", selectedBooking.getSubTotal()));

        if (selectedBooking.getDiscountPercent() > 0) {
            discountLabel.setText(String.format("-$%.2f  (%.0f%% birthday discount)",
                    selectedBooking.getDiscountAmount(), selectedBooking.getDiscountPercent()));
            discountLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            birthdayNoteLabel.setText("🎂 Birthday discount applied on check-in date!");
            birthdayNoteLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
        } else {
            discountLabel.setText("$0.00");
            discountLabel.setStyle("-fx-text-fill: #2c3e50;");
            birthdayNoteLabel.setText("");
        }

        finalBillLabel.setText(String.format("$%.2f", selectedBooking.getFinalBill()));
        billTextArea.setText(dm.generateBillText(selectedBooking));

        emailStatusLabel.setText("");
        sendEmailButton.setDisable(false);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    @FXML
    private void handlePrintBill() {
        if (selectedBooking == null) {
            AlertUtil.showWarning("No Booking", "Please select a booking first.");
            return;
        }
        String bill = dm.generateBillText(selectedBooking);
        System.out.println("\n========== PRINT OUTPUT ==========");
        System.out.println(bill);
        System.out.println("===================================\n");
        AlertUtil.showInfo("Bill Printed", "Bill for " + selectedBooking.getBookingId()
                + " has been printed to the console.");
    }

    /**
     * Sends e-bill using EmailService (multithreaded).
     * Button is disabled while the background thread is running.
     */
    @FXML
    private void handleSendEmail() {
        if (selectedBooking == null) {
            AlertUtil.showWarning("No Booking", "Please select a booking first.");
            return;
        }

        sendEmailButton.setDisable(true);
        emailStatusLabel.setStyle("-fx-text-fill: #2980b9;");
        emailStatusLabel.setText("⏳ Sending e-bill, please wait...");

        EmailService.sendEBill(
                selectedBooking,
                successMsg -> {
                    emailStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    emailStatusLabel.setText("✅ " + successMsg);
                    sendEmailButton.setDisable(false);
                    AlertUtil.showInfo("E-Bill Sent", successMsg);
                },
                errorMsg -> {
                    emailStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    emailStatusLabel.setText("❌ " + errorMsg);
                    sendEmailButton.setDisable(false);
                    AlertUtil.showError("Email Failed", errorMsg);
                }
        );
    }

    @FXML
    private void handleRefresh() {
        refreshCombos();
        clearBill();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    public void refreshCombos() {
        billingBookingCombo.setItems(
                FXCollections.observableArrayList(dm.getAllBookings()));
    }

    private void clearBill() {
        bookingIdLabel.setText("—");
        customerLabel.setText("—");
        roomLabel.setText("—");
        nightsLabel.setText("—");
        roomChargesLabel.setText("—");
        packageCostLabel.setText("—");
        subTotalLabel.setText("—");
        discountLabel.setText("—");
        finalBillLabel.setText("—");
        birthdayNoteLabel.setText("");
        billTextArea.clear();
        emailStatusLabel.setText("");
        sendEmailButton.setDisable(false);
        selectedBooking = null;
    }
}
