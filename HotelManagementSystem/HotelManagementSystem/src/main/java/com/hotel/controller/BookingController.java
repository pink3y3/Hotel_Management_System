package com.hotel.controller;

import com.hotel.model.*;
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

/**
 * BookingController — Booking tab.
 * Handles: Book room, prevent duplicate, booking history, unique BOOK-IDs.
 */
public class BookingController implements Initializable {

    @FXML private ComboBox<Customer> customerCombo;
    @FXML private ComboBox<Room>     roomCombo;
    @FXML private DatePicker         checkInPicker;
    @FXML private DatePicker         checkOutPicker;
    @FXML private Label              birthdayLabel;

    @FXML private TableView<Booking>            bookingTable;
    @FXML private TableColumn<Booking, String>  colBookingId;
    @FXML private TableColumn<Booking, String>  colCustomerName;
    @FXML private TableColumn<Booking, String>  colRoomNumber;
    @FXML private TableColumn<Booking, String>  colCheckIn;
    @FXML private TableColumn<Booking, String>  colCheckOut;
    @FXML private TableColumn<Booking, String>  colPackageName;
    @FXML private TableColumn<Booking, String>  colStatus;

    private final HotelDataManager dm = HotelDataManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDisplay"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDisplay"));
        colPackageName.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusDisplay"));

        // Status colour coding
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setText(null); setStyle(""); return; }
                setText(status);
                setStyle("Active".equals(status)
                        ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                        : "-fx-text-fill: #7f8c8d;");
            }
        });

        refreshCombos();
        refreshTable();

        // ── DatePicker constraints ────────────────────────────────────────
        // Check-in: disable past dates in the calendar pop-up
        checkInPicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #bbb;");
                }
            }
        });

        // Check-out: disable dates on or before the selected check-in date
        checkOutPicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate cin = checkInPicker.getValue();
                LocalDate earliest = (cin != null) ? cin.plusDays(1) : LocalDate.now().plusDays(1);
                if (date.isBefore(earliest)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #bbb;");
                }
            }
        });
        // Re-validate check-out whenever check-in changes
        checkInPicker.valueProperty().addListener((obs, old, newVal) -> {
            // Clear check-out if it is now invalid relative to the new check-in
            LocalDate cout = checkOutPicker.getValue();
            if (cout != null && newVal != null && !cout.isAfter(newVal)) {
                checkOutPicker.setValue(null);
            }
        });
        // ── End DatePicker constraints ────────────────────────────────────

        // Listen for customer / check-in changes to show birthday notice
        customerCombo.setOnAction(e -> checkBirthday());
        checkInPicker.setOnAction(e -> checkBirthday());
    }

    private void checkBirthday() {
        Customer c = customerCombo.getValue();
        LocalDate cin = checkInPicker.getValue();
        if (c != null && cin != null && c.isBirthdayOn(cin)) {
            birthdayLabel.setText("🎂 Birthday! 8% discount will be applied.");
            birthdayLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
        } else {
            birthdayLabel.setText("");
        }
    }

    @FXML
    private void handleBook() {
        Customer customer = customerCombo.getValue();
        Room room         = roomCombo.getValue();
        LocalDate cin     = checkInPicker.getValue();
        LocalDate cout    = checkOutPicker.getValue();

        if (customer == null || room == null || cin == null || cout == null) {
            AlertUtil.showWarning("Validation", "Please fill in all booking fields.");
            return;
        }

        // ── DatePicker validation ─────────────────────────────────────────
        if (cin.isBefore(LocalDate.now())) {
            AlertUtil.showError("Invalid Date",
                    "Check-in date cannot be in the past.\nPlease select today or a future date.");
            checkInPicker.requestFocus();
            return;
        }
        if (!cout.isAfter(cin)) {
            AlertUtil.showError("Invalid Date",
                    "Check-out date must be AFTER the check-in date.\n"
                    + "Check-in: "  + cin  + "\n"
                    + "Check-out: " + cout);
            checkOutPicker.requestFocus();
            return;
        }
        // ── End date validation ───────────────────────────────────────────

        if (!room.isAvailable()) {
            AlertUtil.showError("Room Unavailable", "Room " + room.getRoomNumber() + " is already occupied.");
            return;
        }

        String bookingId = dm.generateBookingId();
        Booking booking = new Booking(bookingId, customer, room, cin, cout);
        dm.addBooking(booking);

        refreshCombos();
        refreshTable();
        clearBookingFields();

        String discountMsg = booking.getDiscountPercent() > 0
                ? "\n🎂 Birthday discount of 8% applied!" : "";
        AlertUtil.showInfo("Booking Confirmed",
                "Booking ID: " + bookingId + " created successfully." + discountMsg);
    }

    @FXML
    private void handleRefresh() {
        refreshCombos();
        refreshTable();
    }

    public void refreshCombos() {
        customerCombo.setItems(FXCollections.observableArrayList(dm.getAllCustomers()));
        roomCombo.setItems(FXCollections.observableArrayList(dm.getAvailableRooms()));
    }

    public void refreshTable() {
        bookingTable.setItems(FXCollections.observableArrayList(dm.getAllBookings()));
    }

    private void clearBookingFields() {
        customerCombo.setValue(null);
        roomCombo.setValue(null);
        checkInPicker.setValue(null);
        checkOutPicker.setValue(null);
        birthdayLabel.setText("");
    }
}
