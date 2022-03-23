package com.main.daycare_administrative_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SignIn extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignIn.class.getResource("authentication.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 375, 565);
        stage.setTitle("Daycare Administrative System");
        Image icon = new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}