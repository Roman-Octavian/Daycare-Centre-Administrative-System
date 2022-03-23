package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class ChildAddController implements Initializable {

    private String imageURL;

    @FXML
    private Button submit;
    @FXML
    private Button cancel;
    @FXML
    private Button selectImage;
    @FXML
    private TextField iFirstName;
    @FXML
    private TextField iLastName;
    @FXML
    private TextField iCPR;
    @FXML
    private MenuButton iGender;
    @FXML
    private DatePicker iDoB;
    @FXML
    private MenuItem male;
    @FXML
    private MenuItem female;
    @FXML
    private MenuItem nonBinary;
    @FXML
    private MenuItem declineTS;
    @FXML
    private ImageView previewImage;


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
                boolean check = Utilities.addChild(imageURL,iFirstName.getText(),iLastName.getText(),iCPR.getText(),java.sql.Date.valueOf(iDoB.getValue()),iGender.getText());
                Alert alert;
                if (check) {
                    // Close windows
                    Stage stage = (Stage) submit.getScene().getWindow();
                    stage.close();

                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Child Added!");

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong!");
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
                        new FileChooser.ExtensionFilter("JPG images", "*.jpg")
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

        /* Does not work. Depending on target directory format, it either throws PermissionDeniedException or it sends the file to the shadow realm (file disappears)

        selectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PNG images","*.png"),
                        new FileChooser.ExtensionFilter("JPG images", "*.jpg")
                );
                Stage stage = (Stage) selectImage.getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);
                try {
                    Files.move(Paths.get(selectedFile.getPath()),Paths.get("com.main.daycare_administrative_system.assets"), StandardCopyOption.ATOMIC_MOVE);
                    Image image = new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/".concat(selectedFile.getName()));
                    previewImage.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong!");
                    alert.show();
                }
            }
        }); */
    }
}
