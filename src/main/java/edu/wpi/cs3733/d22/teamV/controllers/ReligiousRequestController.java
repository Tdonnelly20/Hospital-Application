package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReligiousRequestController extends RequestController {
  @FXML private TextField Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomNumber;
  @FXML private Button sendRequest;
  @FXML private TextField specialRequests;

  private static class SingletonHelper {
    private static final ReligiousRequestController controller = new ReligiousRequestController();
  }

  public static ReligiousRequestController getController() {
    return ReligiousRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    setTitleText("Religious Request Service");
    fillTopPane();
    mapSetUp();
    filterCheckBox.getCheckModel().check("Religious Requests Service");
  }

  @Override
  void updateTreeTable() {}

  @FXML
  void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomNumber.setText("");
    specialRequests.setText("");
    sendRequest.setDisable(true);
  }

  // Checks to see if the user can submit info
  @FXML
  void validateButton() {
    if (!(userID.getText().isEmpty())
        && !(userID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(roomNumber.getText().isEmpty())
        && !(specialRequests.getText().isEmpty())) {
      // Information verification and submission needed
      sendRequest.setDisable(false);
      Status.setText("Status: Done");
    } else if (!(userID.getText().isEmpty())
        || !(userID.getText().isEmpty())
        || !(patientID.getText().isEmpty())
        || !(firstName.getText().isEmpty())
        || !(lastName.getText().isEmpty())
        || !(roomNumber.getText().isEmpty())
        || !(specialRequests.getText().isEmpty())) {
      Status.setText("Status: Processing");
    } else {
      Status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }

  @Override
  public void start(Stage primaryStage) {}
}
