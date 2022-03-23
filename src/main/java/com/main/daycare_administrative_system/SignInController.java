package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML
    private Button signInButton;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Utilities.loginUser(actionEvent, userField.getText(), passwordField.getText());
                userField.setText("");
                passwordField.setText("");
                userField.requestFocus();
            }
        });
        signInButton.setOnMouseEntered(l->{
            signInButton.setStyle("-fx-background-color:  #347c36;");
        });
        signInButton.setOnMouseExited(l->{
            signInButton.setStyle("-fx-background-color:  #49ae4c;");
        });
    }
}