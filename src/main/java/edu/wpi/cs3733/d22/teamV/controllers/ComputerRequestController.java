package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.ComputerRequestDao;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.ComputerRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ComputerRequestController extends RequestController {
  @FXML private TextField employeeID;
  @FXML private TextField patientID;
  @FXML private TextField roomLocation;
  @FXML private JFXComboBox<Object> computerDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Label statusLabel;

  @FXML private TreeTableView<ComputerRequest> computerRequestTable;
  @FXML private TreeTableColumn<ComputerRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<ComputerRequest, Integer> employeeIDCol;
  @FXML private TreeTableColumn<ComputerRequest, String> firstNameCol;
  @FXML private TreeTableColumn<ComputerRequest, String> lastNameCol;
  @FXML private TreeTableColumn<ComputerRequest, String> roomLocationCol;
  @FXML private TreeTableColumn<ComputerRequest, String> typeCol;
  @FXML private TreeTableColumn<ComputerRequest, String> requestDetailsCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> statusCol;
  @FXML private Pane tablePane;
  private boolean updating = false;
  private int updateServiceID;

  private static final ComputerRequestDao ComputerRequestDao =
      (ComputerRequestDao) Vdb.requestSystem.getDao(Dao.ComputerRequest);
  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();

  private static class SingletonHelper {
    private static final ComputerRequestController controller = new ComputerRequestController();
  }

  public static ComputerRequestController getController() {
    return ComputerRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    updateTreeTable();
    setTitleText("Computer Request Service");
    fillTopPane();
    updating = false;
    validateButton();

    setColumnSizes(960);

    tablePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePane.getWidth();
                computerRequestTable.setPrefWidth(w - 30);
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
                computerRequestTable.setPrefHeight(h - 75);
              }
            });
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

  /** Determines if a computer request is valid, and sends it to the Dao */
  @FXML
  void validateButton() {
    sendRequest.setDisable(true);
    statusLabel.setTextFill(Color.web("Red"));
    // If any field is left blank, (except for request details) throw an error
    if (patientID.getText().equals("")
        || employeeID.getText().equals("")
        || roomLocation.getText().equals("")
        || computerDropDown.getValue() == null) {
      statusLabel.setText("Please fill in the required fields.");
      // Make sure the patient ID is an integer
    } else if (!findPatient()) {
      statusLabel.setText("Invalid Patient.");
    } else if (!findEmployee()) { // check if emp exists
      statusLabel.setText("Invalid Employee.");
    } else if (LocationDao.getLocation(roomLocation.getText()) == null) {
      statusLabel.setText("Invalid Room.");
    } else if (computerDropDown.getValue() == null) {
      statusLabel.setText("Needs Type");
    } else if (statusDropDown.getValue() == null) {
      statusLabel.setText("Needs Status");
    } else {
      statusLabel.setText("");
      sendRequest.setDisable(false);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  void sendRequest()
      throws SQLException, IOException { // must check to see if its updating or new req
    ComputerRequest request =
        new ComputerRequest(
            Integer.parseInt(employeeID.getText()),
            Integer.parseInt(patientID.getText()),
            roomLocation.getText(),
            computerDropDown.getValue().toString(),
            requestDetails.getText(),
            statusDropDown.getValue().toString(),
            -1,
            Timestamp.from(Instant.now()).toString());
    request.setStatus(statusDropDown.getValue().toString());
    if (updating) {
      ComputerRequestDao.updateServiceRequest(request, request.getServiceID());
    } else {
      ComputerRequestDao.addServiceRequest(request);
    }
    updating = false;
    updateTreeTable();
    resetForm(); // Set all fields to blank for another entry
  }

  @FXML
  void updateTreeTable() {
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientLastName"));
    roomLocationCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
    typeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("typeName"));
    requestDetailsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("details"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    ArrayList<ComputerRequest> currComputerRequests =
        (ArrayList<ComputerRequest>)
            RequestSystem.getSystem().getAllServiceRequests(Dao.ComputerRequest);
    ArrayList<TreeItem<ComputerRequest>> treeItems = new ArrayList<>();

    if (currComputerRequests.isEmpty()) {
      computerRequestTable.setRoot(null);
    } else {
      for (ComputerRequest delivery : currComputerRequests) {
        TreeItem<ComputerRequest> item = new TreeItem<>(delivery); // claims null pointer
        treeItems.add(item);
      }
      computerRequestTable.setShowRoot(false);
      TreeItem<ComputerRequest> root = new TreeItem<>(currComputerRequests.get(0));
      computerRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  void resetForm() {
    patientID.setText("");
    employeeID.setText("");
    roomLocation.setText("");
    computerDropDown.setValue(null);
    requestDetails.setText("");
    statusLabel.setText("");
    sendRequest.setDisable(true);
    validateButton();
  }

  // same error as remove, pressing sendrequest causes issue
  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    if (computerRequestTable.getSelectionModel().getSelectedItem() != null) {
      ComputerRequest request =
          computerRequestTable.getSelectionModel().getSelectedItem().getValue();

      employeeID.setText(String.valueOf(request.getEmployeeID()));
      patientID.setText(String.valueOf(request.getPatientID()));
      roomLocation.setText(request.getNodeID());
      computerDropDown.setValue(request.getTypeName());
      requestDetails.setText(request.getRequestDetails());
      updateServiceID = request.getServiceID();
      statusDropDown.setValue(request.getStatus());
      updateTreeTable();
    }
  }
  // has error
  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      ComputerRequest delivery =
          computerRequestTable.getSelectionModel().getSelectedItem().getValue();
      ComputerRequestDao.removeServiceRequest(delivery);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(employeeIDCol, (w - 30) / 8);
    setColumnSize(patientIDCol, (w - 30) / 8);
    setColumnSize(firstNameCol, (w - 30) / 8);
    setColumnSize(lastNameCol, (w - 30) / 8);
    setColumnSize(roomLocationCol, (w - 30) / 8);
    setColumnSize(typeCol, (w - 30) / 8);
    setColumnSize(requestDetailsCol, (w - 30) / 8);
    setColumnSize(statusCol, (w - 30) / 8);
  }
}
