package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.*;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
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
import javafx.stage.Stage;

public class EquipmentRequestController extends RequestController {
  // These are the buttons, text fields and labels that appear in the right column of the request
  @FXML private TextField patientID;
  @FXML private TextField employeeID;
  @FXML private Label status;
  @FXML private TextField pos;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private TextField quant;
  @FXML private TextArea notes;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Button sendRequest;

  // This is the table and columns for the equipment request table
  @FXML private TreeTableView<EquipmentDelivery> equipmentRequestTable;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> patientIDCol;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> employeeIDCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> firstNameCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> lastNameCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> posCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> equipCol;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> quantCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> notesCol;
  @FXML private Pane tablePlane;

  // This is the table and columns for the equipment table
  @FXML private TreeTableView<Equipment> table;
  @FXML private TreeTableColumn<Equipment, String> nodeIDCol;
  @FXML private TreeTableColumn<Equipment, Integer> xCol;
  @FXML private TreeTableColumn<Equipment, Integer> yCol;
  @FXML private TreeTableColumn<Equipment, String> floorCol;
  @FXML private TreeTableColumn<Equipment, String> buildingCol;
  @FXML private TreeTableColumn<Equipment, String> nodeTypeCol;
  @FXML private TreeTableColumn<Equipment, Boolean> shortNameCol;
  @FXML private Pane tablePane2;

  // The two fields that are required for when a request is being updated
  private boolean updating = false;
  private int updateServiceID;
  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();
  // Helper for setting up
  private static class SingletonHelper {
    private static final EquipmentRequestController controller = new EquipmentRequestController();
  }

  public static EquipmentRequestController getController() {
    return EquipmentRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    System.out.println("In init Equipment Request");
    setTitleText("Equipment Delivery Request");
    fillTopPane();

    setColumnSizes(910);

    tablePlane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePlane.getWidth();
                equipmentRequestTable.setPrefWidth(w - 30);
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
                equipmentRequestTable.setPrefHeight(h - 75);
              }
            });

    tablePane2
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePane2.getWidth();
                table.setPrefWidth(w - 30);
                setColumnSizes2(w);
              }
            });

    tablePane2
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = tablePane2.getHeight();
                table.setPrefHeight(h - 30);
              }
            });
  }

  @FXML
  void updateTreeTable() {
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    posCol.setCellValueFactory(new TreeItemPropertyValueFactory("locationName"));
    equipCol.setCellValueFactory(new TreeItemPropertyValueFactory("equipment"));
    quantCol.setCellValueFactory(new TreeItemPropertyValueFactory("quantity"));
    notesCol.setCellValueFactory(new TreeItemPropertyValueFactory("notes"));

    ArrayList<EquipmentDelivery> currEquipmentDeliveries =
        (ArrayList<EquipmentDelivery>)
            RequestSystem.getSystem().getAllServiceRequests(Dao.EquipmentDelivery);

    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (currEquipmentDeliveries.isEmpty()) {
      equipmentRequestTable.setRoot(null);
    } else {
      for (EquipmentDelivery delivery : currEquipmentDeliveries) {
        TreeItem<EquipmentDelivery> item = new TreeItem(delivery);
        treeItems.add(item);
      }
      equipmentRequestTable.setShowRoot(false);
      TreeItem root = new TreeItem(currEquipmentDeliveries.get(0));
      equipmentRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void updateEquipmentTable() {
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("ID"));
    xCol.setCellValueFactory(new TreeItemPropertyValueFactory("x"));
    yCol.setCellValueFactory(new TreeItemPropertyValueFactory("y"));
    floorCol.setCellValueFactory(new TreeItemPropertyValueFactory("floor"));
    buildingCol.setCellValueFactory(new TreeItemPropertyValueFactory("name"));
    nodeTypeCol.setCellValueFactory(new TreeItemPropertyValueFactory("description"));
    shortNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("isDirtyString"));

    ArrayList<Equipment> currEquipment = Vdb.requestSystem.getEquipment();
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currEquipment.isEmpty()) {

      for (Equipment pos : currEquipment) {
        TreeItem<Equipment> item = new TreeItem(pos);
        treeItems.add(item);
      }

      table.setShowRoot(false);
      TreeItem root = new TreeItem(RequestSystem.getSystem().getEquipment().get(0));
      table.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  void resetForm() {
    employeeID.setText("");
    patientID.setText("");
    status.setText("Status: Blank");
    pos.setText("");
    notes.setText("");
    quant.setText("");
    dropDown.setValue(null);
    sendRequest.setDisable(true);
    sendRequest.setText("Send Request");
    validateButton();
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

  @FXML
  void validateButton() {
    sendRequest.setDisable(true);
    if (employeeID.getText().isEmpty()
        && patientID.getText().isEmpty()
        && employeeID.getText().isEmpty()
        && pos.getText().isEmpty()
        && dropDown.getValue() == null
        && quant.getText().isEmpty()
        && statusDropDown.getValue() == null) {
      status.setText("Status: Blank");
    } else if (employeeID.getText().isEmpty()
        || patientID.getText().isEmpty()
        || employeeID.getText().isEmpty()
        || status.getText().isEmpty()
        || pos.getText().isEmpty()
        || dropDown.getValue() == null
        || quant.getText().isEmpty()
        || statusDropDown.getValue() == null) {
      status.setText("Status: Processing");
    } else if (!quant.getText().isEmpty() && !isInteger(quant.getText())) {
      status.setText("Status: Quantity is not a number");
    } else if (!findPatient()) {
      status.setText("Invalid Patient.");
      sendRequest.setDisable(true);
    } else if (!findEmployee()) { // check if emp exists
      status.setText("Invalid Employee.");
      sendRequest.setDisable(true);
    } else if (LocationDao.getLocation(pos.getText()) == null) {
      status.setText("Invalid Room.");
      sendRequest.setDisable(true);
    } else {
      status.setText("Status: Good");
      sendRequest.setDisable(false);
    }
  }

  @FXML
  private void sendRequest() throws SQLException {

    EquipmentDelivery delivery =
        new EquipmentDelivery(
            Integer.parseInt(employeeID.getText()),
            Integer.parseInt(patientID.getText()),
            pos.getText(),
            dropDown.getValue().toString(),
            notes.getText(),
            Integer.parseInt(quant.getText()),
            status.getText());

    try {
      if (updating) {
        Vdb.requestSystem
            .getDao(Dao.EquipmentDelivery)
            .updateServiceRequest(delivery, updateServiceID);
        updating = false;
      } else {
        RequestSystem.getSystem().addServiceRequest(delivery, Dao.EquipmentDelivery);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    resetForm();
    updateTreeTable();
  }

  @FXML
  private void updateSelectedRow() throws NullPointerException {
    updating = true;
    EquipmentDelivery delivery =
        equipmentRequestTable.getSelectionModel().getSelectedItem().getValue();

    employeeID.setText(String.valueOf(delivery.getEmployeeID()));
    patientID.setText(String.valueOf(delivery.getPatientID()));
    pos.setText(delivery.getLocationName());
    dropDown.setValue(delivery.getEquipment());
    quant.setText(Integer.toString(delivery.getQuantity()));
    notes.setText(delivery.getDetails());
    statusDropDown.setValue(delivery.getStatus());
    updateServiceID = delivery.getServiceID();
    sendRequest.setText("Update");
    updateTreeTable();
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      EquipmentDelivery delivery =
          equipmentRequestTable.getSelectionModel().getSelectedItem().getValue();
      RequestSystem.getSystem().getDao(Dao.EquipmentDelivery).removeServiceRequest(delivery);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(patientIDCol, (w - 30) / 8);
    setColumnSize(employeeIDCol, (w - 30) / 8);
    setColumnSize(firstNameCol, (w - 30) / 8);
    setColumnSize(lastNameCol, (w - 30) / 8);
    setColumnSize(posCol, (w - 30) / 8);
    setColumnSize(equipCol, (w - 30) / 8);
    setColumnSize(quantCol, (w - 30) / 8);
    setColumnSize(notesCol, (w - 30) / 8);
  }

  void setColumnSizes2(double w) {
    setColumnSize(nodeIDCol, (w - 30) / 7);
    setColumnSize(xCol, (w - 30) / 7);
    setColumnSize(yCol, (w - 30) / 7);
    setColumnSize(floorCol, (w - 30) / 7);
    setColumnSize(buildingCol, (w - 30) / 7);
    setColumnSize(nodeTypeCol, (w - 30) / 7);
    setColumnSize(shortNameCol, (w - 30) / 7);
  }

  @Override
  public void start(Stage primaryStage) {}
}
