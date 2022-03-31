package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffAddController implements Initializable {

    private String imageURL = "file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png";

    @FXML private Button submit;
    @FXML private Button cancel;
    @FXML private Button selectImage;
    @FXML private TextField iFirstName;
    @FXML private TextField iLastName;
    @FXML private TextField iCPR;
    @FXML private MenuButton iGender;
    @FXML private TextField iTelephone;
    @FXML private TextField iRole;
    @FXML private DatePicker iDoB;
    @FXML private TextField iUser;
    @FXML private PasswordField iPass;
    @FXML private CheckBox iAdmin;
    @FXML private MenuItem male;
    @FXML private MenuItem female;
    @FXML private MenuItem nonBinary;
    @FXML private MenuItem declineTS;
    @FXML private ImageView previewImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) cancel.getScene().getWindow();
                stage.close();
            }
        });
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean check = Utilities.addStaff(imageURL, iFirstName.getText(), iLastName.getText(), iCPR.getText(),java.sql.Date.valueOf(iDoB.getValue()),iGender.getText(), iRole.getText(), iTelephone.getText(), iUser.getText(), iPass.getText(), iAdmin.isSelected());
                Alert alert;
                if (check) {
                    // Close windows
                    Stage stage = (Stage) submit.getScene().getWindow();
                    stage.close();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Staff Added Successfully!");
                    alert.setTitle("Add Staff");
                    alert.setHeaderText("Operation Finalized");
                    Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));

                    StaffController.refreshOnAdd = true;

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong!");
                    alert.setTitle("Add Staff");
                    alert.setHeaderText("Operation Failed");
                    Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));
                }
                alert.show();
            }
        });
        male.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iGender.setText("M");
            }
        });
        female.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iGender.setText("F");
            }
        });
        nonBinary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iGender.setText("N");
            }
        });
        declineTS.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iGender.setText("D");
            }
        });

        // Works, but is bound to my machine; could not get platform-independent version to work.
        selectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PNG images","*.png"),
                        new FileChooser.ExtensionFilter("JPG images", "*.jpg"),
                        new FileChooser.ExtensionFilter("GIF images", "*.gif")
                );
                Stage stage = (Stage) selectImage.getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);
                try {
                    selectedFile.renameTo(new File("C:/Users/octav/IdeaProjects/Daycare_Administrative_System/src/main/resources/com/main/daycare_administrative_system/assets/".concat(selectedFile.getName())));
                    Image image = new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/".concat(selectedFile.getName()));
                    previewImage.setImage(image);
                    imageURL = image.getUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong!");
                    alert.show();
                }
            }
        });
    }
}
