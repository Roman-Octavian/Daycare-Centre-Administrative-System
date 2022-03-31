package com.main.daycare_administrative_system;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class Utilities {
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static Connection connect = null;
    private static final String url = "jdbc:mysql://localhost:3306/daycare";
    private static final  String user = "root";
    private static final String pass = "pass";
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


    /** Inner class that makes use of a Singleton pattern to store information about the user that is logged in at a given time.
    * The information can then be utilized to dynamically customize the GUI anywhere */
    public static class ConnectedUser {
        private static ConnectedUser connectedUser = new ConnectedUser();
        private String userName, firstName, lastName, image;
        private Boolean admin;
        private int staff_ID;

        private ConnectedUser() {}


        public static ConnectedUser getConnectedUser() {
            return connectedUser;
        }

        public static void loadUserDetails(String userName) {
            connectedUser = ConnectedUser.getConnectedUser();
            connection();
            try {
                preparedStatement = connect.prepareStatement("SELECT first_name, last_name, image, admin, staff_ID FROM daycare.staff INNER JOIN daycare.user USING (staff_ID) WHERE user_name = ?");
                preparedStatement.setString(1, userName);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                connectedUser.setUserName(userName);
                connectedUser.setFirstName(resultSet.getString("first_name"));
                connectedUser.setLastName(resultSet.getString("last_name"));
                connectedUser.setAdmin(resultSet.getBoolean("admin"));
                connectedUser.setImage(resultSet.getString("image"));
                connectedUser.setStaff_ID(resultSet.getInt("staff_ID"));
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
            connectedUser.setStaff_ID(0);
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

        public int getStaff_ID() {
            return staff_ID;
        }

        public void setStaff_ID(int staff_ID) {
            this.staff_ID = staff_ID;
        }
    }



    public static void changeScene(Event event, String fxmlFile, String title, String username, Boolean resizable, Boolean maximized, int width, int height) {
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
        stage.setScene(new Scene(root));
        stage.setMaximized(maximized);
            if (maximized) {
                Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
                stage.setWidth(screenSize.getWidth());
                stage.setHeight(screenSize.getHeight());
            } else {
                stage.setWidth(width);
                stage.setHeight(height);
            }
        stage.setResizable(resizable);
        stage.centerOnScreen();
        stage.show();
    }

    public static void popUp(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Utilities.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();
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

    /**
     * Takes all the values from the "StaffAddController" and tries to insert them all into their respective tables.
     * First, we insert values into the staff table.
     * Then, we save the ID of this newly created staff in the "staffID" variable
     * Lastly, we add the respective user and telephone entities, if any, alongside the ID we stored previously.
     * @param firstName First name to be inserted into staff table
     * @param lastName Last name to be inserted into staff table
     * @param CPR CPR number to be inserted into staff table
     * @param date Date of birth to be inserted into staff table
     * @param gender Gender to be inserted into staff table
     * @param role Company role to be inserted into staff table
     * @param telephone Telephone number (if any) to be inserted into telephone table
     * @param userName Account username (if any) to be inserted into user table
     * @param userPass Account password (if any) to be inserted into user table
     * @param admin Account admin status (if any) to be inserted into user table
     * @return true if staff member is added successfully; false if something goes wrong
     */
    public static boolean addStaff(String image, String firstName, String lastName, String CPR, Date date, String gender, String role, String telephone, String userName, String userPass, Boolean admin) {
        try {
            connection();
                // Add data to staff table.
                preparedStatement = connect.prepareStatement("INSERT INTO daycare.staff (image, first_name, last_name, cpr, date_of_birth, gender, company_role) VALUES (?,?,?,?,?,?,?);");
                preparedStatement.setString(1, image);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, CPR);
                preparedStatement.setDate(5, date);
                preparedStatement.setString(6, gender);
                preparedStatement.setString(7, role);
                preparedStatement.execute();

                // Get the ID of the new staff and store it in a variable
                preparedStatement = connect.prepareStatement("SELECT LAST_INSERT_ID()");
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int staffID = resultSet.getInt(1);

                // Add data to user table if provided
                if (!userName.equals("") && !userPass.equals("")) {
                    preparedStatement = connect.prepareStatement("INSERT INTO daycare.user (user_name, password, admin, staff_ID) VALUES (?,?,?,?)");
                    preparedStatement.setString(1, userName);
                    preparedStatement.setString(2, userPass);
                    preparedStatement.setBoolean(3, admin);
                    preparedStatement.setInt(4, staffID);
                    preparedStatement.execute();
                }
                // Add data to telephone table if provided
                if (!telephone.equals("")) {
                    preparedStatement = connect.prepareStatement("INSERT INTO daycare.telephone (telephone_number, staff_ID) VALUES (?, ?)");
                    preparedStatement.setString(1, telephone);
                    preparedStatement.setInt(2, staffID);
                    preparedStatement.execute();
                }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addGuardian(String image, String firstName, String lastName, String CPR, Date date, String gender, String telephone, String childID) {
        try {
            connection();
            // Add data to guardian table.
            preparedStatement = connect.prepareStatement("INSERT INTO daycare.guardian (image, first_name, last_name, cpr, date_of_birth, gender, child_ID) VALUES (?,?,?,?,?,?,?);");
            preparedStatement.setString(1, image);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, CPR);
            preparedStatement.setDate(5, date);
            preparedStatement.setString(6, gender);
            preparedStatement.setString(7, childID);
            preparedStatement.execute();

            // Get the ID of the new guardian and store it in a variable
            preparedStatement = connect.prepareStatement("SELECT LAST_INSERT_ID()");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int guardianID = resultSet.getInt(1);

            // Add data to telephone table if provided
            if (!telephone.equals("")) {
                preparedStatement = connect.prepareStatement("INSERT INTO daycare.telephone (telephone_number, guardian_ID) VALUES (?, ?)");
                preparedStatement.setString(1, telephone);
                preparedStatement.setInt(2, guardianID);
                preparedStatement.execute();
            }
            return true;
        } catch (Exception e) {
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

            ChildEditController.ChildToFields.getChildToFields().setChildFirstN(firstName);
            ChildEditController.ChildToFields.getChildToFields().setChildLastN(lastName);
            ChildEditController.ChildToFields.getChildToFields().setChildCPR(CPR);
            ChildEditController.ChildToFields.getChildToFields().setChildGender(gender);
            ChildEditController.ChildToFields.getChildToFields().setChildDoB(date);
            ChildEditController.ChildToFields.getChildToFields().setChildImageURL(img);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }

    public static boolean editStaff(int staffId, String userName, String userPass, boolean admin, String img, String firstName, String lastName, String CPR, String telephone, String role, Date date, String gender) {

        try {
            connection();

            // Handle edge-case scenario where editing staff that wants user but has no previous one
            if (userName != null && userPass != null) {
                preparedStatement = connect.prepareStatement("SELECT * FROM daycare.user " +
                        "WHERE staff_ID = ?;");
                preparedStatement.setInt(1, staffId);
                resultSet = preparedStatement.executeQuery();
                // This will initialize the user entry in the database with a temporary value, otherwise there would be nothing to insert the new values into
                if (!resultSet.next()) {
                    preparedStatement = connect.prepareStatement("INSERT INTO daycare.user (user_name, password, admin, staff_ID) VALUES (?,?,?,?)");
                    preparedStatement.setString(1, "temp");
                    preparedStatement.setString(2, "temp");
                    preparedStatement.setBoolean(3, false);
                    preparedStatement.setInt(4, staffId);
                    preparedStatement.execute();
                }
            }

            // Same as previous conditional but for telephone
            if (telephone != null) {
                preparedStatement = connect.prepareStatement("SELECT * FROM daycare.telephone " +
                        "WHERE staff_ID = ?;");
                preparedStatement.setInt(1, staffId);
                resultSet = preparedStatement.executeQuery();
                // This will initialize the user entry in the database with a temporary value, otherwise there would be nothing to insert the new values into
                if (!resultSet.next()) {
                    preparedStatement = connect.prepareStatement("INSERT INTO daycare.telephone (telephone_number, staff_ID) VALUES (?,?)");
                    preparedStatement.setString(1, "temp");
                    preparedStatement.setInt(2, staffId);
                    preparedStatement.execute();
                }
            }

            preparedStatement = connect.prepareStatement("UPDATE daycare.staff " +
                    "LEFT JOIN daycare.user USING (staff_ID) " +
                    "LEFT JOIN daycare.telephone USING (staff_ID) " +
                    "SET user_name = ?, password = ?, admin = ?, image = ?, first_name = ?, last_name = ?, cpr = ?, telephone_number = ?, company_role = ?, date_of_birth = ?, gender = ?" +
                    "WHERE staff_ID = ?;");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPass);
            preparedStatement.setBoolean(3,admin);
            preparedStatement.setString(4, img);
            preparedStatement.setString(5, firstName);
            preparedStatement.setString(6, lastName);
            preparedStatement.setString(7, CPR);
            preparedStatement.setString(8, telephone);
            preparedStatement.setString(9, role);
            preparedStatement.setDate(10, date);
            preparedStatement.setString(11, gender);
            preparedStatement.setInt(12, staffId);
            preparedStatement.execute();
            closeConnection();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }

    public static boolean editGuardian(int guardianId, String img, String firstName, String lastName, String CPR, String telephone, Date date, String gender, int childId) {
        try {
            connection();

            // Handle null case
            if (telephone != null) {
                preparedStatement = connect.prepareStatement("SELECT * FROM daycare.telephone " +
                        "WHERE guardian_ID = ?;");
                preparedStatement.setInt(1, guardianId);
                resultSet = preparedStatement.executeQuery();
                // This will initialize the user entry in the database with a temporary value, otherwise there would be nothing to insert the new values into
                if (!resultSet.next()) {
                    preparedStatement = connect.prepareStatement("INSERT INTO daycare.telephone (telephone_number, guardian_ID) VALUES (?,?)");
                    preparedStatement.setString(1, "temp");
                    preparedStatement.setInt(2, guardianId);
                    preparedStatement.execute();
                }
            }

            preparedStatement = connect.prepareStatement("UPDATE daycare.guardian " +
                    "JOIN daycare.telephone USING (guardian_ID) " +
                    "SET image = ?, first_name = ?, last_name = ?, cpr = ?, telephone.telephone_number = ?, date_of_birth = ?, gender = ?, telephone.guardian_ID = ?, child_ID = ? " +
                    "WHERE guardian_ID = ?;");
            preparedStatement.setString(1, img);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, CPR);
            preparedStatement.setString(5, telephone);
            preparedStatement.setDate(6, date);
            preparedStatement.setString(7, gender);
            preparedStatement.setString(8, String.valueOf(guardianId));
            preparedStatement.setInt(9, childId);
            preparedStatement.setInt(10, guardianId);
            preparedStatement.execute();
            closeConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
            return false;
        }
    }

    public static void loginUser(ActionEvent event, String userName, String password) {
        if (!userName.equals("") && !password.equals("")) {
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
                            changeScene(event, "main.fxml", "Main Menu", userName, true, true,0,0);
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
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to insert your details!");
            alert.setTitle("Login");
            alert.setHeaderText("Operation Failed");
            Stage popStage = (Stage) alert.getDialogPane().getScene().getWindow();
            popStage.getIcons().add(new Image("file:src/main/resources/com/main/daycare_administrative_system/assets/icon64.png"));
            alert.showAndWait();
        }
    }
}
