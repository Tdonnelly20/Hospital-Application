package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.icons.Icon;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.manager.MapManager;
import edu.wpi.veganvampires.objects.Floor;
import edu.wpi.veganvampires.objects.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import edu.wpi.veganvampires.objects.ServiceRequest;
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

  @FXML private Pane mapPane;
  @FXML private ImageView mapImage;

  @FXML
  private void checkDropDown() {
    MapManager manager;
    String url = floorDropDown.getValue().toString() + ".png";
    mapImage.setImage(new Image(url));
    System.out.println(floorDropDown.getValue());
    switch (floorDropDown.getValue().toString()) {
      case "Ground Floor":
      case "Lower Level 1":
      case "Lower Level 2":
      case "Floor 1":
      case "Floor 2":
      case "Floor 3":
    }
  }

  private String getFloor() {
    switch (floorDropDown.getValue().toString()) {
      case "Ground Floor":
        return "G";
      case "Lower Level 1":
        return "L1";
      case "Lower Level 2":
        return "L2";
      case "Floor 1":
        return "1";
      case "Floor 2":
        return "2";
      case "Floor 3":
        return "3";
    }
    return "";
  }

  @FXML
  private void populateFloor(ArrayList<ServiceRequest> request, ArrayList<String> requestTypes) {}

  @FXML
  public void populateFloorIconArr() {
    for (Location l : Vdb.locations) {
      if (l.getFloor().equals(getFloor())
          && !(l.getNodeType().equalsIgnoreCase("HALL")
              || l.getNodeType().equalsIgnoreCase("ELEV")
              || l.getNodeType().equalsIgnoreCase("REST")
              || l.getNodeType().equalsIgnoreCase("BATH")
              || l.getNodeType().equalsIgnoreCase("EXIT")
              || l.getNodeType().equalsIgnoreCase("RETL")
              || l.getNodeType().equalsIgnoreCase("STAI")
              || l.getNodeType().equalsIgnoreCase("SERV"))) {
        Icon temp = new Icon(l);
        mapPane.getChildren().addAll(temp.getImage(), temp.getRectangle());
        currentIconArr.add(temp);
      }
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
