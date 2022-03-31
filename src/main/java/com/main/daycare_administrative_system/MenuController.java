package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML private VBox childrenMenu;
    @FXML private VBox guardiansMenu;
    @FXML private VBox staffMenu;
    @FXML private VBox telephoneMenu;
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Button signOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(Utilities.ConnectedUser.getConnectedUser().getFirstName().concat(" ").concat(Utilities.ConnectedUser.getConnectedUser().getLastName()));
        Image i = new Image(Utilities.ConnectedUser.getConnectedUser().getImage());
        userImage.setImage(i);

        childrenMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Utilities.changeScene(mouseEvent,"child.fxml","Children Menu", null, true, true, 0,0);
            }
        });
        childrenMenu.setOnMouseEntered(l->{
            childrenMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        childrenMenu.setOnMouseExited(l->{
            childrenMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        guardiansMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Utilities.changeScene(mouseEvent,"guardian.fxml","Guardian Menu", null, true, true, 0,0);
            }
        });
        guardiansMenu.setOnMouseEntered(l->{
            guardiansMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        guardiansMenu.setOnMouseExited(l->{
            guardiansMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        staffMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (Utilities.ConnectedUser.getConnectedUser().getAdmin()) {
                    Utilities.changeScene(mouseEvent,"staff.fxml","Staff Menu", null, true, true, 0,0);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Access Denied; Contact your manager.");
                    alert.setTitle("Staff Menu");
                    alert.setHeaderText("Operation Failed");
                    Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));
                    alert.showAndWait();
                }
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
                Utilities.changeScene(mouseEvent,"telephone.fxml","Telephone List", null, true, true, 0,0);
            }
        });
        telephoneMenu.setOnMouseEntered(l->{
            telephoneMenu.setStyle("-fx-background-color:#4bc190; -fx-border-style: solid; -fx-scale-x: 120%; -fx-scale-y: 120%;");
        });
        telephoneMenu.setOnMouseExited(l->{
            telephoneMenu.setStyle("-fx-background-color:#ffffff; -fx-border-style: solid; -fx-border-radius: 5px;");
        });

        signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Confirmation alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirm Sign Out", ButtonType.YES, ButtonType.NO);
                alert.setContentText("Are you sure you want to log out?");
                alert.showAndWait();
                // If user selects "YES"
                if (alert.getResult() == ButtonType.YES) {
                    Utilities.changeScene(actionEvent, "authentication.fxml", "Authentication", null, false, false, 375, 565);
                    Utilities.ConnectedUser.unloadUserDetails();
                }
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
