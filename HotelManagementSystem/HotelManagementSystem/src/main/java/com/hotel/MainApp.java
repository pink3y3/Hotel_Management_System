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

        Scene scene = new Scene(root, 480, 360);
        scene.getStylesheets().add(
                getClass().getResource("/com/hotel/css/hotel.css").toExternalForm());

        primaryStage.setTitle("Horizon Hotel — Admin Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Gracefully shut down the e-mail thread pool
        EmailService.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
