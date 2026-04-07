package com.hotel.model;

import java.io.Serializable;

/**
 * Room model with encapsulation and serialization support.
 * Demonstrates: Encapsulation, Enums, Serialization
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    // Enum for room types - demonstrates enum usage
    public enum RoomType {
        SINGLE("Single", 1),
        DOUBLE("Double", 2),
        SUITE("Suite", 4),
        DELUXE("Deluxe", 3);

        private final String displayName;
        private final int capacity;

        RoomType(String displayName, int capacity) {
            this.displayName = displayName;
            this.capacity = capacity;
        }

        public String getDisplayName() { return displayName; }
        public int getCapacity() { return capacity; }

        @Override
        public String toString() { return displayName; }
    }

    private String roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private boolean available;

    public Room() {}

    public Room(String roomNumber, RoomType roomType, double pricePerNight, boolean available) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    // Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getStatusDisplay() { return available ? "Available" : "Occupied"; }

    @Override
    public String toString() {
        return String.format("Room[%s | %s | $%.2f/night | %s]",
                roomNumber, roomType, pricePerNight, getStatusDisplay());
    }
}
