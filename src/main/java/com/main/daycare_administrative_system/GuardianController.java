package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class GuardianController implements Initializable {
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static Connection connect = null;
    private static final String url = "jdbc:mysql://localhost:3306/daycare";
    private static final  String user = "root";
    private static final String pass = "pass";
    public static boolean refreshOnAdd;
    public static int idForSelect;

    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private VBox container;
    @FXML private Button back;
    @FXML private Button add;
    @FXML private Button refresh;

    /* Establishes connection to the MySQL Database */
    private static void connection() {
        try {
            connect = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* Closes connection to the MySQL Database */
    private static void closeConnection() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connect != null) {
            try {
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void retrieveGuardians() {
        connection();

        try {
            preparedStatement = connect.prepareStatement("SELECT " +
                    "guardian.guardian_ID," +
                    "guardian.first_name, " +
                    "guardian.last_name, " +
                    "guardian.image, " +
                    "guardian.cpr, " +
                    "guardian.date_of_birth, " +
                    "guardian.gender, " +
                    "telephone.telephone_number, " +
                    "guardian.child_ID, " +
                    "child.first_name childF, " +
                    "child.last_name childL " +
                    "FROM daycare.guardian " +
                    "JOIN daycare.telephone " +
                    "USING (guardian_ID) " +
                    "JOIN daycare.child USING (child_ID);");

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                injectGuardian(
                        resultSet.getInt("guardian_ID"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("image"),
                        resultSet.getString("cpr"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("telephone_number"),
                        resultSet.getInt("child_ID"),
                        resultSet.getString("childF"),
                        resultSet.getString("childL")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void injectGuardian(int id, String firstName, String lastName, String pic, String cpr, Date date, String gender, String telephone_number, Integer child_ID, String childFirstName, String childLastName) {
        // Setting up HBox container for guardian instance
        AnchorPane innerContainer = new AnchorPane();
        innerContainer.setPrefHeight(100.0);
        innerContainer.setPrefWidth(1280.0);
        innerContainer.setMinWidth(1250.0);
        innerContainer.setStyle("-fx-border-style: solid; -fx-border-color:#b3b3b3;");

        // Setting up ImageView for guardian profile picture
        ImageView img = new ImageView();
        img.setFitHeight(64);
        img.setFitWidth(64);
        img.setPickOnBounds(true);
        img.setPreserveRatio(true);
        Image image;
        image = new Image(Objects.requireNonNullElse(pic, "file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png"));
        img.setImage(image);


        // Setting up name label
        Label fullName = new Label();
        if (firstName != null && lastName != null) {
            fullName.setText(firstName.concat(" ").concat(lastName));
        } else {
            fullName.setText("Name Unknown");
        }
        fullName.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 20));

        // Setting up date of birth label
        Label dob = new Label();
        if (date != null) {
            dob.setText(date.toString());
        } else {
            dob.setText("Date of Birth Unknown");
        }
        dob.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));

        // Setting up CPR label
        Label c = new Label();
        if (cpr != null) {
            c.setText(cpr);
        } else {
            c.setText("CPR Unknown");
        }
        c.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));

        // Setting up gender label
        Label g = new Label();
        if (gender != null) {
            switch (gender) {
                case "M" -> g.setText("Male");
                case "F" -> g.setText("Female");
                case "N" -> g.setText("Non-Binary");
                case "D" -> g.setText("Decline to State");
            }
        } else {
            g.setText("Gender Unknown");
        }
        g.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));

        // Setting up phone number label
        Label phone = new Label();
        if (telephone_number != null) {
            phone.setText(telephone_number);
        } else {
           phone.setText("N/A");
        }
        phone.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));

        Label child = new Label();
        if (child_ID != null) {
            child.setText(childFirstName + " " + childLastName);
        } else {
            phone.setText("N/A");
        }
        phone.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));

        // Setting up Edit button
        Button edit = new Button();
        edit.setText("Edit");
        edit.setMinWidth(54.0);
        edit.setStyle("-fx-background-color: #7be3ad");
        edit.setOnMouseEntered(e -> edit.setStyle("-fx-background-color: #4bc190;"));
        edit.setOnMouseExited(e -> edit.setStyle("-fx-background-color: #7be3ad;"));
        edit.setMnemonicParsing(false);

        // Setting up Delete button
        Button delete = new Button();
        delete.setText("Delete");
        delete.setMinWidth(54.0);
        delete.setStyle("-fx-background-color: #7be3ad");
        delete.setOnMouseEntered(e -> delete.setStyle("-fx-background-color: red"));
        delete.setOnMouseExited(e -> delete.setStyle("-fx-background-color: #7be3ad;"));
        delete.setMnemonicParsing(false);

        // Adding edit functionality
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (pic != null) {
                    GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianImageURL(pic);
                } else {
                    GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianImageURL("file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png");
                }
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianID(id);
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianFirstN(firstName);
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianLastN(lastName);
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianCPR(cpr);
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianDoB(date);
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianGender(gender);
                GuardianEditController.GuardianToFields.getGuardianToFields().setGuardianTelephone(telephone_number);
                GuardianEditController.GuardianToFields.getGuardianToFields().setChildID(child_ID);
                GuardianEditController.GuardianToFields.getGuardianToFields().setChildFirstN(childFirstName);
                GuardianEditController.GuardianToFields.getGuardianToFields().setChildLastN(childLastName);
                Utilities.popUp("guardian_edit.fxml", "Edit Guardian");

                // Update guardian dynamically
                try {
                    connection();
                    preparedStatement = connect.prepareStatement("SELECT * FROM daycare.guardian " +
                            "LEFT OUTER JOIN daycare.telephone USING (guardian_ID) " +
                            "LEFT OUTER JOIN daycare.child USING (child_ID)" +
                            "WHERE guardian_ID = ?");
                    preparedStatement.setInt(1,GuardianEditController.GuardianToFields.getGuardianToFields().getGuardianID());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    Image i = new Image(resultSet.getString("image"));
                    img.setImage(i);
                    fullName.setText(resultSet.getString("first_name").concat(" ").concat(resultSet.getString("last_name")));
                    dob.setText(resultSet.getDate("date_of_birth").toString());
                    c.setText(resultSet.getString("cpr"));
                    switch (resultSet.getString("gender")) {
                        case "M" -> g.setText("Male");
                        case "F" -> g.setText("Female");
                        case "N" -> g.setText("Non-Binary");
                        case "D" -> g.setText("Decline to State");
                    }
                    phone.setText(resultSet.getString("telephone_number"));
                    child.setText(resultSet.getString("child.first_name") + " " + resultSet.getString("child.last_name"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Adding delete functionality
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    Alert alert0 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "+ fullName.getText() + "?", ButtonType.YES, ButtonType.NO);
                    alert0.setHeaderText("Please Confirm Guardian Deletion");
                    alert0.setTitle("Guardian Deletion");

                    // Has to be done like this
                    // Plain alert0.setGraphic(img) removes the image from the child instance node
                    ImageView tempImage = new ImageView();
                    tempImage.setImage(img.getImage());
                    // Must explicitly size image, otherwise it preserves original size and makes pop-up grow
                    tempImage.setFitHeight(64.0);
                    tempImage.setFitWidth(64.0);
                    alert0.setGraphic(tempImage);

                    Stage popStage = (Stage) alert0.getDialogPane().getScene().getWindow();
                    popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));

                    alert0.showAndWait();

                    if (alert0.getResult() == ButtonType.YES) {
                        connection();
                        try {
                            // Execute SQL statement
                            preparedStatement = connect.prepareStatement("DELETE FROM daycare.guardian WHERE guardian_ID = ?");
                            preparedStatement.setInt(1, id);
                            preparedStatement.execute();

                            // Refresh the window
                            Stage stage = (Stage) delete.getScene().getWindow();
                            stage.close();
                            Utilities.changeScene(actionEvent,"guardian.fxml","Guardian menu",null,true, true, 0,0);

                            // Display success alert
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Guardian Removed!");
                            alert.show();

                        } catch (SQLException e) {
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Guardian not removed (SQL Error)");
                            alert.show();
                        }
                    }
                }
        });

        innerContainer.getChildren().addAll(img, fullName, dob, c, g, phone, child, edit, delete);

        // Adding margin to all elements
        img.setLayoutX(80.0);
        img.setLayoutY(20.0);

        fullName.setLayoutX(200.0);
        fullName.setLayoutY(40.0);

        dob.setLayoutX(450.0);
        dob.setLayoutY(42.5);

        c.setLayoutX(575.0);
        c.setLayoutY(42.5);

        g.setLayoutX(650.0);
        g.setLayoutY(42.5);

        phone.setLayoutX(750.0);
        phone.setLayoutY(42.5);

        child.setLayoutX(875.0);
        child.setLayoutY(42.5);

        edit.setLayoutX(1080.0);
        edit.setLayoutY(40.0);

        delete.setLayoutX(1159.0);
        delete.setLayoutY(40.0);

        container.getChildren().add(innerContainer);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(Utilities.ConnectedUser.getConnectedUser().getFirstName().concat(" ").concat(Utilities.ConnectedUser.getConnectedUser().getLastName()));
        userImage.setImage(new Image(Utilities.ConnectedUser.getConnectedUser().getImage()));
        retrieveGuardians();

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Utilities.changeScene(actionEvent, "main.fxml","Main Menu", null, true, true, 0,0);
            }
        });
        back.setOnMouseEntered(l->{
            back.setStyle("-fx-background-color: red;");
        });
        back.setOnMouseExited(l->{
            back.setStyle("-fx-background-color: #4bc190;");
        });

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Utilities.popUp("guardian_add.fxml", "Add Guardian");

                if (refreshOnAdd) {
                    Stage stage = (Stage) add.getScene().getWindow();
                    stage.close();
                    Utilities.changeScene(actionEvent, "guardian.fxml","Guardian Menu", null, true, true, 0,0);
                    refreshOnAdd = false;
                }
            }

        });
        add.setOnMouseEntered(l->{
            add.setStyle("-fx-background-color: #3b9675;");
        });
        add.setOnMouseExited(l->{
            add.setStyle("-fx-background-color: #4bc190;");
        });


        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) refresh.getScene().getWindow();
                stage.close();
                Utilities.changeScene(actionEvent, "guardian.fxml","Guardian Menu", null, true, true, 0,0);
            }
        });
        refresh.setOnMouseEntered(l->{
            refresh.setStyle("-fx-background-color: #3b9675;");
        });
        refresh.setOnMouseExited(l->{
            refresh.setStyle("-fx-background-color: #4bc190;");
        });
    }
}
