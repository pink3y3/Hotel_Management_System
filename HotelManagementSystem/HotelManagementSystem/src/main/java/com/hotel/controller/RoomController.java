package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.model.Room.RoomType;
import com.hotel.util.AlertUtil;
import com.hotel.util.HotelDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * RoomController — Rooms tab.
 * Handles: Add room, view all, filter available, colour-code status.
 */
public class RoomController implements Initializable {

    @FXML private TextField roomNumberField;
    @FXML private ComboBox<RoomType> roomTypeCombo;
    @FXML private TextField priceField;
    @FXML private CheckBox availableCheckBox;

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, String>  colRoomNumber;
    @FXML private TableColumn<Room, String>  colRoomType;
    @FXML private TableColumn<Room, Double>  colPrice;
    @FXML private TableColumn<Room, String>  colStatus;

    @FXML private CheckBox filterAvailableCheckBox;
    @FXML private TextField searchRoomField;

    private final HotelDataManager dm = HotelDataManager.getInstance();
    private ObservableList<Room> roomList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roomTypeCombo.setItems(FXCollections.observableArrayList(RoomType.values()));
        roomTypeCombo.getSelectionModel().selectFirst();
        availableCheckBox.setSelected(true);

        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusDisplay"));

        // Color-code status column
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("Available".equals(status)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        refreshTable();
    }

    @FXML
    private void handleAddRoom() {
        String roomNumber = roomNumberField.getText().trim();
        String priceText  = priceField.getText().trim();
        RoomType type     = roomTypeCombo.getValue();

        if (roomNumber.isEmpty() || priceText.isEmpty() || type == null) {
            AlertUtil.showWarning("Validation", "Please fill in all room fields.");
            return;
        }

        if (dm.roomNumberExists(roomNumber)) {
            AlertUtil.showError("Duplicate", "Room number '" + roomNumber + "' already exists.");
            return;
        }

        double price;
        try { price = Double.parseDouble(priceText); }
        catch (NumberFormatException e) {
            AlertUtil.showError("Validation", "Please enter a valid price.");
            return;
        }

        if (price <= 0) {
            AlertUtil.showError("Validation", "Price must be greater than zero.");
            return;
        }

        Room room = new Room(roomNumber, type, price, availableCheckBox.isSelected());
        dm.addRoom(room);
        refreshTable();
        clearRoomFields();
        AlertUtil.showInfo("Success", "Room " + roomNumber + " added successfully!");
    }

    @FXML
    private void handleFilter() {
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        refreshTable();
    }

    @FXML
    private void handleRefresh() {
        filterAvailableCheckBox.setSelected(false);
        searchRoomField.clear();
        refreshTable();
    }

    private void refreshTable() {
        List<Room> rooms = filterAvailableCheckBox.isSelected()
                ? dm.getAvailableRooms()
                : dm.getAllRooms();

        String query = searchRoomField.getText().trim().toLowerCase();
        if (!query.isEmpty()) {
            rooms = rooms.stream()
                    .filter(r -> r.getRoomNumber().toLowerCase().contains(query)
                            || r.getRoomType().toString().toLowerCase().contains(query))
                    .toList();
        }

        roomList = FXCollections.observableArrayList(rooms);
        roomTable.setItems(roomList);
    }

    private void clearRoomFields() {
        roomNumberField.clear();
        priceField.clear();
        roomTypeCombo.getSelectionModel().selectFirst();
        availableCheckBox.setSelected(true);
    }
}
