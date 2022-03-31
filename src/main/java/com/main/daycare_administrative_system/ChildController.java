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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChildController implements Initializable {
    public static boolean refreshOnAdd;

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
                injectChild(
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

    public void injectChild(int id, String firstName, String lastName, String pic, String cpr, Date date, String gender, String guardianFirstName, String guardianLastName) {
        // Setting up HBox container for child instance
        AnchorPane innerContainer = new AnchorPane();
        innerContainer.setPrefHeight(100.0);
        innerContainer.setPrefWidth(1280.0);
        innerContainer.setMinWidth(1280.0);
        innerContainer.setStyle("-fx-border-style: solid; -fx-border-color:#b3b3b3;");

        // Setting up ImageView for child profile picture
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


        // Setting up Edit button
        Button edit = new Button();
        edit.setText("Edit");
        edit.setMinWidth(54.0);
        edit.setStyle("-fx-background-color: #7be3ad;");
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
                    ChildEditController.ChildToFields.getChildToFields().setChildImageURL(pic);
                } else {
                    ChildEditController.ChildToFields.getChildToFields().setChildImageURL("file:src/main/resources/com/main/daycare_administrative_system/assets/placeholder.png");
                }

                // Adding values to Singleton to pass onto the Edit popup
                    ChildEditController.ChildToFields.getChildToFields().setChildID(id);
                    ChildEditController.ChildToFields.getChildToFields().setChildFirstN(firstName);
                    ChildEditController.ChildToFields.getChildToFields().setChildLastN(lastName);
                    ChildEditController.ChildToFields.getChildToFields().setChildCPR(cpr);
                    ChildEditController.ChildToFields.getChildToFields().setChildDoB(date);
                    ChildEditController.ChildToFields.getChildToFields().setChildGender(gender);
                    Utilities.popUp("child_edit.fxml", "Edit Child");


                // Update child dynamically
                try {
                    connection();
                    preparedStatement = connect.prepareStatement("SELECT * FROM daycare.child WHERE child_ID = ?");
                    preparedStatement.setInt(1,ChildEditController.ChildToFields.getChildToFields().getChildID());
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

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        // Adding delete functionality
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Confirmation alert
                Alert alert0 = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "+ fullName.getText() + "?", ButtonType.YES, ButtonType.NO);
                alert0.setHeaderText("Please Confirm Child Deletion");
                alert0.setTitle("Child Deletion");

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
                // If user selects "YES"
                if (alert0.getResult() == ButtonType.YES) {
                    connection();
                    try {
                        // Execute SQL statement
                        preparedStatement = connect.prepareStatement("DELETE FROM daycare.child WHERE child_ID = ?");
                        preparedStatement.setInt(1, id);
                        preparedStatement.execute();

                        // Refresh the window
                        Stage stage = (Stage) delete.getScene().getWindow();
                        stage.close();
                        Utilities.changeScene(actionEvent,"child.fxml","Children menu",null,true, true,0,0);

                        // Display success alert
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setContentText("Child Removed!");
                        alert1.show();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Child not removed (SQL Error)");
                        alert.show();
                    }
                }
            }
        });

        innerContainer.getChildren().addAll(img, fullName, dob, c, g, guardian, edit, delete);

        // Adding margin to all elements
        img.setLayoutX(80.0);
        img.setLayoutY(20.0);

        fullName.setLayoutX(200.0);
        fullName.setLayoutY(40.0);

        dob.setLayoutX(450.0);
        dob.setLayoutY(42.5);

        c.setLayoutX(600.0);
        c.setLayoutY(42.5);

        g.setLayoutX(700.0);
        g.setLayoutY(42.5);

        guardian.setLayoutX(850.0);
        guardian.setLayoutY(42.5);

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
        retrieveChildren("ASC");

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
                    Utilities.popUp("child_add.fxml", "Add Child");

                    // Refresh window if child has been added
                    if (refreshOnAdd) {
                        Stage stage = (Stage) add.getScene().getWindow();
                        stage.close();
                        Utilities.changeScene(actionEvent, "child.fxml","Children Menu", null, true, true, 0,0);
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
                Utilities.changeScene(actionEvent, "child.fxml","Children Menu", null, true, true,0,0);
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
