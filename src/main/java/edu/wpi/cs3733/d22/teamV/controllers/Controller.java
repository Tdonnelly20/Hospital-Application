package edu.wpi.cs3733.d22.teamV.controllers;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller extends Application {
  private Parent root;
  FXMLLoader loader = new FXMLLoader();

  @Override
  public void init() {}
  /**
   * Determines if a String is an integer or not
   *
   * @param input is a string
   * @return true if the string is an integer, false if not
   */
  protected boolean isInteger(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Determines if a String is a double or not
   *
   * @param input is a string
   * @return true if the string is an double, false if not
   */
  protected boolean isDouble(String input) {
    try {
      Double.parseDouble(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  // Closes the program
  @Override
  public void stop() {
    System.out.println("Shutting Down");
    Platform.exit();
  }

  // Switches scene to the home page
  @FXML
  protected void switchToHome(Event event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/home.fxml"));
    switchScene(event);
  }

  // Switches scene to the map
  @FXML
  protected void switchToMap(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/Map.fxml"));
    switchScene(event);
  }

  // Switches scene to the location database
  @FXML
  protected void switchToLocationDB(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LocationDatabase.fxml"));
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  protected void switchToServiceRequest(Event event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/serviceRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to API landing page
  @FXML
  protected void switchToAPI(Event event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/APILandingPage.fxml"));
    switchScene(event);
  }

  // Switches scene to the lab request page
  @FXML
  protected void switchToLabRequest(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LabRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the medicine delivery page
  @FXML
  protected void switchToMedicineDelivery(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml"));
    switchScene(event);
  }

  // Switches scene to the medical equipment page
  @FXML
  protected void switchToMedEquipDelivery(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/EquipmentDelivery.fxml"));
    switchScene(event);
  }

  // Switches scene to the sanitation page
  @FXML
  protected void switchToSanitationRequest(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/SanitationRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the computer page
  @FXML
  protected void switchToComputerRequest(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/ComputerRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the meal delivery page
  @FXML
  protected void switchToMealDelivery(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MealDelivery.fxml"));
    switchScene(event);
  }

  // Switches scene to the laundry request page
  @FXML
  protected void switchToLaundryRequest(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  protected void switchToReligiousRequest(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  protected void switchToInternalPatientTransport(ActionEvent event) {
    loader.setLocation(
        getClass().getClassLoader().getResource("FXML/InternalPatientTransportation.fxml"));
    switchScene(event);
  }

  /** Switches to the robot request page */
  @FXML
  protected void switchToRobot(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/Robot.fxml"));
    switchScene(event);
  }

  /** Switches to the employee database page */
  @FXML
  protected void switchToEmployeeDB(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/EmployeeDatabase.fxml"));
    switchScene(event);
  }

  /** Switches to the patient database page */
  @FXML
  protected void switchToPatientDB(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/PatientDatabase.fxml"));
    switchScene(event);
  }

  /** Switches to the map dashboard page */
  @FXML
  protected void switchToDashboard(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MapDashboard.fxml"));
    switchScene(event);
  }

  /** Switches to the about page */
  @FXML
  protected void switchToAbout(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/About.fxml"));
    switchScene(event);
  }

  // Switches scene to the root
  @FXML
  protected void switchScene(Event event) {
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Controller controller = loader.getController();
    controller.init();

    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  protected void runOurAPI() throws IOException {
    Runtime runtime = Runtime.getRuntime();
    runtime.exec(" java -jar " + "C:\\Users\\jason\\Downloads\\SoftEngRobotAPI.jar");
  }

  @FXML
  protected void runEAPI() throws IOException {
    Runtime runtime = Runtime.getRuntime();
    runtime.exec(" java -jar " + returnPath() + "\\TeamE-API.jar");
  }

  @FXML
  protected void runZAPI() throws IOException {
    Runtime runtime = Runtime.getRuntime();
    runtime.exec(" java -jar " + returnPath() + "\\ExternalTransportAPI.jar");
  }

  public static String returnPath() {
    String currentPath = System.getProperty("user.dir");
    if (currentPath.contains("TeamVeganVampires")) {
      int position = currentPath.indexOf("TeamVeganVampires") + 65;
      if (currentPath.length() > position) {
        currentPath = currentPath.substring(0, position);
      }
      currentPath += "\\api";
      System.out.println(currentPath);
    }
    return currentPath;
  }
}
