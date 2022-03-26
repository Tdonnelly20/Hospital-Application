package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LabRequestController extends Controller {
  @FXML private TextField Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private JFXComboBox dropDown;
  @FXML private Button sendRequest;
  @FXML private Button Reset;

  @FXML
  public void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    dropDown.setValue(null);
    sendRequest.setDisable(true);
  }

  @FXML
  private void validateButton() {
    System.out.println(dropDown.getValue());
    if (!(userID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(dropDown.getValue() == null)) {
      // Information verification and submission needed
      sendRequest.setDisable(false);
      Status.setText("Status: Done");
    } else if (!(userID.getText().isEmpty())
        || !(patientID.getText().isEmpty())
        || !(firstName.getText().isEmpty())
        || !(lastName.getText().isEmpty())
        || !(dropDown.getValue() == null)) {
      Status.setText("Status: Processing");
    } else {
      Status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }
}
