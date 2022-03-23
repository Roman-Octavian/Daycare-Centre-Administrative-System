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

public class StaffEditController implements Initializable {


    /** Inner class that makes use of a Singleton pattern to load the staff into the fields
     * The information can then be utilized to dynamically customize the GUI anywhere */
    public static class StaffToFields {
        private static StaffToFields staffToAdd = new StaffToFields();
        private int staffID, userID;
        private String staffFirstN, staffLastN, staffImageURL, staffCPR, staffGender, userName, userPass;
        private Date staffDoB;
        private Boolean admin;

        private StaffToFields() {
        }

        public StaffToFields(int staffID, int userID, String staffFirstN, String staffLastN, String staffImageURL, String staffCPR, String staffGender, String userName, String userPass, Date staffDoB, Boolean admin) {
            this.staffID = staffID;
            this.userID = userID;
            this.staffFirstN = staffFirstN;
            this.staffLastN = staffLastN;
            this.staffImageURL = staffImageURL;
            this.staffCPR = staffCPR;
            this.staffGender = staffGender;
            this.userName = userName;
            this.userPass = userPass;
            this.staffDoB = staffDoB;
            this.admin = admin;
        }

        public static StaffToFields getStaffToFields() {
            return staffToAdd;
        }

        public static StaffToFields getStaffToAdd() {
            return staffToAdd;
        }

        public static void setStaffToAdd(StaffToFields staffToAdd) {
            StaffToFields.staffToAdd = staffToAdd;
        }

        public int getStaffID() {
            return staffID;
        }

        public void setStaffID(int staffID) {
            this.staffID = staffID;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getStaffFirstN() {
            return staffFirstN;
        }

        public void setStaffFirstN(String staffFirstN) {
            this.staffFirstN = staffFirstN;
        }

        public String getStaffLastN() {
            return staffLastN;
        }

        public void setStaffLastN(String staffLastN) {
            this.staffLastN = staffLastN;
        }

        public String getStaffImageURL() {
            return staffImageURL;
        }

        public void setStaffImageURL(String staffImageURL) {
            this.staffImageURL = staffImageURL;
        }

        public String getStaffCPR() {
            return staffCPR;
        }

        public void setStaffCPR(String staffCPR) {
            this.staffCPR = staffCPR;
        }

        public String getStaffGender() {
            return staffGender;
        }

        public void setStaffGender(String staffGender) {
            this.staffGender = staffGender;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPass() {
            return userPass;
        }

        public void setUserPass(String userPass) {
            this.userPass = userPass;
        }

        public Date getStaffDoB() {
            return staffDoB;
        }

        public void setStaffDoB(Date staffDoB) {
            this.staffDoB = staffDoB;
        }

        public Boolean getAdmin() {
            return admin;
        }

        public void setAdmin(Boolean admin) {
            this.admin = admin;
        }
    }

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
    private TextField iUser;
    @FXML
    private PasswordField iPass;
    @FXML
    private CheckBox iAdmin;
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


    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        iFirstName.setText(StaffToFields.getStaffToFields().getStaffFirstN());
        iLastName.setText(StaffToFields.getStaffToFields().getStaffLastN());
        iCPR.setText(StaffToFields.getStaffToFields().getStaffCPR());
        iGender.setText(StaffToFields.getStaffToFields().getStaffGender());
        iDoB.setValue(StaffToFields.getStaffToFields().getStaffDoB().toLocalDate());
        iUser.setText(StaffToFields.getStaffToFields().getUserName());
        iPass.setText(StaffToFields.getStaffToFields().getUserPass());
        iAdmin.setSelected(StaffToFields.getStaffToFields().getAdmin());
        if (imageURL != null) {
            setImageURL(StaffToFields.getStaffToFields().getStaffImageURL());
        } else {
            setImageURL("file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png");
        }

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
                boolean check = Utilities.editStaff(StaffToFields.getStaffToFields().getStaffID(),StaffToFields.getStaffToFields().getUserID(),iUser.getText(),iPass.getText(),iAdmin.isSelected(),imageURL,iFirstName.getText(),iLastName.getText(),iCPR.getText(), Date.valueOf(iDoB.getValue()),iGender.getText());
                Alert alert;
                if (check) {
                    // Close windows
                    Stage stage = (Stage) submit.getScene().getWindow();
                    stage.close();

                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Staff Edited!");

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
                    alert.show();
                }
            }
        });
    }

}

