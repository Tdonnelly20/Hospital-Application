package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.SanitationRequestDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SanitationRequestController extends MapController {
  @FXML private TextField hospitalID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomLocation;
  @FXML private JFXComboBox<Object> sanitationDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;
  private static SanitationRequestDao SanitationRequestDao = new SanitationRequestDao();

  private static class SingletonHelper {
    private static final SanitationRequestController controller = new SanitationRequestController();
  }

  public static SanitationRequestController getController() {
    return SanitationRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Sanitation Requests");
  }

  @FXML
  private void checkValidation() {
    if (!(patientID.getText().equals("")
        || hospitalID.getText().equals("")
        || firstName.getText().equals("")
        || lastName.getText().equals("")
        || roomLocation.getText().equals("")
        || requestDetails.getText().equals("")
        || sanitationDropDown.getValue().equals(""))) {
      sendRequest.setDisable(false);
    }
  }

  /** Determines if a medical delivery request is valid, and sends it to the Dao */
  @FXML
  private void validateButton() {

    // If any field is left blank, (except for request details) throw an error
    if (patientID.getText().equals("")
        || hospitalID.getText().equals("")
        || firstName.getText().equals("")
        || lastName.getText().equals("")
        || roomLocation.getText().equals("")
        || requestDetails.getText().equals("")
        || sanitationDropDown.getValue().equals("")) {

      // Set the text and color of the status label
      statusLabel.setText("All fields must be entered!");
      statusLabel.setTextFill(Color.web("Red"));

      // Make sure the patient ID is an integer
    } else if (!isInteger(patientID.getText())) {
      statusLabel.setText("Status: Failed. Patient ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {

      // Set the label to green, and let the user know it has been processed
      statusLabel.setText("Status: Processed Successfully");
      statusLabel.setTextFill(Color.web("Green"));

      // For testing purposes
      System.out.println(
          "\nPatient ID: "
              + patientID.getText()
              + "\nLocation: "
              + roomLocation.getText()
              + "\nName: "
              + firstName.getText()
              + "\nLast Name "
              + lastName.getText()
              + "\nMedication: "
              + sanitationDropDown.getValue()
              + "\n\nRequest Details: "
              + requestDetails.getText());

      resetFields(); // Set all fields to blank for another entry
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  private void resetFields() {
    patientID.setText("");
    hospitalID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomLocation.setText("");
    sanitationDropDown.setValue(null);
    requestDetails.setText("");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

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

  @FXML
  private void sendRequest() {
    // If any field is left blank, (except for request details) throw an error

    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(hospitalID.getText())) {
      statusLabel.setText("Status: Failed. Patient/Hospital ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {

      // Send the request to the Dao pattern

      // For testing purposes
      System.out.println(
          "\nHospital ID: "
              + hospitalID.getText()
              + "\nPatient ID: "
              + patientID.getText()
              + "\nRoom #: "
              + roomLocation.getText()
              + "\nName: "
              + firstName.getText()
              + " "
              + lastName.getText()
              + "\nMedication: "
              + sanitationDropDown.getValue()
              + "\n\nRequest Details: "
              + requestDetails.getText());

      resetFields(); // Set all fields to blank for another entry
    }
  }
}
