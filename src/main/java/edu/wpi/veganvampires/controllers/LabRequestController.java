package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LabRequestController {
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private JFXComboBox dropDown;
  @FXML private Button sendRequest;

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
    root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("FXML/serviceRequest.fxml")));
    switchScene(event);
  }

  @FXML
  void switchScene(ActionEvent event) {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void validateButton() {
    System.out.println(dropDown.getValue());
    if (!(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(dropDown.getValue() == null)) {
      // Information verification needed
      sendRequest.setDisable(false);
    } else {
      sendRequest.setDisable(true);
    }
  }
}
