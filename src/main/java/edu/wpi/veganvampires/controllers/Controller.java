package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Floor;
import edu.wpi.veganvampires.objects.Icon;
import edu.wpi.veganvampires.objects.ServiceRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class Controller extends Application {
  private Parent root;
  ArrayList<Icon> currentIconArr;
  private Floor currFloor;

  @FXML private Pane mapPane;
  @FXML private ImageView mapImage;

  @FXML
  private JFXComboBox floorDropDown =
      new JFXComboBox<>(
          FXCollections.observableArrayList(
              "Ground Floor",
              "Lower Level 2",
              "Lower Level 1",
              "1st Floor",
              "2nd Floor",
              "3rd Floor"));

  /** Checks the value of the floor drop down and matches it with the corresponding map png */
  @FXML
  private void checkDropDown() {
    String url = floorDropDown.getValue().toString() + ".png";
    mapImage.setImage(new Image(url));
    System.out.println(floorDropDown.getValue());
    getFloor();
  }

  private void getFloor() {
    switch (floorDropDown.getValue().toString()) {
      case "Ground Floor":
        currFloor = Vdb.mapManager.getFloor("G");
        break;
      case "Lower Level 1":
        currFloor = Vdb.mapManager.getFloor("L1");
        break;
      case "Lower Level 2":
        currFloor = Vdb.mapManager.getFloor("L2");
        break;
      case "1st Floor":
        currFloor = Vdb.mapManager.getFloor("1");
        break;
      case "2nd Floor":
        currFloor = Vdb.mapManager.getFloor("2");
        break;
      case "3rd Floor":
        currFloor = Vdb.mapManager.getFloor("3");
        break;
    }
    populateFloorIconArr();
  }

  @FXML
  public void populateFloorIconArr() {
    mapPane.getChildren().clear();
    for (Icon icon : currFloor.getIconList()) {
      mapPane.getChildren().add(icon.getImage());
    }
  }

  private boolean hasIcon(ServiceRequest request) {
    for (Icon icon : currentIconArr) {
      if (icon.getRequestsArr().contains(request)) {
        return true;
      }
    }
    return false;
  }

  private Icon getIcon(ServiceRequest request) {
    for (Icon icon : currentIconArr) {
      if (icon.getRequestsArr().contains(request)) {
        return icon;
      }
    }
    return null;
  }
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
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("FXML/LocationDB.fxml"));
    root = loader.load();
    LocationController lc = loader.getController();
    lc.setElements();
    lc.resetPage();
    switchScene(event);
  }

  // Switches scene to the service request page
  @FXML
  public void switchToServiceRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/serviceRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the lab request page
  @FXML
  public void switchToLabRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/LabRequest.fxml")));
    switchScene(event);
    checkDropDown();
  }

  // Switches scene to the medicine delivery page
  @FXML
  public void switchToMedicineDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml")));
    switchScene(event);
  }

  // Switches scene to the medical equipment page
  @FXML
  public void switchToMedEquipDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/MedicalEquipmentDelivery.fxml")));
    switchScene(event);
  }

  // Switches scene to the sanitation page
  @FXML
  public void switchToSanitationRequests(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/SanitationRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the meal delivery page
  @FXML
  public void switchToMealDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/MealDelivery.fxml")));
    switchScene(event);
  }

  // Switches scene to the laundry request page
  @FXML
  public void switchToLaundryRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToReligiousRequest(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml")));
    switchScene(event);
  }

  // Switches scene to the religious request page
  @FXML
  public void switchToInternalPatientTransport(ActionEvent event) throws IOException {
    /*
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml")));
    switchScene(event);
    * */
  }

  // Switches scene to the rootW
  @FXML
  void switchScene(ActionEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
