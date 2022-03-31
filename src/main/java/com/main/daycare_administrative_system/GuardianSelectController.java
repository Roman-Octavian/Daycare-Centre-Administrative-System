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
import javafx.scene.input.MouseEvent;
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

public class GuardianSelectController implements Initializable {

    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static Connection connect = null;
    private static final String url = "jdbc:mysql://localhost:3306/daycare";
    private static final  String user = "root";
    private static final String pass = "pass";

    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private VBox container;
    @FXML private Button back;

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

    public void retrieveChildren(String order) {
        connection();

        try {
            preparedStatement = connect.prepareStatement("SELECT " +
                    "child.child_ID," +
                    "child.first_name, " +
                    "child.last_name, " +
                    "child.image, " +
                    "child.cpr, " +
                    "child.date_of_birth, " +
                    "child.gender, " +
                    "guardian.first_name, " +
                    "guardian.last_name " +
                    "FROM daycare.child " +
                    "LEFT OUTER JOIN daycare.guardian " +
                    "USING (child_ID)" +
                    "ORDER BY child.first_name " + order +";");

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                injectGuardian(
                        resultSet.getInt("child_ID"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("image"),
                        resultSet.getString("cpr"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("guardian.first_name"),
                        resultSet.getString("guardian.last_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void injectGuardian(int id, String firstName, String lastName, String pic, String cpr, Date date, String gender, String guardianFirstName, String guardianLastName) {
        // Setting up HBox container for guardian instance
        AnchorPane innerContainer = new AnchorPane();
        innerContainer.setPrefHeight(100.0);
        innerContainer.setPrefWidth(1280.0);
        innerContainer.setMinWidth(1280.0);
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

        // Setting up guardian name
        Label guardian = new Label();
        if (guardianFirstName != null && guardianLastName != null) {
            guardian.setText(guardianFirstName.concat(" ").concat(guardianLastName));
        } else {
            guardian.setText("Guardian Unknown");
        }
        guardian.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));


        innerContainer.getChildren().addAll(img, fullName, dob, c, g, guardian);

        // Adding margin to all elements
        img.setLayoutX(80.0);
        img.setLayoutY(20.0);

        fullName.setLayoutX(200.0);
        fullName.setLayoutY(40.0);

        dob.setLayoutX(450.0);
        dob.setLayoutY(42.5);

        c.setLayoutX(600.0);
        c.setLayoutY(42.5);

        g.setLayoutX(750.0);
        g.setLayoutY(42.5);

        guardian.setLayoutX(900.0);
        guardian.setLayoutY(42.5);

        innerContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                GuardianController.idForSelect = id;
                Stage stage = (Stage) innerContainer.getScene().getWindow();
                stage.close();
            }
        });

       innerContainer.setOnMouseEntered(l->{
           innerContainer.setStyle("-fx-border-style: solid; -fx-border-color:#b3b3b3; -fx-background-color: #3b9675;");
        });
        innerContainer.setOnMouseExited(l->{
            innerContainer.setStyle("-fx-border-style: solid; -fx-border-color:#b3b3b3;");
        });

        container.getChildren().add(innerContainer);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(Utilities.ConnectedUser.getConnectedUser().getFirstName().concat(" ").concat(Utilities.ConnectedUser.getConnectedUser().getLastName()));
        userImage.setImage(new Image(Utilities.ConnectedUser.getConnectedUser().getImage()));
        retrieveChildren("ASC");

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) back.getScene().getWindow();
                stage.close();
            }
        });
        back.setOnMouseEntered(l->{
            back.setStyle("-fx-background-color: red;");
        });
        back.setOnMouseExited(l->{
            back.setStyle("-fx-background-color: #4bc190;");
        });
    }
}
