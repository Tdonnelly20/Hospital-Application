package edu.wpi.veganvampires.Controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.Dao.MedicineDeliveryDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MedicineDeliveryController extends Controller {
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomNum;
  @FXML private TextField dosage;
  @FXML private JFXComboBox<Object> medicationDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;
  private static MedicineDeliveryDao medicineDeliveryDao = new MedicineDeliveryDao();

  @FXML
  private void validateButton() {
    if ((patientID.getText().equals("")
        && firstName.getText().equals("")
        && lastName.getText().equals("")
        && roomNum.getText().equals("")
        && dosage.getText().equals("")
        && medicationDropDown.getValue().equals(null))) {
      sendRequest.setDisable(false);
      statusLabel.setText("Status: Blank");
    } else if ((patientID.getText().equals("")
        || firstName.getText().equals("")
        || lastName.getText().equals("")
        || roomNum.getText().equals("")
        || dosage.getText().equals("")
        || medicationDropDown.getValue().equals(""))) {
      sendRequest.setDisable(false);
      statusLabel.setText("Status: Processing");
    } else {
      sendRequest.setDisable(true);
    }
  }

  /** Determines if a medical delivery request is valid, and sends it to the Dao */
  @FXML
  private void sendRequest() {
    // If any field is left blank, (except for request details) throw an error

    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText())) {
      statusLabel.setText("Status: Failed. Patient ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {

      // Send the request to the Dao pattern
      medicineDeliveryDao.addMedicationDelivery(
          firstName.getText(),
          lastName.getText(),
          Integer.parseInt(patientID.getText()),
          medicationDropDown.getValue().toString(),
          dosage.getText(),
          requestDetails.getText());

      // Set the label to green, and let the user know it has been processed
      statusLabel.setText("Status: Processed Successfully");
      statusLabel.setTextFill(Color.web("Green"));

      // For testing purposes
      System.out.println(
          "\nPatient ID: "
              + patientID.getText()
              + "\nRoom #: "
              + roomNum.getText()
              + "\nName: "
              + firstName.getText()
              + " "
              + lastName.getText()
              + "\nMedication: "
              + medicationDropDown.getValue()
              + "\nDosage: "
              + dosage.getText()
              + "\n\nRequest Details: "
              + requestDetails.getText());

      resetFields(); // Set all fields to blank for another entry
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  private void resetFields() {
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomNum.setText("");
    dosage.setText("");
    medicationDropDown.setValue(null);
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
}
