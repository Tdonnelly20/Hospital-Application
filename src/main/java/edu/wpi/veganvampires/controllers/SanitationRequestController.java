package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.dao.SanitationRequestDao;
import edu.wpi.veganvampires.interfaces.RequestInterface;
import edu.wpi.veganvampires.objects.SanitationRequest;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SanitationRequestController extends Controller implements RequestInterface {

  @FXML private TreeTableView<SanitationRequest> sanitationRequestTable;
  @FXML private TreeTableColumn<SanitationRequest, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<SanitationRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<SanitationRequest, String> firstNameCol;
  @FXML private TreeTableColumn<SanitationRequest, String> lastNameCol;
  @FXML private TreeTableColumn<SanitationRequest, String> roomLocationCol;
  @FXML private TreeTableColumn<SanitationRequest, String> hazardCol;
  @FXML private TreeTableColumn<SanitationRequest, String> requestDetailsCol;

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

  @Override
  public void updateTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("hospitalID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    roomLocationCol.setCellValueFactory(new TreeItemPropertyValueFactory("roomLocation"));
    hazardCol.setCellValueFactory(new TreeItemPropertyValueFactory("hazardName"));
    requestDetailsCol.setCellValueFactory(new TreeItemPropertyValueFactory("requestDetails"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<SanitationRequest> currSanitationRequests =
        (ArrayList<SanitationRequest>) SanitationRequestDao.getAllSanitationRequests();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (!currSanitationRequests.isEmpty()) {

      // for each loop cycling through each medicine delivery currently entered into the system
      for (SanitationRequest delivery : currSanitationRequests) {
        TreeItem<SanitationRequest> item = new TreeItem(delivery);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      sanitationRequestTable.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(SanitationRequestDao.getAllSanitationRequests().get(0));
      // Set the root in the table
      sanitationRequestTable.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
    }
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
  @Override
  public void validateButton() {

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

      // Send the request to the Dao pattern
      SanitationRequestDao.addSanitationRequest(
          firstName.getText(),
          lastName.getText(),
          Integer.parseInt(patientID.getText()),
          Integer.parseInt(hospitalID.getText()),
          roomLocation.getText(),
          sanitationDropDown.getValue().toString(),
          requestDetails.getText());

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

      resetForm(); // Set all fields to blank for another entry
    }
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
  @Override
  public void sendRequest() {
    // If any field is left blank, (except for request details) throw an error

    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(hospitalID.getText())) {
      statusLabel.setText("Status: Failed. Patient/Hospital ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {

      // Send the request to the Dao pattern
      SanitationRequestDao.addSanitationRequest(
          firstName.getText(),
          lastName.getText(),
          Integer.parseInt(patientID.getText()),
          Integer.parseInt(hospitalID.getText()),
          roomLocation.getText(),
          sanitationDropDown.getValue().toString(),
          requestDetails.getText());

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

      resetForm(); // Set all fields to blank for another entry
    }
  }
  /** Sets all the fields to their default value for another entry */
  @FXML
  public void resetForm() {
    patientID.setText("");
    hospitalID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomLocation.setText("");
    sanitationDropDown.setValue(null);
    requestDetails.setText("");
  }
}
