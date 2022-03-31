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
import java.sql.Date;
import java.util.ResourceBundle;

public class ChildEditController implements Initializable {



    /** Inner class that makes use of a Singleton pattern to load the child into the fields
     * The information can then be utilized to dynamically customize the GUI anywhere */
    public static class ChildToFields {
        private static ChildToFields childToAdd = new ChildToFields();
        private int childID;
        private String childFirstN, childLastN, childImageURL, childCPR, childGender;
        private Date childDoB;

        private ChildToFields() {
        }

        public static ChildToFields getChildToFields() {
            return childToAdd;
        }

        public int getChildID() {
            return childID;
        }

        public void setChildID(int childID) {
            this.childID = childID;
        }

        public String getChildFirstN() {
            return childFirstN;
        }

        public void setChildFirstN(String childFirstN) {
            this.childFirstN = childFirstN;
        }

        public String getChildLastN() {
            return childLastN;
        }

        public void setChildLastN(String childLastN) {
            this.childLastN = childLastN;
        }

        public String getChildImageURL() {
            return childImageURL;
        }

        public void setChildImageURL(String childImageURL) {
            this.childImageURL = childImageURL;
        }

        public String getChildCPR() {
            return childCPR;
        }

        public void setChildCPR(String childCPR) {
            this.childCPR = childCPR;
        }

        public String getChildGender() {
            return childGender;
        }

        public void setChildGender(String childGender) {
            this.childGender = childGender;
        }

        public Date getChildDoB() {
            return childDoB;
        }

        public void setChildDoB(Date childDoB) {
            this.childDoB = childDoB;
        }

    }





    @FXML private Button submit;
    @FXML private Button cancel;
    @FXML private Button selectImage;
    @FXML private TextField iFirstName;
    @FXML private TextField iLastName;
    @FXML private TextField iCPR;
    @FXML private MenuButton iGender;
    @FXML private DatePicker iDoB;
    @FXML private MenuItem male;
    @FXML private MenuItem female;
    @FXML private MenuItem nonBinary;
    @FXML private MenuItem declineTS;
    @FXML private ImageView previewImage;

    // Auxiliary to the ImageView
    private String imageURL;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Load child data into fields from Singleton
        iFirstName.setText(ChildToFields.getChildToFields().getChildFirstN());
        iLastName.setText(ChildToFields.getChildToFields().getChildLastN());
        iCPR.setText(ChildToFields.getChildToFields().getChildCPR());
        iGender.setText(ChildToFields.getChildToFields().getChildGender());
        iDoB.setValue(ChildToFields.getChildToFields().getChildDoB().toLocalDate());
        setImageURL(ChildToFields.getChildToFields().getChildImageURL());
        Image i = new Image(imageURL);
        previewImage.setImage(i);

        // Cancel button functionality
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) cancel.getScene().getWindow();
                stage.close();
            }
        });

        // Submit button functionality
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean check = Utilities.editChild(ChildToFields.getChildToFields().getChildID(),imageURL,iFirstName.getText(),iLastName.getText(),iCPR.getText(),java.sql.Date.valueOf(iDoB.getValue()),iGender.getText());
                Alert alert;
                if (check) {
                    // Close windows
                    Stage stage = (Stage) submit.getScene().getWindow();
                    stage.close();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Child Edited Successfully!");
                    alert.setTitle("Child Edit");
                    alert.setHeaderText("Operation Finalized");
                    Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong! Check for errors in your fields.");
                    alert.setTitle("Child Edit");
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

        // Works, but is independent to my machine; could not get platform-independent version to work.
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
                    alert.setTitle("Image Submission");
                    alert.setHeaderText("Operation Failed");
                    Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));
                    alert.show();
                }
            }
        });
    }
}