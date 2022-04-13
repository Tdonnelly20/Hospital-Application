package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.SanitationRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.SanitationRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SanitationRequestController extends RequestController {
  @FXML private TextField hospitalID;
  @FXML private TextField patientID;
  @FXML private TextField roomLocation;
  @FXML private JFXComboBox<Object> sanitationDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;

  @FXML private TreeTableView<EquipmentDelivery> sanitationRequestTable;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> patientIDCol;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> firstNameCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> lastNameCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> roomLocationCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> hazardCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> requestDetailsCol;

  private static final SanitationRequestDao SanitationRequestDao =
      (SanitationRequestDao) Vdb.requestSystem.getDao(Dao.SanitationRequest);

  private static class SingletonHelper {
    private static final SanitationRequestController controller = new SanitationRequestController();
  }

  public static SanitationRequestController getController() {
    return SanitationRequestController.SingletonHelper.controller;
  }

  // private static SanitationRequest sanitationRequest;

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Sanitation Requests");
    setTitleText("Sanitation Request Service");
    fillTopPane();
  }

  @FXML
  private void checkValidation() {
    if (!(patientID.getText().equals("")
        || hospitalID.getText().equals("")
        || roomLocation.getText().equals("")
        || requestDetails.getText().equals("")
        || sanitationDropDown.getValue().equals(""))) {
      sendRequest.setDisable(false);
    }
  }

  /** Determines if a medical delivery request is valid, and sends it to the Dao */
  @FXML
  void validateButton() {

    // If any field is left blank, (except for request details) throw an error
    if (patientID.getText().equals("")
        || hospitalID.getText().equals("")
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
              + "\nHazard: "
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
  private void sendRequest() throws SQLException {

    SanitationRequest delivery =
        new SanitationRequest(
            Integer.parseInt(hospitalID.getText()),
            Integer.parseInt(patientID.getText()),
            roomLocation.getText(),
            sanitationDropDown.getValue().toString(),
            requestDetails.getText());

    updateTreeTable();
    resetFields(); // Set all fields to blank for another entry
  }

  @FXML
  void updateTreeTable() {
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    roomLocationCol.setCellValueFactory(new TreeItemPropertyValueFactory("locationName"));
    hazardCol.setCellValueFactory(new TreeItemPropertyValueFactory("hazard"));
    requestDetailsCol.setCellValueFactory(new TreeItemPropertyValueFactory("notes"));

    ArrayList<SanitationRequest> currSanitationRequests =
        (ArrayList<SanitationRequest>)
            RequestSystem.getSystem().getAllServiceRequests(Dao.SanitationRequest);

    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (currSanitationRequests.isEmpty()) {
      sanitationRequestTable.setRoot(null);
    } else {
      for (SanitationRequest delivery : currSanitationRequests) {
        TreeItem<SanitationRequest> item = new TreeItem(delivery);
        treeItems.add(item);
      }
      sanitationRequestTable.setShowRoot(false);
      TreeItem root = new TreeItem(currSanitationRequests.get(0));
      sanitationRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  void resetForm() {
    return;
  }
}
