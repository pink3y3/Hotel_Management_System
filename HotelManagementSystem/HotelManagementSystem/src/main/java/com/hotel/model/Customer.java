package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Customer model.
 * Demonstrates: Encapsulation, Serialization
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 2L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String customerId;
    private String name;
    private String contactNumber;
    private LocalDate dateOfBirth;

    public Customer() {}

    public Customer(String customerId, String name, String contactNumber, LocalDate dateOfBirth) {
        this.customerId = customerId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getDobDisplay() {
        return dateOfBirth != null ? dateOfBirth.format(DATE_FORMATTER) : "";
    }

    /**
     * Check if today or a given date matches customer's birthday (month & day).
     */
    public boolean isBirthdayOn(LocalDate date) {
        if (dateOfBirth == null || date == null) return false;
        return dateOfBirth.getMonthValue() == date.getMonthValue()
                && dateOfBirth.getDayOfMonth() == date.getDayOfMonth();
    }

    @Override
    public String toString() { return name + " (" + customerId + ")"; }
}
