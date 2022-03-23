package com.main.daycare_administrative_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Staff extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SignIn.class.getResource("staff.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 0, 0);
        stage.setTitle("Staff Menu");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
