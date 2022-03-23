package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class Utilities {
    private static PreparedStatement preparedStatement = null;
    private static PreparedStatement pInsert = null;
    private static PreparedStatement pCheckUserExists = null;
    private static ResultSet resultSet = null;
    private static Connection connect = null;
    private static final String url = "jdbc:mysql://localhost:3306/daycare";
    private static final  String user = "root";
    private static final String pass = "pass";
    private static String query;
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
        if (pCheckUserExists != null) {
            try {
                pCheckUserExists.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pInsert != null) {
            try {
                pInsert.close();
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


    /** Inner class that makes use of a Singleton pattern to store information about the user that is logged in at a given time.
    * The information can then be utilized to dynamically customize the GUI anywhere */
    public static class ConnectedUser {
        private static ConnectedUser connectedUser = new ConnectedUser();
        private String userName, firstName, lastName, image;
        private Boolean admin;

        private ConnectedUser() {}

        private ConnectedUser(String userName, String firstName, String lastName, Boolean admin, String image) {
            this.userName = userName; this.firstName = firstName; this.lastName = lastName; this.admin = admin; this.image = image;
        }

        public static ConnectedUser getConnectedUser() {
            return connectedUser;
        }

        public static void loadUserDetails(String userName) {
            connectedUser = ConnectedUser.getConnectedUser();
            connection();
            try {
                preparedStatement = connect.prepareStatement("SELECT first_name, last_name, image, admin FROM daycare.staff INNER JOIN daycare.user USING (user_ID) WHERE user_name = ?");
                preparedStatement.setString(1, userName);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                connectedUser.setUserName(userName);
                connectedUser.setFirstName(resultSet.getString("first_name"));
                connectedUser.setLastName(resultSet.getString("last_name"));
                connectedUser.setAdmin(resultSet.getBoolean("admin"));
                connectedUser.setImage(resultSet.getString("image"));
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                closeConnection();
            }
        }

        public static void unloadUserDetails() {
            connectedUser.setUserName("");
            connectedUser.setFirstName("");
            connectedUser.setLastName("");
            connectedUser.setAdmin(false);
            connectedUser.setImage("");
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Boolean getAdmin() {
            return admin;
        }

        public void setAdmin(Boolean admin) {
            this.admin = admin;
        }
    }



    public static void changeScene(Event event, String fxmlFile, String title, String username, Boolean resizable, int width, int height) {
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(Utilities.class.getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(Objects.requireNonNull(Utilities.class.getResource(fxmlFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.setResizable(resizable);
        stage.show();
    }

    public static void popUp(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(Utilities.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Child");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean addChild(String image, String firstName, String lastName, String CPR, Date date, String gender) {
        try {
            connection();
            preparedStatement = connect.prepareStatement("INSERT INTO daycare.child (image, first_name, last_name, cpr, date_of_birth, gender) VALUES (?,?,?,?,?,?);");
            preparedStatement.setString(1, image);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, CPR);
            preparedStatement.setDate(5, date);
            preparedStatement.setString(6, gender);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addStaff(String image, String firstName, String lastName, String CPR, Date date, String gender, String userName, String userPass, Boolean admin, int userID) {
        try {
            connection();
            preparedStatement = connect.prepareStatement("INSERT INTO daycare.child (image, first_name, last_name, cpr, date_of_birth, gender) VALUES (?,?,?,?,?,?);");
            preparedStatement.setString(1, image);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, CPR);
            preparedStatement.setDate(5, date);
            preparedStatement.setString(6, gender);
            preparedStatement.execute();

            preparedStatement = connect.prepareStatement("INSERT INTO daycare.user (user_ID, user_name, password, admin) VALUES (?,?,?,?)");
            preparedStatement.setInt(1,userID);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, userPass);
            preparedStatement.setBoolean(4, admin);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean editChild(int id, String img, String firstName, String lastName, String CPR, Date date, String gender) {

        try {
            connection();
            preparedStatement = connect.prepareStatement("UPDATE daycare.child " +
                    "SET image = ?, first_name = ?, last_name = ?, cpr = ?, date_of_birth = ?, gender = ?" +
                    "WHERE child_ID = ?;");
            preparedStatement.setString(1, img);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, CPR);
            preparedStatement.setDate(5, date);
            preparedStatement.setString(6, gender);
            preparedStatement.setInt(7, id);
            preparedStatement.execute();
            closeConnection();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }

    public static boolean editStaff(int staffId, int userId, String userName, String userPass, boolean admin, String img, String firstName, String lastName, String CPR, Date date, String gender) {

        try {
            connection();
            preparedStatement = connect.prepareStatement("UPDATE daycare.staff, daycare.user LEFT JOIN daycare.staff ON staff.user_ID = user.user_ID " +
                    "SET user_name = ?, password = ?, admin = ?, image = ?, first_name = ?, last_name = ?, cpr = ?, date_of_birth = ?, gender = ?" +
                    "WHERE user_ID = ?;");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPass);
            preparedStatement.setBoolean(3,admin);
            preparedStatement.setString(4, img);
            preparedStatement.setString(5, firstName);
            preparedStatement.setString(6, lastName);
            preparedStatement.setString(7, CPR);
            preparedStatement.setDate(8, date);
            preparedStatement.setString(9, gender);
            preparedStatement.setInt(10, userId);
            preparedStatement.execute();
            closeConnection();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }

    public static void loginUser(ActionEvent event, String userName, String password) {
        try {
            connection();
            preparedStatement = connect.prepareStatement("SELECT password FROM daycare.user WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User not found");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {
                        ConnectedUser.loadUserDetails(userName);
                        changeScene(event, "main.fxml", "Daycare Centre Administrative System", userName, true,1200,800);
                        break;
                    } else {
                        System.out.println("Passwords didn't match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect password");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

}
