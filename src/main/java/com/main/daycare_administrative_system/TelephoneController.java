package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TelephoneController implements Initializable {

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

    /**
     * Takes phone number, first name, and last name, and generates a UI element AnchorPane.
     * This Pane can then be injected into a view dynamically every time the view is initialized.
     * @param t telephone number to print out
     * @param firstName to concatenate as first part of fullName
     * @param lastName to concatenate as last part of fullName
     */
    public void injectTelephone(String t, String firstName, String lastName) {
        // Setting up HBox container for guardian instance
        AnchorPane innerContainer = new AnchorPane();
        innerContainer.setPrefHeight(50.0);
        innerContainer.setPrefWidth(1280.0);
        innerContainer.setMinWidth(1280.0);
        innerContainer.setStyle("-fx-border-style: solid; -fx-border-color:#b3b3b3;");

        Label phone = new Label();
        if (t != null) {
            phone.setText(t);
        } else {
            phone.setText("N/A");
        }

        // Setting up name label
        Label fullName = new Label();
        if (firstName != null && lastName != null) {
            fullName.setText(firstName.concat(" ").concat(lastName));
        } else {
            fullName.setText("Name Unknown");
        }
        fullName.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));

        phone.setFont(Font.font("Calibri", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        phone.setStyle("-fx-color-label-visible: #4bc190");
        innerContainer.getChildren().addAll(fullName,phone);

        fullName.setLayoutX(400.0);
        fullName.setLayoutY(15.0);

        phone.setLayoutX(800.0);
        phone.setLayoutY(17.5);

        container.getChildren().add(innerContainer);
    }

    /**
     * Makes use of the injectTelephone function to dynamically generate the entries belonging to Staff members that can be found in the Telephone Menu.
     * Iterates through the entire 'staff' table, and for each instance, it injects a Pane element into the view with injectTelephone.
     */
    public void retrieveSTelephones() {
        connection();

        try {
            preparedStatement = connect.prepareStatement("SELECT " +
                    "staff.first_name, " +
                    "staff.last_name, " +
                    "telephone.telephone_number " +
                    "FROM daycare.telephone " +
                    "JOIN daycare.staff " +
                    "USING (staff_ID)" +
                    "ORDER BY first_name");

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                injectTelephone(
                        resultSet.getString("telephone_number"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Makes use of the injectTelephone function to dynamically generate the entries belonging to guardians that can be found in the Telephone Menu.
     * Iterates through the entire 'guardian' table, and for each instance, it injects a Pane element into the view with injectTelephone.
     */
    public void retrieveGTelephones() {
        connection();

        try {
            preparedStatement = connect.prepareStatement("SELECT " +
                    "guardian.first_name, " +
                    "guardian.last_name, " +
                    "telephone.telephone_number " +
                    "FROM daycare.telephone " +
                    "JOIN daycare.guardian " +
                    "USING (guardian_ID)" +
                    "ORDER BY first_name");

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                injectTelephone(
                        resultSet.getString("telephone_number"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set the username and image on the navbar to the logged user stored in the ConnectedUser singleton.
        // This should probably be a function, but I won't change too much "a posteriori" not to impact the accuracy of the code review.
        userName.setText(Utilities.ConnectedUser.getConnectedUser().getFirstName().concat(" ").concat(Utilities.ConnectedUser.getConnectedUser().getLastName()));
        userImage.setImage(new Image(Utilities.ConnectedUser.getConnectedUser().getImage()));

        // Inject telephone numbers for each instance in the database.
        retrieveGTelephones();
        retrieveSTelephones();

        // Back button functionality and hover effect.
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

    }
}
