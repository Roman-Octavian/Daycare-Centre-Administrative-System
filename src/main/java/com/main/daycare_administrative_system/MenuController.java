package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private VBox childrenMenu;
    @FXML
    private VBox parentsMenu;
    @FXML
    private VBox staffMenu;
    @FXML
    private VBox telephoneMenu;
    @FXML
    private VBox scheduleMenu;
    @FXML
    private ImageView userImage;
    @FXML
    private Label userName;
    @FXML
    private Button signOut;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(Utilities.ConnectedUser.getConnectedUser().getFirstName().concat(" ").concat(Utilities.ConnectedUser.getConnectedUser().getLastName()));
        Image i = new Image(Utilities.ConnectedUser.getConnectedUser().getImage());
        userImage.setImage(i);

        childrenMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Utilities.changeScene(mouseEvent,"child.fxml","Daycare Centre Administrative System", null, true,1200,800);
            }
        });
        childrenMenu.setOnMouseEntered(l->{
            childrenMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        childrenMenu.setOnMouseExited(l->{
            childrenMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        parentsMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Menu Clicked!");
            }
        });
        parentsMenu.setOnMouseEntered(l->{
            parentsMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        parentsMenu.setOnMouseExited(l->{
            parentsMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        staffMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Utilities.changeScene(mouseEvent,"staff.fxml","Staff Menu", null, true,1200,800);
            }
        });
        staffMenu.setOnMouseEntered(l->{
            staffMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        staffMenu.setOnMouseExited(l->{
            staffMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        telephoneMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Menu Clicked!");
            }
        });
        telephoneMenu.setOnMouseEntered(l->{
            telephoneMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        telephoneMenu.setOnMouseExited(l->{
            telephoneMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        scheduleMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("Menu Clicked!");
            }
        });
        scheduleMenu.setOnMouseEntered(l->{
            scheduleMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        scheduleMenu.setOnMouseExited(l->{
            scheduleMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Utilities.changeScene(actionEvent, "authentication.fxml","Daycare Centre Administrative System", null, false,375,565);
                Utilities.ConnectedUser.unloadUserDetails();
            }
        });
        signOut.setOnMouseEntered(l->{
            signOut.setStyle("-fx-background-color: red;");
        });
        signOut.setOnMouseExited(l->{
            signOut.setStyle("-fx-background-color:  #4bc190;");
        });
    }
}
