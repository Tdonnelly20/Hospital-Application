package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.dao.SanitationRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.SanitationRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SanitationRequestController extends RequestController {
  @FXML private TextField hospitalID;
  @FXML private TextField patientID;
  @FXML private TextField roomLocation;
  @FXML private JFXComboBox<Object> sanitationDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Label statusLabel;

  @FXML private TreeTableView<SanitationRequest> sanitationRequestTable;
  @FXML private TreeTableColumn<SanitationRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<SanitationRequest, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<SanitationRequest, String> firstNameCol;
  @FXML private TreeTableColumn<SanitationRequest, String> lastNameCol;
  @FXML private TreeTableColumn<SanitationRequest, String> roomLocationCol;
  @FXML private TreeTableColumn<SanitationRequest, String> hazardCol;
  @FXML private TreeTableColumn<SanitationRequest, String> requestDetailsCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> statusCol;
  @FXML private Pane tablePane;
  private boolean updating = false;
  private int updateServiceID;

  private static final SanitationRequestDao SanitationRequestDao =
      (SanitationRequestDao) Vdb.requestSystem.getDao(Dao.SanitationRequest);
  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();

  private static class SingletonHelper {
    private static final SanitationRequestController controller = new SanitationRequestController();
  }

  public static SanitationRequestController getController() {
    return SanitationRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    updateTreeTable();
    setTitleText("Sanitation Request Service");
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
                sanitationRequestTable.setPrefWidth(w - 30);
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
                sanitationRequestTable.setPrefHeight(h - 75);
              }
            });
  }

  // ask about adding employee and patient DAOs to request System, requires them to use interface
  /** Determines if a medical delivery request is valid, and sends it to the Dao */
  @FXML
  void validateButton() {
    sendRequest.setDisable(true);
    statusLabel.setTextFill(Color.web("Red"));
    // If any field is left blank, (except for request details) throw an error
    if (patientID.getText().equals("")
        || hospitalID.getText().equals("")
        || roomLocation.getText().equals("")
        || sanitationDropDown.getValue() == null) {
      statusLabel.setText("Please fill in the required fields.");
      // Make sure the patient ID is an integer
    } else if (!isInteger(patientID.getText())
        || patientID.getText().isEmpty()) { // needs to check if patient even exists
      statusLabel.setText("Invalid Patient.");
    } else if (!isInteger(hospitalID.getText())
        || hospitalID.getText().isEmpty()) { // check if emp exists
      statusLabel.setText("Invalid Employee.");
    } else if (LocationDao.getLocation(roomLocation.getText()) == null) {
      statusLabel.setText("Invalid Room.");
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
    SanitationRequest request =
        new SanitationRequest(
            Integer.parseInt(hospitalID.getText()),
            Integer.parseInt(patientID.getText()),
            roomLocation.getText(),
            sanitationDropDown.getValue().toString(),
            requestDetails.getText());
    request.setStatus(statusDropDown.getValue().toString());
    if (updating) {
      SanitationRequestDao.updateServiceRequest(request, request.getServiceID());
    } else {
      SanitationRequestDao.addServiceRequest(request);
    }
    updating = false;
    // System.out.println(hospitalID + patientID + roomLocation,sanitationDropDown,requestDetails);
    updateTreeTable();
    resetForm(); // Set all fields to blank for another entry
  }

  @FXML
  void updateTreeTable() {
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientLastName"));
    roomLocationCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
    hazardCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("hazardName"));
    requestDetailsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("requestDetails"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    ArrayList<SanitationRequest> currSanitationRequests =
        (ArrayList<SanitationRequest>)
            RequestSystem.getSystem().getAllServiceRequests(Dao.SanitationRequest);

    ArrayList<TreeItem<SanitationRequest>> treeItems = new ArrayList<>();

    if (currSanitationRequests.isEmpty()) {
      sanitationRequestTable.setRoot(null);
    } else {
      for (SanitationRequest delivery : currSanitationRequests) {
        TreeItem<SanitationRequest> item = new TreeItem<>(delivery); // claims null pointer
        treeItems.add(item);
      }
      sanitationRequestTable.setShowRoot(false);
      TreeItem<SanitationRequest> root = new TreeItem<>(currSanitationRequests.get(0));
      sanitationRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  void resetForm() {
    patientID.setText("");
    hospitalID.setText("");
    roomLocation.setText("");
    sanitationDropDown.setValue(null);
    requestDetails.setText("");
    statusLabel.setText("");
    sendRequest.setDisable(true);
  }

  // same error as remove, pressing sendrequest causes issue
  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    if (sanitationRequestTable.getSelectionModel().getSelectedItem() != null) {
      SanitationRequest request =
          sanitationRequestTable.getSelectionModel().getSelectedItem().getValue();

      hospitalID.setText(String.valueOf(request.getEmployeeID()));
      patientID.setText(String.valueOf(request.getPatientID()));
      roomLocation.setText(request.getNodeID());
      sanitationDropDown.setValue(request.getHazardName());
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
      SanitationRequest delivery =
          sanitationRequestTable.getSelectionModel().getSelectedItem().getValue();
      SanitationRequestDao.removeServiceRequest(delivery);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(hospitalIDCol, (w - 30) / 8);
    setColumnSize(patientIDCol, (w - 30) / 8);
    setColumnSize(firstNameCol, (w - 30) / 8);
    setColumnSize(lastNameCol, (w - 30) / 8);
    setColumnSize(roomLocationCol, (w - 30) / 8);
    setColumnSize(hazardCol, (w - 30) / 8);
    setColumnSize(requestDetailsCol, (w - 30) / 8);
    setColumnSize(statusCol, (w - 30) / 8);
  }
}
