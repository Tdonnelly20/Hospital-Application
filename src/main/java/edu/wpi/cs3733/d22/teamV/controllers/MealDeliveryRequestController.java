package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.dao.MealRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.MealRequest;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
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

public class MealDeliveryRequestController extends RequestController {

  @FXML private TreeTableView<MealRequest> mealDeliveryTable;

  @FXML private TreeTableColumn<MealRequest, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<MealRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<MealRequest, String> firstNameCol;
  @FXML private TreeTableColumn<MealRequest, String> lastNameCol;
  @FXML private TreeTableColumn<MealRequest, String> nodeIDCol;
  @FXML private TreeTableColumn<MealRequest, String> mealCol;
  @FXML private TreeTableColumn<MealRequest, String> allergyCol;
  @FXML private TreeTableColumn<MealRequest, String> statusCol;
  @FXML private TreeTableColumn<MealRequest, String> otherInfoCol;
  @FXML private TreeTableColumn<MealRequest, String> timeStampCol;

  @FXML private TextField patientID;
  @FXML private TextField employeeID;
  @FXML private TextField nodeID;
  @FXML private TextField allergy;
  @FXML private JFXComboBox<Object> mealDropDown;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;
  @FXML private Pane tablePane;
  // MUST take from Vdb, do NOT create
  private static final MealRequestDao mealRequestDao =
      (MealRequestDao) Vdb.requestSystem.getDao(Dao.MealRequest);

  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();
  private boolean updating = false;
  private int updateServiceID;

  private static class SingletonHelper {
    private static final MealDeliveryRequestController controller =
        new MealDeliveryRequestController();
  }

  public static MealDeliveryRequestController getController() {
    return MealDeliveryRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    setTitleText("Meal Delivery");
    fillTopPane();
    updateTreeTable();
    tablePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePane.getWidth();
                mealDeliveryTable.setPrefWidth(w - 30);
                setColumnSizes(w);
              }
            });

    tablePane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = tablePane.getHeight();
                mealDeliveryTable.setPrefHeight(h - 75);
              }
            });
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
    mealCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("mealName"));
    allergyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("allergy"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    otherInfoCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("requestDetails"));
    timeStampCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("timeString"));
    // Get the current list of medicine deliveries from the DAO
    ArrayList<MealRequest> currMealDeliveries =
        (ArrayList<MealRequest>) mealRequestDao.getAllServiceRequests();
    // Create a list for our tree items
    ArrayList<TreeItem<MealRequest>> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currMealDeliveries.isEmpty()) {
      mealDeliveryTable.setRoot(null);
      return;
    }

    // for each loop cycling through each meal delivery currently entered into the system
    for (MealRequest delivery : currMealDeliveries) {
      TreeItem<MealRequest> item = new TreeItem<>(delivery);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    mealDeliveryTable.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem<MealRequest> root = new TreeItem<>(currMealDeliveries.get(0));
    // Set the root in the table
    mealDeliveryTable.setRoot(root);
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
    statusLabel.setTextFill(Color.web("Black"));
    sendRequest.setDisable(true);
    try {
      if ((employeeID.getText().equals("")
          && patientID.getText().equals("")
          && nodeID.getText().equals("")
          && allergy.getText().equals("")
          && statusDropDown.getValue().equals("Status")
          && mealDropDown.getValue().equals("Select Meal"))) {
        statusLabel.setText("Status: Blank");
      } else if ((employeeID.getText().equals("")
          || patientID.getText().equals("")
          || nodeID.getText().equals("")
          || allergy.getText().equals("")
          || statusDropDown.getValue().equals("Status")
          || mealDropDown.getValue().equals("Select Meal"))) {
        statusLabel.setText("Status: Processing");
      } else if (LocationDao.getLocation(nodeID.getText()) == null) {
        statusLabel.setText("Status: Needs valid room");
      } else if (!findEmployee()) {
        statusLabel.setText("Status: Needs valid employee");
      } else if (!findPatient()) {
        statusLabel.setText("Status: Needs valid patient");
      } else {
        statusLabel.setText("Status: Valid Request");
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
      MealRequest mealRequest =
          new MealRequest(
              nodeID.getText(),
              Integer.parseInt(patientID.getText()),
              Integer.parseInt(employeeID.getText()),
              mealDropDown.getValue().toString(),
              allergy.getText(),
              requestDetails.getText(),
              statusDropDown.getValue().toString(),
              Timestamp.from(Instant.now()).toString());
      // Send the request to the Dao pattern
      try {
        if (updating) {
          mealRequestDao.updateServiceRequest(mealRequest, updateServiceID);
          updating = false;
        } else {
          mealRequestDao.addServiceRequest(mealRequest);
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
    allergy.setText("");

    mealDropDown.setValue("Select Meal");
    requestDetails.setText("");
    statusDropDown.setValue("Status");
    sendRequest.setDisable(true);
    validateButton();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    MealRequest delivery = mealDeliveryTable.getSelectionModel().getSelectedItem().getValue();

    employeeID.setText(String.valueOf(delivery.getEmployeeID()));
    patientID.setText(String.valueOf(delivery.getPatientID()));
    nodeID.setText(delivery.getLocation().getNodeID());
    allergy.setText(delivery.getAllergy());
    mealDropDown.setValue(delivery.getMealName());
    statusDropDown.setValue("Processing");
    requestDetails.setText(delivery.getRequestDetails());
    updateServiceID = delivery.getServiceID();
    updateTreeTable();
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      MealRequest delivery = mealDeliveryTable.getSelectionModel().getSelectedItem().getValue();
      mealRequestDao.removeServiceRequest(delivery);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(hospitalIDCol, (w - 30) / 10);
    setColumnSize(patientIDCol, (w - 30) / 10);
    setColumnSize(firstNameCol, (w - 30) / 10);
    setColumnSize(lastNameCol, (w - 30) / 10);
    setColumnSize(nodeIDCol, (w - 30) / 10);
    setColumnSize(mealCol, (w - 30) / 10);
    setColumnSize(allergyCol, (w - 30) / 10);
    setColumnSize(statusCol, (w - 30) / 10);
    setColumnSize(otherInfoCol, (w - 30) / 10);
    setColumnSize(timeStampCol, (w - 30) / 10);
  }
}
