package com.hotel;

import com.hotel.util.EmailService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/hotel/fxml/Login.fxml"));
        Parent root = loader.load();

        // Set window size here
        Scene scene = new Scene(root, 350, 420);

        // Attach CSS
        scene.getStylesheets().add(
                getClass().getResource("/com/hotel/css/hotel.css").toExternalForm());

        primaryStage.setTitle("The Stanley Hotel — Admin Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void stop() {
        EmailService.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}