package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LabRequestDao;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.*;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.LabRequest;
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

public class LabRequestController extends RequestController {
  @FXML private TreeTableView<LabRequest> table;
  @FXML private TreeTableColumn<LabRequest, String> nodeIDCol;
  @FXML private TreeTableColumn<LabRequest, Integer> employeeIDCol;
  @FXML private TreeTableColumn<LabRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<LabRequest, String> firstNameCol;
  @FXML private TreeTableColumn<LabRequest, String> lastNameCol;
  @FXML private TreeTableColumn<LabRequest, String> requestedLabCol;
  @FXML private TreeTableColumn<LabRequest, String> statusCol;
  @FXML private TreeTableColumn<LabRequest, String> timeStampCol;
  @FXML private Pane tablePlane;

  private static final LabRequestDao labRequestDao =
      (LabRequestDao) Vdb.requestSystem.getDao(Dao.LabRequest);
  @FXML private TextField employeeID;
  @FXML private TextField patientID;
  @FXML private TextField nodeID;
  @FXML private JFXComboBox<Object> requestedLab;
  @FXML private JFXComboBox<Object> statusDropDown;
  @FXML private Label status;
  @FXML private Button sendRequest;

  private boolean updating = false;
  private int updateServiceID;

  private static class SingletonHelper {
    private static final LabRequestController manager = new LabRequestController();
  }

  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();

  @Override
  public void init() {
    setTitleText("Lab Request");
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
                table.setPrefWidth(w - 30);
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
                table.setPrefHeight(h - 75);
              }
            });
  }

  @Override
  @FXML
  public void resetForm() {
    nodeID.setText("");
    statusDropDown.setValue("Status: ");
    employeeID.setText("");
    patientID.setText("");
    requestedLab.setValue("Select Lab");
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

  // Checks to see if the user can submit info
  @Override
  public void validateButton() {
    try {
      sendRequest.setDisable(true);
      if ((employeeID.getText().isEmpty())
          && (patientID.getText().isEmpty())
          && (requestedLab.getValue().equals("Select Lab"))) {
        status.setText("Status: Blank");
      } else if ((employeeID.getText().isEmpty())
          || (patientID.getText().isEmpty())
          || (requestedLab.getValue().equals("Select Lab"))) {
        status.setText("Status: Processing");
      } else if (LocationDao.getLocation(nodeID.getText()) == null) {
        status.setText("Status: Needs valid room");
      } else if (!findEmployee()) {
        status.setText("Status: Needs valid employee");
      } else if (!findPatient()) {
        status.setText("Status: Needs valid patient");
      } else {
        sendRequest.setDisable(false);
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  /** Runs whenever we switch to the table, or update a value */
  @Override
  public void updateTreeTable() {
    // Set our cell values based on the LabRequest Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nodeID"));
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientLastName"));
    requestedLabCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("lab"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    timeStampCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("timeString"));
    // Get the current list of lab requests from the DAO
    ArrayList<LabRequest> currLabRequests =
        (ArrayList<LabRequest>) labRequestDao.getAllServiceRequests();

    // Create a list for our tree items
    ArrayList<TreeItem<LabRequest>> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currLabRequests.isEmpty()) {
      table.setRoot(null);
    } else {
      // for each loop cycling through each lab request currently entered into the system
      for (LabRequest lab : currLabRequests) {
        TreeItem<LabRequest> item = new TreeItem<>(lab);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      table.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem<LabRequest> root = new TreeItem<>(currLabRequests.get(0));
      // Set the root in the table
      table.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
    }
  }

  public void sendRequest() {
    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(employeeID.getText())) {
      status.setText("Status: Failed. Patient/Hospital ID must be a number!");

      // If all conditions pass, create the request
    } else {
      LabRequest l =
          new LabRequest(
              Integer.parseInt(employeeID.getText()),
              Integer.parseInt(patientID.getText()),
              nodeID.getText(),
              requestedLab.getValue().toString(),
              statusDropDown.getValue().toString(),
              Timestamp.from(Instant.now()).toString());
      try {
        if (updating) {
          Vdb.requestSystem.getDao(Dao.LabRequest).updateServiceRequest(l, updateServiceID);
          updating = false;
        } else {
          RequestSystem.getSystem().addServiceRequest(l, Dao.LabRequest);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      resetForm();
      updateTreeTable();
    }
  }

  @FXML
  private void updateSelectedRow() throws NullPointerException {
    System.out.println("here");
    updating = true;
    LabRequest request = table.getSelectionModel().getSelectedItem().getValue();
    updateServiceID = request.getServiceID();

    nodeID.setText(request.getNodeID());
    employeeID.setText(Integer.toString(request.getEmployeeID()));
    patientID.setText(Integer.toString(request.getPatientID()));
    requestedLab.setValue(request.getLab());
    statusDropDown.setValue(request.getStatus());

    sendRequest.setText("Update");
    updateTreeTable();
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      LabRequest request = table.getSelectionModel().getSelectedItem().getValue();
      RequestSystem.getSystem().getDao(Dao.LabRequest).removeServiceRequest(request);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(nodeIDCol, (w - 30) / 8);
    setColumnSize(employeeIDCol, (w - 30) / 8);
    setColumnSize(patientIDCol, (w - 30) / 8);
    setColumnSize(firstNameCol, (w - 30) / 8);
    setColumnSize(lastNameCol, (w - 30) / 8);
    setColumnSize(requestedLabCol, (w - 30) / 8);
    setColumnSize(statusCol, (w - 30) / 8);
    setColumnSize(timeStampCol, (w - 30) / 8);
  }

  @Override
  public void start(Stage primaryStage) {}
}
