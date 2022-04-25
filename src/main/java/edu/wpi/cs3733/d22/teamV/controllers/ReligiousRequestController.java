package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.dao.ReligiousRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.MedicineDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.ReligiousRequest;
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
import javafx.stage.Stage;

public class ReligiousRequestController extends RequestController {
  @FXML private TextField employeeID;
  @FXML private TextField patientID;
  @FXML private TextField religion;
  @FXML private TextField roomNumber;
  @FXML private TextField details;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Button sendRequest;
  @FXML private Label statusLabel;
  @FXML private Pane tablePlane;

  @FXML private TreeTableView<ReligiousRequest> ReligiousRequestTable;
  @FXML private TreeTableColumn<ReligiousRequest, Integer> employeeIDCol;
  @FXML private TreeTableColumn<ReligiousRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> roomCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> religionCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> requestDetailsCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> statusCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> firstNameCol;
  @FXML private TreeTableColumn<MedicineDelivery, String> lastNameCol;

  private boolean updating;
  private int updateServiceID;
  // religious request can't seem to remove things if there are more than 1 now???
  private static class SingletonHelper {
    private static final ReligiousRequestController controller = new ReligiousRequestController();
  }

  private static final ReligiousRequestDao ReligiousRequestDao =
      (ReligiousRequestDao) Vdb.requestSystem.getDao(RequestSystem.Dao.ReligiousRequest);

  public static ReligiousRequestController getController() {
    return ReligiousRequestController.SingletonHelper.controller;
  }

  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();

  public void init() {
    setTitleText("Religious Request Service");
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
                ReligiousRequestTable.setPrefWidth(w - 30);
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
                ReligiousRequestTable.setPrefHeight(h - 75);
              }
            });
  }

  void setColumnSizes(double w) {
    setColumnSize(patientIDCol, (w - 30) / 5);
    setColumnSize(religionCol, (w - 30) / 5);
    setColumnSize(requestDetailsCol, (w - 30) / 5);
    setColumnSize(employeeIDCol, (w - 30) / 5);
    setColumnSize(roomCol, (w - 30) / 5);
  }

  // empIDCol;
  // @FXML private TextField employeeID;
  // @FXML private TextField patientID;
  // @FXML private TextField religion;
  // @FXML private TextField roomNumber;
  // @FXML private TextField details;
  // only displays religion and patientID for some reason
  // figure out why update/remove dont work
  @FXML
  void updateTreeTable() {
    employeeIDCol.setCellValueFactory(
        new TreeItemPropertyValueFactory<>("employeeID")); // issue, but it matches textfield
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    roomCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
    religionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("religion"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    requestDetailsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("details"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientLastName"));

    ArrayList<ReligiousRequest> requests =
        (ArrayList<ReligiousRequest>)
            RequestSystem.getSystem().getAllServiceRequests(RequestSystem.Dao.ReligiousRequest);
    ArrayList<TreeItem<ReligiousRequest>> treeItems = new ArrayList<>();
    // TreeItemPropertyVvalueFactory claims unable to retrieve property
    if (requests.isEmpty()) {
      ReligiousRequestTable.setRoot(null);
    } else {
      for (ReligiousRequest r : requests) {
        TreeItem<ReligiousRequest> item = new TreeItem<>(r);
        treeItems.add(item);
      }
      ReligiousRequestTable.setShowRoot(false);
      TreeItem<ReligiousRequest> root = new TreeItem<>(requests.get(0));
      ReligiousRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  void resetForm() {
    employeeID.setText("");
    patientID.setText("");
    roomNumber.setText("");
    religion.setText("");
    sendRequest.setDisable(true);
    statusDropDown.setValue(null);
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

  // Checks to see if the user can submit info
  @FXML
  void validateButton() {
    sendRequest.setDisable(true);
    boolean valid = true;
    String issues = "Issues:\n";
    // Information verification and submission needed
    if (!findPatient()) { // nned to check if patient exists
      issues += "Invalid Patient.\n";
      valid = false;
    }
    if (!findEmployee()) {
      issues += "Invalid Employee.\n";
      valid = false;
    }
    if (LocationDao.getLocation(roomNumber.getText()) == null) {
      issues += "Invalid Room.\n";
      valid = false;
    }
    if (religion.getText().isEmpty()) {
      valid = false;
      issues += "Specify Religion.\n";
    }
    if (statusDropDown.getValue() == null) {
      valid = false;
      issues += "Specify Status.\n";
    }
    if (valid) {
      issues = "";
      sendRequest.setDisable(false);
    }
    statusLabel.setText(issues);
  }

  @Override
  public void start(Stage primaryStage) {}

  @FXML
  void sendRequest()
      throws SQLException, IOException { // must check to see if its updating or new req
    ReligiousRequest request =
        new ReligiousRequest(
            Integer.parseInt(patientID.getText()),
            Integer.parseInt(employeeID.getText()),
            roomNumber.getText(),
            religion.getText(),
            details.getText(),
            statusDropDown.getValue().toString(),
            -1,
            Timestamp.from(Instant.now()).toString());
    request.setStatus(statusDropDown.getValue().toString());
    if (updating) {
      ReligiousRequestDao.updateServiceRequest(request, request.getServiceID());
    } else {
      ReligiousRequestDao.addServiceRequest(request);
    }
    updating = false;
    // System.out.println(hospitalID + patientID + roomLocation,sanitationDropDown,requestDetails);
    updateTreeTable();
    resetForm(); // Set all fields to blank for another entry
  }

  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    if (ReligiousRequestTable.getSelectionModel().getSelectedItem() != null) {
      ReligiousRequest request =
          ReligiousRequestTable.getSelectionModel().getSelectedItem().getValue();

      employeeID.setText(String.valueOf(request.getEmployeeID()));
      patientID.setText(String.valueOf(request.getPatientID()));
      religion.setText(request.getReligion());
      roomNumber.setText(request.getLocation().getNodeID());
      details.setText(request.getDetails());
      updateServiceID = request.getServiceID();
      statusDropDown.setValue(request.getStatus());
      updateServiceID = request.getServiceID();
      updateTreeTable();
    }
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      ReligiousRequest request =
          ReligiousRequestTable.getSelectionModel().getSelectedItem().getValue();
      ReligiousRequestDao.removeServiceRequest(request);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }
}
