package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static Connection connect = null;
    private static final String url = "jdbc:mysql://localhost:3306/daycare";
    private static final  String user = "root";
    private static final String pass = "pass";

    @FXML
    private ImageView userImage;
    @FXML
    private Label userName;
    @FXML
    private VBox container;
    @FXML
    private Button back;
    @FXML
    private Button add;
    @FXML
    private Button refresh;

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

    public void retrieveStaff() {
        connection();

        try {
            preparedStatement = connect.prepareStatement("SELECT " +
                    "staff.staff_ID," +
                    "staff.first_name, " +
                    "staff.last_name, " +
                    "staff.image, " +
                    "staff.cpr, " +
                    "staff.date_of_birth, " +
                    "staff.gender, " +
                    "user.user_name, " +
                    "user.user_ID, " +
                    "user.password, " +
                    "user.admin " +
                    "FROM daycare.staff " +
                    "LEFT OUTER JOIN daycare.user " +
                    "USING (user_ID) ");

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                injectStaff(
                        resultSet.getInt("staff_ID"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("image"),
                        resultSet.getString("cpr"),
                        resultSet.getDate("date_of_birth"),
                        resultSet.getString("gender"),
                        resultSet.getString("user_name"),
                        resultSet.getInt("user_ID"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("admin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // UNFINISHED
    public void injectStaff(int id, String firstName, String lastName, String pic, String cpr, Date date, String gender, String user_name, int user_ID, String password, Boolean admin) {
        // Setting up HBox container for staff instance
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPrefHeight(100.0);
        hbox.setPrefWidth(1158.0);
        hbox.setStyle("-fx-border-style: solid; -fx-border-color:#b3b3b3;");

        // Setting up ImageView for staff profile picture
        ImageView img = new ImageView();
        img.setFitHeight(64);
        img.setFitWidth(64);
        img.setPickOnBounds(true);
        img.setPreserveRatio(true);
        Image image;
        if (pic != null) {
            image = new Image(pic);
        } else {
            image = new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png");
        }
        img.setImage(image);


        // Setting up name label
        Label fullName = new Label();
        if (firstName != null && lastName != null) {
            fullName.setText(firstName.concat(" ").concat(lastName));
        } else {
            fullName.setText("Name Unknown");
        }
        fullName.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 20));
        fullName.setAlignment(Pos.CENTER);
        fullName.setMaxWidth(99999.0);
        HBox.setHgrow(fullName, Priority.ALWAYS);

        // Setting up date of birth label
        Label dob = new Label();
        if (date != null) {
            dob.setText(date.toString());
        } else {
            dob.setText("Date of Birth Unknown");
        }
        dob.setAlignment(Pos.CENTER);
        dob.setMaxWidth(99999.0);
        HBox.setHgrow(dob,Priority.ALWAYS);

        // Setting up CPR label
        Label c = new Label();
        if (cpr != null) {
            c.setText(cpr);
        } else {
            c.setText("CPR Unknown");
        }
        c.setAlignment(Pos.CENTER);
        c.setMaxWidth(99999.0);
        HBox.setHgrow(c,Priority.ALWAYS);

        // Setting up gender label
        Label g = new Label();
        if (gender != null) {
            g.setText(gender);
        } else {
            g.setText("Gender Unknown");
        }
        g.setAlignment(Pos.CENTER);
        g.setMaxWidth(99999.0);
        HBox.setHgrow(g,Priority.ALWAYS);

        // Setting up region to push options button to right edge
        Region r = new Region();
        r.prefHeight(200.0);
        HBox.setHgrow(r, Priority.ALWAYS);
        r.setMaxWidth(99999.0);

        // Setting up Edit button
        Button b = new Button();
        b.setText("Edit");
        b.setStyle("-fx-background-color: #7be3ad");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #4bc190;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #7be3ad;"));
        b.setMnemonicParsing(false);

        // Setting up Delete button
        Button bu = new Button();
        bu.setText("Delete");
        bu.setStyle("-fx-background-color: #7be3ad");
        bu.setOnMouseEntered(e -> bu.setStyle("-fx-background-color: red"));
        bu.setOnMouseExited(e -> bu.setStyle("-fx-background-color: #7be3ad;"));
        bu.setMnemonicParsing(false);

        // Adding edit functionality
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ChildEditController.ChildToFields.getChildToFields().setChildImageURL(pic);
                ChildEditController.ChildToFields.getChildToFields().setChildID(id);
                ChildEditController.ChildToFields.getChildToFields().setChildFirstN(firstName);
                ChildEditController.ChildToFields.getChildToFields().setChildLastN(lastName);
                ChildEditController.ChildToFields.getChildToFields().setChildCPR(cpr);
                ChildEditController.ChildToFields.getChildToFields().setChildDoB(date);
                ChildEditController.ChildToFields.getChildToFields().setChildGender(gender);
                Utilities.popUp("staff_edit.fxml");
            }
        });

        // Adding delete functionality
        bu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                connection();
                try {
                    // Execute SQL statement
                    preparedStatement = connect.prepareStatement("DELETE FROM daycare.child WHERE child_ID = ?");
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();

                    // Refresh the window
                    Stage stage = (Stage) bu.getScene().getWindow();
                    stage.close();
                    Utilities.changeScene(actionEvent,"child.fxml","Children menu",null,true,1200,800);

                    // Display success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Child Removed!");
                    alert.show();

                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Child not removed (SQL Error)");
                    alert.show();
                }
            }
        });

        // Adding margin to all elements
        hbox.getChildren().addAll(img, fullName, dob, c, g, r, b, bu);
        HBox.setMargin(img, new Insets(0,0,0,65.0));
        HBox.setMargin(fullName, new Insets(0,0,0,0));
        HBox.setMargin(dob, new Insets(0,0,0,0));
        HBox.setMargin(c, new Insets(0,0,0,0));
        HBox.setMargin(g, new Insets(0,0,0,0));
        HBox.setMargin(r, new Insets(0,0,0,0));
        HBox.setMargin(b, new Insets(0,20.0,0,0));
        HBox.setMargin(bu, new Insets(0,65.0,0,0));
        container.getChildren().add(hbox);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText(Utilities.ConnectedUser.getConnectedUser().getFirstName().concat(" ").concat(Utilities.ConnectedUser.getConnectedUser().getLastName()));
        userImage.setImage(new Image(Utilities.ConnectedUser.getConnectedUser().getImage()));
        retrieveStaff();

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Utilities.changeScene(actionEvent, "main.fxml","Daycare Centre Administrative System", null, true,1200,800);
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
                Utilities.popUp("staff_add.fxml");
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
                Utilities.changeScene(actionEvent, "staff.fxml","Staff Menu", null, true,1200,800);
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
