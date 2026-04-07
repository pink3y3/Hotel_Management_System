package com.hotel.controller;

import com.hotel.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * LoginController — handles admin authentication.
 *
 * Hardcoded credentials (admin / hotel123).
 */
public class LoginController {

    // Hardcoded admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "hotel123";

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showWarning("Login", "Please enter both username and password.");
            return;
        }

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            openDashboard();
        } else {
            AlertUtil.showError("Login Failed", "Invalid username or password.");
            passwordField.clear();
        }
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/hotel/fxml/Dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 720));
            stage.setTitle("Horizon Hotel Management System — Dashboard");
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtil.showError("Error", "Could not load dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        usernameField.clear();
        passwordField.clear();
        usernameField.requestFocus();
    }
}
