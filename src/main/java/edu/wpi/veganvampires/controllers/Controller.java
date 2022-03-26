package edu.wpi.veganvampires.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller {
  private Stage stage;
  private Scene scene;
  private Parent root;

  @FXML
  public void switchToHome(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/home.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToDefault(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/serviceRequest.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToLabRequest(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/LabRequest.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToMedicineDelivery(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToMedEquipDelivery(ActionEvent event) throws IOException {
    root =
        FXMLLoader.load(
            getClass().getClassLoader().getResource("FXML/MedicalEquipmentDelivery.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToSanitationRequests(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/SanitationRequests.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToMealDelivery(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/MealDelivery.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToLaundryRequest(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/LaundryRequest.fxml"));
    switchScene(event);
  }

  @FXML
  public void switchToReligiousRequest(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/ReligiousRequest.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  void switchScene(ActionEvent event) {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
