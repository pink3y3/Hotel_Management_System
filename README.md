# Hotel_Management_System

A JavaFX-based Hotel Management System built with Maven for managing rooms, customers, bookings, and billing.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| GUI Framework | JavaFX 17 |
| Build Tool | Maven |
| Data Storage | File Serialization |

## Project Structure
hotel-management/
├── pom.xml
└── src/main/java/com/hotel/
├── Main.java
├── model/
│   ├── Room.java
│   └── Customer.java
├── storage/
│   └── FileStorage.java
└── ui/
├── RoomTab.java
├── BookingTab.java
└── BillingTab.java

## Features

- Room Management — add, view, and filter available rooms
- Customer Management — capture and manage customer details
- Booking — book rooms with availability validation
- Checkout — release rooms and update availability
- Billing — auto-generate bill on checkout
- Persistent Storage — data saved to file using serialization

## Prerequisites

- Java 17 (OpenJDK / Temurin)
- Maven
- JavaFX 17 SDK

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/hotel-management.git
cd hotel-management
```

### 2. Run the application
```bash
mvn javafx:run
```

## GUI Overview

| Tab | Description |
|---|---|
| Rooms | Add new rooms, view all rooms, filter available rooms |
| Bookings | Book a room for a customer, prevent double booking |
| Billing | View and generate bill on customer checkout |


## Developed By
Atisha
