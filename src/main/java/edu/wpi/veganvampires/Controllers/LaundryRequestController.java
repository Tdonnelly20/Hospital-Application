package edu.wpi.veganvampires.Controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LaundryRequestController extends Controller {

  @FXML private TextField employeeID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomNumber;
  @FXML private TextField details;
  @FXML private TextField Status;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private Button sendRequest;

  @FXML
  private void resetForm() {
    Status.setText("Status: Blank");
    employeeID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomNumber.setText("");
    dropDown.setValue(null);
    sendRequest.setDisable(true);
  }

  // Checks to see if the user can submit info
  @FXML
  private void validateButton() {
    if ((!employeeID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(roomNumber.getText().isEmpty())) {
      // Information verification and submission needed
      sendRequest.setDisable(false);
      Status.setText("Status: Done");

    } else if ((employeeID.getText().isEmpty())
        || (patientID.getText().isEmpty())
        || (firstName.getText().isEmpty())
        || (lastName.getText().isEmpty())
        || (roomNumber.getText().isEmpty())) {
      sendRequest.setDisable(true);
      Status.setText("Status: Processing");

    } else {
      sendRequest.setDisable(true);
      Status.setText("Status: Blank");
    }
  }

  @Override
  public void start(Stage primaryStage) {}
}
