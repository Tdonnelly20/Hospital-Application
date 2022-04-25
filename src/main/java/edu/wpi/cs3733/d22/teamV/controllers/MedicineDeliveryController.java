package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.dao.MedicineDeliveryDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import java.awt.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MedicineDeliveryController extends RequestController {

  @FXML private TreeTableView<MedicineDelivery> medicineDeliveryTable;
  @FXML private Pane tablePlane;

  @FXML private TreeTableColumn<MedicineDelivery, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<MedicineDelivery, Integer> patientIDCol;
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
  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();

  private static class SingletonHelper {
    private static final MedicineDeliveryController controller = new MedicineDeliveryController();
  }

  public static MedicineDeliveryController getController() {
    return MedicineDeliveryController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    setTitleText("Medicine Delivery Request");
    fillTopPane();

    updateTreeTable();

    setColumnSizes(910);

    tablePlane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePlane.getWidth();
                medicineDeliveryTable.setPrefWidth(w - 30);
                setColumnSizes(w);
              }
            });

    tablePlane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = tablePlane.getHeight();
                medicineDeliveryTable.setPrefHeight(h - 75);
              }
            });
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(patientIDCol, (w - 30) / 9);
    setColumnSize(hospitalIDCol, (w - 30) / 9);
    setColumnSize(firstNameCol, (w - 30) / 9);
    setColumnSize(lastNameCol, (w - 30) / 9);
    setColumnSize(nodeIDCol, (w - 30) / 9);
    setColumnSize(medicineCol, (w - 30) / 9);
    setColumnSize(dosageCol, (w - 30) / 9);
    setColumnSize(otherInfoCol, (w - 30) / 9);
    setColumnSize(statusCol, (w - 30) / 9);
  }

  /** Update the table with values from fields and the DB */
  @Override
  public void updateTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientLastName"));
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
    medicineCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("medicineName"));
    dosageCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("dosage"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    otherInfoCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("requestDetails"));
    // Get the current list of medicine deliveries from the DAO
    ArrayList<MedicineDelivery> currMedicineDeliveries =
        (ArrayList<MedicineDelivery>) medicineDeliveryDao.getAllServiceRequests();
    // Create a list for our tree items
    ArrayList<TreeItem<MedicineDelivery>> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currMedicineDeliveries.isEmpty()) {
      medicineDeliveryTable.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (MedicineDelivery delivery : currMedicineDeliveries) {
      TreeItem<MedicineDelivery> item = new TreeItem<>(delivery);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    medicineDeliveryTable.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem<MedicineDelivery> root = new TreeItem<>(currMedicineDeliveries.get(0));
    // Set the root in the table
    medicineDeliveryTable.setRoot(root);
    // Set the rest of the tree items to the root, including the one we set as the root
    root.getChildren().addAll(treeItems);
  }

  boolean findPatient() { // returns true if finds patient
    boolean result = false;
    if (!patientID.getText().isEmpty() && isInteger(patientID.getText())) {
      for (Patient p : Vdb.requestSystem.getPatients()) {
        if (p.getPatientID() == Integer.parseInt(patientID.getText())) {
          result = true;
          break;
        }
      }
    }
    return result;
  }

  boolean findEmployee() { // returns true if finds patient
    boolean result = false;
    if (!employeeID.getText().isEmpty() && isInteger(employeeID.getText())) {
      for (Employee e : Vdb.requestSystem.getEmployees()) {
        if (e.getEmployeeID() == Integer.parseInt(employeeID.getText())) {
          result = true;
          break;
        }
      }
    }
    return result;
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
      statusLabel.setTextFill(Color.web("Black"));
      sendRequest.setDisable(true);
      if ((employeeID.getText().equals("")
          && patientID.getText().equals("")
          && nodeID.getText().equals("")
          && dosage.getText().equals("")
          && statusDropDown.getValue().equals("Status")
          && medicationDropDown.getValue().equals("Select Medication"))) {
        statusLabel.setText("Status: Blank");
      } else if ((employeeID.getText().equals("")
          || patientID.getText().equals("")
          || nodeID.getText().equals("")
          || dosage.getText().equals("")
          || statusDropDown.getValue().equals("Status")
          || medicationDropDown.getValue().equals("Select Medication"))) {
        statusLabel.setText("Status: Processing");
      } else if (LocationDao.getLocation(nodeID.getText()) == null) {
        statusLabel.setText("Status: Needs valid room");
      } else if (!findEmployee()) {
        statusLabel.setText("Status: Needs valid employee ID");
      } else if (!findPatient()) {
        statusLabel.setText("Status: Needs valid patient ID");
      } else {
        statusLabel.setText("Status: Good");
        sendRequest.setDisable(false);
      }
    } catch (Exception e) {
      sendRequest.setDisable(true);
    }
  }

  /** Determines if a medical delivery request is valid, and sends it to the Dao */
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
              requestDetails.getText(),
              statusDropDown.getValue().toString(),
              -1,
              Timestamp.from(Instant.now()).toString());
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
    validateButton();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  private void updateSelectedRow() throws NullPointerException {
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
  private void removeSelectedRow() throws NullPointerException {
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
