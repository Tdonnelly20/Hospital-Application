package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import java.io.IOException;
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
  FXMLLoader loader = new FXMLLoader();

  /**
   * Determines if a String is an integer or not
   *
   * @param input is a string
   * @return true if the string is an integer, false if not
   */
  public boolean isInteger(String input) {
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
  public boolean isDouble(String input) {
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
  public void switchToHome(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(getClass().getClassLoader().getResource("FXML/home.fxml")));
    switchScene(event);
  }

  // Switches scene to the location database
  @FXML
  public void switchToLocationDB(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LocationDatabase.fxml"));
    root = loader.load();
    LocationController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    controller.setElements();
    controller.resetPage();
    // filterCheckBox.getCheckModel().clearChecks();
    // filterCheckBox.getCheckModel().check("Location");
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  public void switchToServiceRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/serviceRequest.fxml"));
    root = loader.load();
    ServiceRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
    // filterCheckBox.getCheckModel().clearChecks();
    // filterCheckBox.getCheckModel().check("Request");
  }

  // Switches scene to the lab request page
  @FXML
  public void switchToLabRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LabRequest.fxml"));
    root = loader.load();
    LabRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the medicine delivery page
  @FXML
  public void switchToMedicineDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml"));
    root = loader.load();
    MedicineDeliveryController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the medical equipment page
  @FXML
  public void switchToMedEquipDelivery(ActionEvent event) throws IOException {
    loader.setLocation(
        getClass().getClassLoader().getResource("FXML/MedicalEquipmentDelivery.fxml"));
    root = loader.load();
    EquipmentRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the sanitation page
  @FXML
  public void switchToSanitationRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/SanitationRequest.fxml"));
    root = loader.load();
    SanitationRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the meal delivery page
  @FXML
  public void switchToMealDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MealDelivery.fxml"));
    root = loader.load();
    MealDeliveryRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the laundry request page
  @FXML
  public void switchToLaundryRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml"));
    root = loader.load();
    LaundryRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToReligiousRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml"));
    root = loader.load();
    ReligiousRequestController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToInternalPatientTransport(ActionEvent event) throws IOException {

    loader.setLocation(
        getClass().getClassLoader().getResource("FXML/InternalPatientTransportation.fxml"));
    root = loader.load();
    InternalPatientTransportationController controller = loader.getController();
    controller.init();
    controller.populateFloorIconArr();
    switchScene(event);
  }

  // Switches scene to the rootW
  @FXML
  public void switchScene(ActionEvent event) {
    MapManager.getManager().closePopUp();

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
