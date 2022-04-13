package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.MedicineDeliveryDao;
import edu.wpi.cs3733.d22.teamV.interfaces.RequestInterface;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
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

public class MedicineDeliveryController extends MapController implements RequestInterface {

  @FXML private TreeTableView<MedicineDelivery> medicineDeliveryTable;

  @FXML private TreeTableColumn<MedicineDelivery, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<MedicineDelivery, Integer> patientIDCol;
  @FXML private TreeTableColumn<MedicineDelivery, Integer> serviceIDCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> firstNameCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> lastNameCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> nodeIDCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> medicineCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> dosageCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> statusCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> otherInfoCol;

  @FXML private TextField patientID;
  @FXML private TextField employeeID;
  @FXML private TextField nodeID;
  @FXML private TextField dosage;
  @FXML private JFXComboBox<Object> medicationDropDown;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;

  // MUST take from Vdb, do NOT create
  private static final MedicineDeliveryDao medicineDeliveryDao =
      (MedicineDeliveryDao) Vdb.requestSystem.getDao(Dao.MedicineDelivery);
  private boolean updating = false;
  private int updateServiceID;

  private static class SingletonHelper {
    private static final MedicineDeliveryController controller = new MedicineDeliveryController();
  }

  public static MedicineDeliveryController getController() {
    return MedicineDeliveryController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Medicine Delivery Requests");
    filterCheckBox.getCheckModel().check("Equipment");
  }

  /** Update the table with values from fields and the DB */
  @Override
  public void updateTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    serviceIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("serviceID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));
    medicineCol.setCellValueFactory(new TreeItemPropertyValueFactory("medicineName"));
    dosageCol.setCellValueFactory(new TreeItemPropertyValueFactory("dosage"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory("status"));
    otherInfoCol.setCellValueFactory(new TreeItemPropertyValueFactory("requestDetails"));
    // Get the current list of medicine deliveries from the DAO
    ArrayList<MedicineDelivery> currMedicineDeliveries =
        (ArrayList<MedicineDelivery>) medicineDeliveryDao.getAllServiceRequests();
    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currMedicineDeliveries.isEmpty()) {
      medicineDeliveryTable.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (MedicineDelivery delivery : currMedicineDeliveries) {
      TreeItem<MedicineDelivery> item = new TreeItem(delivery);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    medicineDeliveryTable.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem root = new TreeItem(currMedicineDeliveries.get(0));
    // Set the root in the table
    medicineDeliveryTable.setRoot(root);
    // Set the rest of the tree items to the root, including the one we set as the root
    root.getChildren().addAll(treeItems);
  }

  /** Determine whether or not all fields have been filled out, so we can submit the info */
  @FXML
  public void validateButton() {
    if (updating) {
      sendRequest.setText("Update Request");
    } else {
      sendRequest.setText("Send Request");
    }

    try {
      if ((employeeID.getText().equals("")
          && patientID.getText().equals("")
          && nodeID.getText().equals("")
          && dosage.getText().equals("")
          && statusDropDown.getValue().equals("Status")
          && medicationDropDown.getValue().equals("Select Medication"))) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Blank");
        statusLabel.setTextFill(Color.web("Black"));
      } else if ((employeeID.getText().equals("")
          || patientID.getText().equals("")
          || nodeID.getText().equals("")
          || dosage.getText().equals("")
          || statusDropDown.getValue().equals("Status")
          || medicationDropDown.getValue().equals("Select Medication"))) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Processing");
        statusLabel.setTextFill(Color.web("Black"));
      } else {
        sendRequest.setDisable(false);
      }
    } catch (Exception e) {
      sendRequest.setDisable(true);
    }
  }

  /** Determines if a medical delivery request is valid, and sends it to the Dao */
  @Override
  public void sendRequest() {
    // If any field is left blank, (except for request details) throw an error

    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(employeeID.getText())) {
      statusLabel.setText("Patient/Employee ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {
      MedicineDelivery medicineDelivery =
          new MedicineDelivery(
              nodeID.getText(),
              Integer.parseInt(patientID.getText()),
              Integer.parseInt(employeeID.getText()),
              medicationDropDown.getValue().toString(),
              dosage.getText(),
              statusDropDown.getValue().toString(),
              requestDetails.getText());
      // Send the request to the Dao pattern
      try {
        if (updating) {
          medicineDeliveryDao.updateServiceRequest(medicineDelivery, updateServiceID);
          updating = false;
        } else {
          medicineDeliveryDao.addServiceRequest(medicineDelivery);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      resetForm(); // Set all fields to blank for another entry
      updateTreeTable();
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  public void resetForm() {
    updating = false;
    patientID.setText("");
    employeeID.setText("");
    nodeID.setText("");
    dosage.setText("");

    medicationDropDown.setValue("Select Medication");
    requestDetails.setText("");
    statusDropDown.setValue("Status");
    sendRequest.setDisable(true);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  // used to get coordinates after clicking map
  @FXML private TextArea coordinates;
  private Point point = new Point();
  private int xCoord, yCoord;

  @FXML
  private void mapCoordTracker() {
    point = MouseInfo.getPointerInfo().getLocation();
    xCoord = point.x - 712;
    yCoord = point.y - 230;
    coordinates.setText("X: " + xCoord + " Y: " + yCoord);
  }

  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    MedicineDelivery delivery =
        medicineDeliveryTable.getSelectionModel().getSelectedItem().getValue();

    employeeID.setText(String.valueOf(delivery.getEmployeeID()));
    patientID.setText(String.valueOf(delivery.getPatientID()));
    nodeID.setText(delivery.getLocation().getNodeID());
    dosage.setText(delivery.getDosage());
    medicationDropDown.setValue(delivery.getMedicineName());
    statusDropDown.setValue("Processing");
    requestDetails.setText(delivery.getRequestDetails());
    updateServiceID = delivery.getServiceID();
    updateTreeTable();
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      MedicineDelivery delivery =
          medicineDeliveryTable.getSelectionModel().getSelectedItem().getValue();
      medicineDeliveryDao.removeServiceRequest(delivery);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }
}
