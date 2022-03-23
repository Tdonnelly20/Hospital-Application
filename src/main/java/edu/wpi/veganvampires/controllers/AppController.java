package edu.wpi.veganvampires.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppController {

  private Stage stage;
  private Scene scene;
  private Parent root;

  @FXML
  public void switchToDefault(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/app.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void switchToLabRequest(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/LabRequest.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void switchToMedicineDelivery(ActionEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/MedicineDelivery.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  void sr3() {}

  @FXML
  void sr4() {}

  @FXML
  void sr5() {}

  @FXML
  void sr6() {}
}
