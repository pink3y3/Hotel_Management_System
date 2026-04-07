package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Booking model connecting Customer, Room, and HotelPackage.
 * Demonstrates: Encapsulation, Serialization, Association
 */
public class Booking implements Serializable {
    private static final long serialVersionUID = 4L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final double BIRTHDAY_DISCOUNT = 0.08; // 8%

    private String bookingId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private HotelPackage hotelPackage;
    private double discountPercent;
    private boolean active;

    public Booking() {
        this.hotelPackage = new HotelPackage();
        this.active = true;
    }

    public Booking(String bookingId, Customer customer, Room room,
                   LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.hotelPackage = new HotelPackage();
        this.active = true;

        // Apply birthday discount automatically
        if (customer != null && customer.isBirthdayOn(checkInDate)) {
            this.discountPercent = BIRTHDAY_DISCOUNT * 100;
        } else {
            this.discountPercent = 0.0;
        }
    }

    // ── Calculations ────────────────────────────────────────────────────────

    /** Number of nights between check-in and check-out. */
    public long getNumberOfNights() {
        if (checkInDate == null || checkOutDate == null) return 0;
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double getRoomCharges() {
        if (room == null) return 0;
        return room.getPricePerNight() * getNumberOfNights();
    }

    public double getPackageCost() {
        return hotelPackage != null ? hotelPackage.getCost() : 0;
    }

    public double getSubTotal() { return getRoomCharges() + getPackageCost(); }

    public double getDiscountAmount() { return getSubTotal() * (discountPercent / 100.0); }

    public double getFinalBill() { return getSubTotal() - getDiscountAmount(); }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

    public LocalDate getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

    public HotelPackage getHotelPackage() { return hotelPackage; }
    public void setHotelPackage(HotelPackage hotelPackage) { this.hotelPackage = hotelPackage; }

    public double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(double discountPercent) { this.discountPercent = discountPercent; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // ── Display helpers ──────────────────────────────────────────────────────

    public String getCustomerName() { return customer != null ? customer.getName() : ""; }
    public String getRoomNumber()   { return room != null ? room.getRoomNumber() : ""; }

    public String getCheckInDisplay()  { return checkInDate  != null ? checkInDate.format(DATE_FORMATTER)  : ""; }
    public String getCheckOutDisplay() { return checkOutDate != null ? checkOutDate.format(DATE_FORMATTER) : ""; }

    public String getPackageName() {
        return hotelPackage != null ? hotelPackage.toString() : "No Package";
    }

    public String getStatusDisplay() { return active ? "Active" : "Checked Out"; }

    @Override
    public String toString() {
        return String.format("Booking[%s | %s | Room %s | %s → %s | $%.2f]",
                bookingId, getCustomerName(), getRoomNumber(),
                getCheckInDisplay(), getCheckOutDisplay(), getFinalBill());
    }
}
