package com.main.daycare_administrative_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignIn.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 0, 0);
        stage.setTitle("Daycare Administrative System");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
