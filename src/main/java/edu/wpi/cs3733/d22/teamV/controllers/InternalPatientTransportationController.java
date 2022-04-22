package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.InternalPatientTransportationDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.InternalPatientTransportation;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
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

public class InternalPatientTransportationController extends RequestController {

  @FXML private TreeTableView<InternalPatientTransportation> internalPatientTransportationTable;

  @FXML private TreeTableColumn<InternalPatientTransportation, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<InternalPatientTransportation, Integer> patientIDCol;
  @FXML private TreeTableColumn<InternalPatientTransportation, String> firstNameCol;
  @FXML private TreeTableColumn<InternalPatientTransportation, String> lastNameCol;
  @FXML private TreeTableColumn<InternalPatientTransportation, String> roomNumberCol;
  @FXML private TreeTableColumn<InternalPatientTransportation, String> otherInfoCol;
  @FXML private TreeTableColumn<InternalPatientTransportation, String> statusCol;
  @FXML private Pane tablePane;

  @FXML private TextField patientID;
  @FXML private TextField hospitalID;
  @FXML private TextField roomNum;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;
  @FXML private JFXComboBox<Object> statusDropDown;

  // The two fields that are required for when a request is being updated
  private boolean updating = false;
  private int updateServiceID;

  public static final InternalPatientTransportationDao internalPatientTransportationDao =
      (InternalPatientTransportationDao)
          Vdb.requestSystem.getDao(RequestSystem.Dao.InternalPatientTransportation);

  private static class SingletonHelper {
    private static final InternalPatientTransportationController controller =
        new InternalPatientTransportationController();
  }

  public static InternalPatientTransportationController getController() {
    return InternalPatientTransportationController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    setTitleText("Internal Patient Transportation");
    fillTopPane();
    updateTreeTable();
    setColumnSizes(910);

    tablePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePane.getWidth();
                internalPatientTransportationTable.setPrefWidth(w - 30);
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
                internalPatientTransportationTable.setPrefHeight(h - 75);
              }
            });
  }

  /** Update the table with values from fields and the DB */
  @Override
  public void updateTreeTable() {
    // TODO add Date and time column

    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    roomNumberCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));
    otherInfoCol.setCellValueFactory(new TreeItemPropertyValueFactory("requestDetails"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory("status"));

    // Create list for tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();
    ArrayList<InternalPatientTransportation> currInternalPatientTransportations =
        internalPatientTransportationDao.getInternalPatientTransportations();
    // make sure the list isn't empty
    if (currInternalPatientTransportations.isEmpty()) {
      internalPatientTransportationTable.setRoot(null);

    } else {
      for (InternalPatientTransportation transport : currInternalPatientTransportations) {
        TreeItem<InternalPatientTransportation> item = new TreeItem(transport);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      internalPatientTransportationTable.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(currInternalPatientTransportations.get(0));
      // Set the root in the table
      internalPatientTransportationTable.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  public void resetForm() {
    patientID.setText("");
    hospitalID.setText("");
    roomNum.setText("");
    statusDropDown.setValue(null);
    requestDetails.setText("");
    sendRequest.setDisable(true);
    sendRequest.setText("Send Request");
  }

  /** Determine whether or not all fields have been filled out, so we can submit the info */
  @FXML
  public void validateButton() {

    try {
      if ((hospitalID.getText().equals("")
              && patientID.getText().equals("")
              && roomNum.getText().equals(""))
          && statusDropDown.getValue() == null) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Blank");
        statusLabel.setTextFill(Color.web("Black"));
      } else if ((hospitalID.getText().equals("")
              || patientID.getText().equals("")
              || roomNum.getText().equals(""))
          || statusDropDown.getValue() == null) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Processing");
        statusLabel.setTextFill(Color.web("Black"));
      } else {
        statusLabel.setText("Status: Done");
        sendRequest.setDisable(false);
      }
    } catch (Exception e) {
      sendRequest.setDisable(true);
    }
  }

  /** Determines if a medical delivery request is valid, and sends it to the Dao */
  public void sendRequest() throws SQLException, IOException {
    // If any field is left blank, (except for request details) throw an error

    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(hospitalID.getText())) {
      statusLabel.setText("Status: Failed. Patient/Hospital ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {

      // Send the request to the Dao pattern
      InternalPatientTransportation internalPatientTransportation =
          new InternalPatientTransportation(
              roomNum.getText(),
              Integer.parseInt(patientID.getText()),
              Integer.parseInt(hospitalID.getText()),
              requestDetails.getText(),
              statusDropDown.getValue().toString());

      try {
        if (updating) {
          Vdb.requestSystem
              .getDao(RequestSystem.Dao.InternalPatientTransportation)
              .updateServiceRequest(internalPatientTransportation, updateServiceID);
          updating = false;
        } else {
          RequestSystem.getSystem()
              .addServiceRequest(
                  internalPatientTransportation, RequestSystem.Dao.InternalPatientTransportation);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      resetForm(); // Set all fields to blank for another entry
      updateTreeTable();
    }
  }

  @FXML
  private void updateSelectedRow() throws NullPointerException {
    updating = true;
    InternalPatientTransportation transport =
        internalPatientTransportationTable.getSelectionModel().getSelectedItem().getValue();

    hospitalID.setText(String.valueOf(transport.getEmployeeID()));
    patientID.setText(String.valueOf(transport.getPatientID()));
    roomNum.setText(transport.getNodeID());
    requestDetails.setText(transport.getRequestDetails());
    statusDropDown.setValue(transport.getStatus());
    updateServiceID = transport.getServiceID();
    sendRequest.setText("Update");
    updateTreeTable();
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      InternalPatientTransportation transport =
          internalPatientTransportationTable.getSelectionModel().getSelectedItem().getValue();
      RequestSystem.getSystem()
          .getDao(RequestSystem.Dao.InternalPatientTransportation)
          .removeServiceRequest(transport);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(patientIDCol, (w - 30) / 7);
    setColumnSize(hospitalIDCol, (w - 30) / 7);
    setColumnSize(firstNameCol, (w - 30) / 7);
    setColumnSize(lastNameCol, (w - 30) / 7);
    setColumnSize(roomNumberCol, (w - 30) / 7);
    setColumnSize(otherInfoCol, (w - 30) / 7);
    setColumnSize(statusCol, (w - 30) / 7);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
