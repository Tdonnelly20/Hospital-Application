package edu.wpi.cs3733.d22.teamV.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller extends Application {
  private Parent root;
  MapController controller;
  FXMLLoader loader = new FXMLLoader();
  String filterString = "";

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
   * Determines if a String is an double or not
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
  protected void switchToHome(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // Switches scene to the location database
  @FXML
  protected void switchToLocationDB(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LocationDatabase.fxml"));
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  protected void switchToServiceRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/serviceRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the lab request page
  @FXML
  protected void switchToLabRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LabRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the medicine delivery page
  @FXML
  protected void switchToMedicineDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml"));
    switchScene(event);
  }

  // Switches scene to the medical equipment page
  @FXML
  protected void switchToMedEquipDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/NewEquipmentDelivery.fxml"));
    switchScene(event);
  }

  // Switches scene to the sanitation page
  @FXML
  protected void switchToSanitationRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/SanitationRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the meal delivery page
  @FXML
  protected void switchToMealDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/NewMealDelivery.fxml"));
    switchScene(event);
  }

  // Switches scene to the laundry request page
  @FXML
  protected void switchToLaundryRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  protected void switchToReligiousRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml"));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  protected void switchToInternalPatientTransport(ActionEvent event) throws IOException {
    loader.setLocation(
        getClass().getClassLoader().getResource("FXML/InternalPatientTransportation.fxml"));
    filterString = "Internal Patient Transportation Requests";
    switchScene(event);
  }

  @FXML
  protected void switchToEmployeeDB(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/EmployeeDatabase.fxml"));
    switchScene(event);
  }

  @FXML
  protected void switchToPatientDB(ActionEvent event) throws IOException {
    URL fxmlLocation = getClass().getClassLoader().getResource("FXML/PatientDatabase.fxml");
    loader.setLocation(fxmlLocation);
    switchScene(event);
  }

  // Switches scene to the rootW
  @FXML
  protected void switchScene(ActionEvent event) {
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    controller = loader.getController();
    controller.filterCheckBox.getCheckModel().clearChecks();
    controller.init();
    controller.populateFloorIconArr();

    PopupController.getController().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
