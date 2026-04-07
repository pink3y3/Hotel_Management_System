package com.hotel.util;

import com.hotel.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * HotelDataManager — Singleton service that owns all DataStore instances.
 * Provides high-level operations for the controllers.
 *
 * Demonstrates: Singleton pattern, encapsulation of persistence logic.
 */
public class HotelDataManager {

    private static HotelDataManager instance;

    private static final String DATA_DIR = System.getProperty("user.home") + "/HotelManagementData/";

    private final DataStore<Room>     roomStore;
    private final DataStore<Customer> customerStore;
    private final DataStore<Booking>  bookingStore;

    private int bookingCounter = 1;

    private HotelDataManager() {
        roomStore     = new DataStore<>(DATA_DIR + "rooms.dat");
        customerStore = new DataStore<>(DATA_DIR + "customers.dat");
        bookingStore  = new DataStore<>(DATA_DIR + "bookings.dat");
        // Sync counter from existing bookings
        bookingStore.getAll().forEach(b -> {
            String id = b.getBookingId();
            if (id != null && id.startsWith("BOOK")) {
                try {
                    int n = Integer.parseInt(id.substring(4));
                    if (n >= bookingCounter) bookingCounter = n + 1;
                } catch (NumberFormatException ignored) {}
            }
        });
    }

    public static HotelDataManager getInstance() {
        if (instance == null) instance = new HotelDataManager();
        return instance;
    }

    // ── Booking ID generation ────────────────────────────────────────────────

    /**
     * Generates sequential booking IDs: BOOK1, BOOK2, BOOK3, …
     */
    public synchronized String generateBookingId() {
        return "BOOK" + (bookingCounter++);
    }

    // ── Room operations ──────────────────────────────────────────────────────

    public void addRoom(Room room) { roomStore.add(room); }

    public List<Room> getAllRooms() { return roomStore.getAll(); }

    public List<Room> getAvailableRooms() {
        return roomStore.filter(Room::isAvailable);
    }

    public boolean roomNumberExists(String roomNumber) {
        return roomStore.getAll().stream()
                .anyMatch(r -> r.getRoomNumber().equalsIgnoreCase(roomNumber));
    }

    public void updateRoom(Room room) { roomStore.update(room); }

    public void saveRooms() { roomStore.saveAll(roomStore.getAll()); }

    // ── Customer operations ──────────────────────────────────────────────────

    public void addCustomer(Customer customer) { customerStore.add(customer); }

    public List<Customer> getAllCustomers() { return customerStore.getAll(); }

    public List<Customer> searchCustomers(String query) {
        if (query == null || query.isBlank()) return getAllCustomers();
        String q = query.toLowerCase();
        return customerStore.filter(c -> c.getName().toLowerCase().contains(q)
                || c.getCustomerId().toLowerCase().contains(q));
    }

    public String generateCustomerId() {
        return "CUST" + (customerStore.size() + 1);
    }

    public void saveCustomers() { customerStore.saveAll(customerStore.getAll()); }

    // ── Booking operations ───────────────────────────────────────────────────

    public void addBooking(Booking booking) {
        bookingStore.add(booking);
        // Mark room as occupied
        if (booking.getRoom() != null) {
            booking.getRoom().setAvailable(false);
            saveRooms();
        }
    }

    public List<Booking> getAllBookings() { return bookingStore.getAll(); }

    public List<Booking> getActiveBookings() {
        return bookingStore.filter(Booking::isActive);
    }

    public void checkoutBooking(Booking booking) {
        booking.setActive(false);
        if (booking.getRoom() != null) {
            booking.getRoom().setAvailable(true);
            saveRooms();
        }
        bookingStore.saveAll(bookingStore.getAll());
    }

    public void saveBookings() { bookingStore.saveAll(bookingStore.getAll()); }

    // ── Billing ──────────────────────────────────────────────────────────────

    public String generateBillText(Booking b) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       HORIZON HOTEL — INVOICE          \n");
        sb.append("========================================\n");
        sb.append(String.format("Booking ID    : %s%n", b.getBookingId()));
        sb.append(String.format("Customer      : %s%n", b.getCustomerName()));
        sb.append(String.format("Room          : %s (%s)%n",
                b.getRoomNumber(),
                b.getRoom() != null ? b.getRoom().getRoomType() : ""));
        sb.append(String.format("Check-in      : %s%n", b.getCheckInDisplay()));
        sb.append(String.format("Check-out     : %s%n", b.getCheckOutDisplay()));
        sb.append(String.format("Nights        : %d%n", b.getNumberOfNights()));
        sb.append("----------------------------------------\n");
        sb.append(String.format("Room Charges  : $%.2f%n", b.getRoomCharges()));
        sb.append(String.format("Package (%s): $%.2f%n", b.getPackageName(), b.getPackageCost()));
        sb.append(String.format("Sub-Total     : $%.2f%n", b.getSubTotal()));
        if (b.getDiscountPercent() > 0) {
            sb.append(String.format("Discount (%.0f%%): -$%.2f%n",
                    b.getDiscountPercent(), b.getDiscountAmount()));
        }
        sb.append("========================================\n");
        sb.append(String.format("TOTAL PAYABLE : $%.2f%n", b.getFinalBill()));
        sb.append("========================================\n");
        if (b.getDiscountPercent() > 0) {
            sb.append("* Birthday discount applied!\n");
        }
        return sb.toString();
    }
}
