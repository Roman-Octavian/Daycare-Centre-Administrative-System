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

public class GuardianEditController implements Initializable {


    /** Inner class that makes use of a Singleton pattern to load the guardian into the fields
     * The information can then be utilized to dynamically customize the GUI anywhere */
    public static class GuardianToFields {
        private static GuardianToFields guardianToAdd = new GuardianToFields();
        private int guardianID, childID;
        private String guardianFirstN, guardianLastN, guardianImageURL, guardianCPR, guardianGender, guardianTelephone, childFirstN, childLastN;
        private Date guardianDoB;

        private GuardianToFields() {
        }

        public static GuardianToFields getGuardianToFields() {
            return guardianToAdd;
        }

        public int getGuardianID() {
            return guardianID;
        }

        public void setGuardianID(int guardianID) {
            this.guardianID = guardianID;
        }


        public String getGuardianFirstN() {
            return guardianFirstN;
        }

        public void setGuardianFirstN(String guardianFirstN) {
            this.guardianFirstN = guardianFirstN;
        }

        public String getGuardianLastN() {
            return guardianLastN;
        }

        public void setGuardianLastN(String guardianLastN) {
            this.guardianLastN = guardianLastN;
        }

        public String getGuardianImageURL() {
            return guardianImageURL;
        }

        public void setGuardianImageURL(String guardianImageURL) {
            this.guardianImageURL = guardianImageURL;
        }

        public String getGuardianCPR() {
            return guardianCPR;
        }

        public void setGuardianCPR(String guardianCPR) {
            this.guardianCPR = guardianCPR;
        }

        public String getGuardianGender() {
            return guardianGender;
        }

        public void setGuardianGender(String guardianGender) {
            this.guardianGender = guardianGender;
        }

        public Date getGuardianDoB() {
            return guardianDoB;
        }

        public void setGuardianDoB(Date guardianDoB) {
            this.guardianDoB = guardianDoB;
        }

        public String getGuardianTelephone() {
            return guardianTelephone;
        }

        public void setGuardianTelephone(String guardianTelephone) {
            this.guardianTelephone = guardianTelephone;
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
    }

    private String imageURL;

    @FXML private Button submit;
    @FXML private Button cancel;
    @FXML private Button selectImage;
    @FXML private TextField iFirstName;
    @FXML private TextField iLastName;
    @FXML private TextField iCPR;
    @FXML private MenuButton iGender;
    @FXML private TextField iTelephone;
    @FXML private DatePicker iDoB;
    @FXML private MenuItem male;
    @FXML private MenuItem female;
    @FXML private MenuItem nonBinary;
    @FXML private MenuItem declineTS;
    @FXML private ImageView previewImage;
    @FXML private Button iChildIDButton;
    @FXML private TextField iChildIDField;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        iFirstName.setText(GuardianToFields.getGuardianToFields().getGuardianFirstN());
        iLastName.setText(GuardianToFields.getGuardianToFields().getGuardianLastN());
        iCPR.setText(GuardianToFields.getGuardianToFields().getGuardianCPR());
        iGender.setText(GuardianToFields.getGuardianToFields().getGuardianGender());
        iTelephone.setText(GuardianToFields.getGuardianToFields().getGuardianTelephone());
        iChildIDField.setText(String.valueOf(GuardianToFields.getGuardianToFields().getChildID()));
        iDoB.setValue(GuardianToFields.getGuardianToFields().getGuardianDoB().toLocalDate());
        setImageURL(GuardianToFields.getGuardianToFields().getGuardianImageURL());
        Image i = new Image(imageURL);
        previewImage.setImage(i);

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
                boolean check = Utilities.editGuardian(
                        GuardianToFields.getGuardianToFields().getGuardianID(),
                        imageURL,
                        iFirstName.getText(),
                        iLastName.getText(),
                        iCPR.getText(),
                        iTelephone.getText(),
                        Date.valueOf(iDoB.getValue()),
                        iGender.getText(),
                        Integer.parseInt(iChildIDField.getText()));


                Alert alert;
                if (check) {
                    // Close windows
                    Stage stage = (Stage) submit.getScene().getWindow();
                    stage.close();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Guardian Edited Successfully!");
                    alert.setTitle("Guardian Edit");
                    alert.setHeaderText("Operation Finalized");
                    Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Something went wrong! Check for errors in your fields.");
                    alert.setTitle("Guardian Edit");
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

        iChildIDButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Utilities.popUp("guardian_select.fxml", "Select Child");
                iChildIDField.setText(String.valueOf(GuardianController.idForSelect));
            }
        });
    }
}