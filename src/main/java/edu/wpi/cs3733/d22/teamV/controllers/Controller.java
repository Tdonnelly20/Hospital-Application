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
  MapController controller;
  FXMLLoader loader = new FXMLLoader();
  String filterString = "";

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
    MapManager.getManager().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // Switches scene to the location database
  @FXML
  public void switchToLocationDB(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LocationDatabase.fxml"));
    filterString = "Locations";
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  public void switchToServiceRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/serviceRequest.fxml"));
    filterString = "Service Requests";
    switchScene(event);
  }

  // Switches scene to the lab request page
  @FXML
  public void switchToLabRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LabRequest.fxml"));
    filterString = "Lab Requests";
    switchScene(event);
  }

  // Switches scene to the medicine delivery page
  @FXML
  public void switchToMedicineDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml"));
    filterString = "Medicine Delivery";
    switchScene(event);
  }

  // Switches scene to the medical equipment page
  @FXML
  public void switchToMedEquipDelivery(ActionEvent event) throws IOException {
    loader.setLocation(
        getClass().getClassLoader().getResource("FXML/MedicalEquipmentDelivery.fxml"));
    filterString = "Equipment Delivery";
    switchScene(event);
  }

  // Switches scene to the sanitation page
  @FXML
  public void switchToSanitationRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/SanitationRequest.fxml"));
    filterString = "Sanitation Requests";
    switchScene(event);
  }

  // Switches scene to the meal delivery page
  @FXML
  public void switchToMealDelivery(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/MealDelivery.fxml"));
    filterString = "Meal Delivery";
    switchScene(event);
  }

  // Switches scene to the laundry request page
  @FXML
  public void switchToLaundryRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml"));
    filterString = "Laundry Requests";
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToReligiousRequest(ActionEvent event) throws IOException {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml"));
    filterString = "Religious Requests";
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToInternalPatientTransport(ActionEvent event) throws IOException {
    loader.setLocation(
        getClass().getClassLoader().getResource("FXML/InternalPatientTransportation.fxml"));
    filterString = "Internal Patient Transportation Requests";
    switchScene(event);
  }

  // Switches scene to the rootW
  @FXML
  public void switchScene(ActionEvent event) {
    try {
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    controller = loader.getController();
    controller.filterCheckBox.getCheckModel().clearChecks();
    controller.filterCheckBox.getCheckModel().check(filterString);
    controller.init();
    controller.populateFloorIconArr();

    MapManager.getManager().closePopUp();
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
