# 🏨 Hotel Management System

A modern **JavaFX-based Hotel Management System** designed for efficient hotel operations.  
This application allows administrators to manage rooms, customers, bookings, packages, and billing — all through an elegant UI.

---

## ✨ Features

### 🔐 Admin Login
- Secure login interface
- Single admin access

### 🛏 Room Management
- Add new rooms with type and pricing
- View all rooms
- Filter available rooms
- Automatic status updates (Available / Occupied)

### 👤 Customer Management
- Add customers with contact details
- Date of Birth selection (with validation)
- Search customers by name or ID

### 📋 Booking System
- Create bookings with:
  - Customer
  - Room
  - Check-in & Check-out (calendar picker)
- Auto-generated Booking IDs (BOOK1, BOOK2, …)
- Birthday discount (8%) applied automatically
- Booking history tracking

### 🎁 Packages
- Predefined packages (Basic, Premium, Luxury)
- Each package includes:
  - Description
  - Cost
- Assign packages to bookings

### 💳 Billing System
- View full bill breakdown:
  - Room charges
  - Package cost
  - Discount
  - Final amount
- Print bill 🖨
- Send e-bill via email 📧
- Booking-based billing to avoid confusion

### 🔄 Checkout System
- Manual checkout option
- Updates room availability automatically

### 🔔 Notifications
- Alerts for major actions (booking, customer added, etc.)

---

## 🎨 UI Design

- Built using **JavaFX + SceneBuilder**
- Theme: **Black, White, Gold (Luxury Hotel Style)**
- Clean and elegant layout with tab-based navigation

---

## 🛠 Technologies Used

- Java (JDK 17+)
- JavaFX
- SceneBuilder
- Maven
- FXML + CSS

---

## 📂 Project Structure
src/main/java/com/hotel/
├── controller/ # Controllers (UI logic)
├── model/ # Data models (Room, Booking, Customer)
├── util/ # Utilities (Data manager, alerts, email)

src/main/resources/com/hotel/
├── fxml/ # UI layouts
├── css/ # Styling


---

## 🚀 How to Run

### Using Maven:

mvn clean install
mvn javafx:run


### Or from IntelliJ:
- Run `MainApp.java`

---

## 📦 Data Storage

- Uses in-memory storage via `HotelDataManager`
- (Optional) Can be extended to use a real database (MySQL/PostgreSQL)

---

## 🔮 Future Improvements

- Integrate database (MySQL/PostgreSQL)
- Add multiple user roles (Admin/Staff)
- Online booking system
- Payment gateway integration
- Analytics dashboard

---

## 👩‍💻 Author

**Atisha Ariga**
---

## 📜 License

This project is for educational purposes.
