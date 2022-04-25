package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.dao.PatientDao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PatientController extends RequestController {
  private static final PatientDao patientDao = Vdb.requestSystem.getPatientDao();
  private boolean updating = false;

  private static class SingletonHelper {
    private static final PatientController controller = new PatientController();
  }

  public static PatientController getController() {
    return PatientController.SingletonHelper.controller;
  }

  @FXML private TreeTableView<Patient> patientTable;
  @FXML private TreeTableColumn<Patient, Integer> patientIDCol;
  @FXML private TreeTableColumn<Patient, String> firstNameCol;
  @FXML private TreeTableColumn<Patient, String> lastNameCol;
  @FXML private TreeTableColumn<Patient, Integer> patientEmployeeIDCol;
  @FXML private TreeTableColumn<Patient, Integer> patientServiceIDSCol;
  @FXML private Pane patientPane;

  @FXML private TreeTableView<Employee> employeeTable;
  @FXML private TreeTableColumn<Employee, Integer> employeeIDCol;
  @FXML private TreeTableColumn<Employee, String> employeeFirstNameCol;
  @FXML private TreeTableColumn<Employee, String> employeeLastNameCol;
  @FXML private TreeTableColumn<Employee, String> specialtiesCol;
  @FXML private TreeTableColumn<Employee, String> otherServiceRequestsCol;
  @FXML private TreeTableColumn<Employee, String> positionCol;
  @FXML private Pane employeePane;

  @FXML private TreeTableView<ServiceRequest> serviceRequestTable;
  @FXML private TreeTableColumn<ServiceRequest, Integer> serviceRequestIDCol;
  @FXML private TreeTableColumn<ServiceRequest, String> serviceRequestEmployeeIDSCol;
  @FXML private TreeTableColumn<ServiceRequest, String> serviceTypeCol;
  @FXML private TreeTableColumn<ServiceRequest, String> statusCol;
  @FXML private TreeTableColumn<ServiceRequest, String> locationCol;
  @FXML private Pane serviceRequestPane;

  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private Button sendRequest;
  @FXML private Label statusLabel;
  private Patient selectedPatient;

  @FXML
  public void updatePatientTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastName"));
    patientEmployeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeIDs"));
    patientServiceIDSCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("serviceIDs"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<Patient> currPatients = patientDao.getAllPatients();
    // Create a list for our tree items
    ArrayList<TreeItem<Patient>> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currPatients.isEmpty()) {
      patientTable.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (Patient delivery : currPatients) {
      TreeItem<Patient> item = new TreeItem<Patient>(delivery);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    patientTable.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem<Patient> root = new TreeItem<>(currPatients.get(0));
    // Set the root in the table
    patientTable.setRoot(root);
    // Set the rest of the tree items to the root, including the one we set as the root
    root.getChildren().addAll(treeItems);
  }

  @FXML
  public void updateEmployeeTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    employeeFirstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstName"));
    employeeLastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastName"));
    specialtiesCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("specialties"));
    positionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeePosition"));
    otherServiceRequestsCol.setCellValueFactory(
        new TreeItemPropertyValueFactory<>("serviceRequestIDs"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<Employee> currEmployees = selectedPatient.getEmployeeList();
    // Create a list for our tree items
    ArrayList<TreeItem<Employee>> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currEmployees.isEmpty()) {
      employeeTable.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (Employee employee : currEmployees) {
      TreeItem<Employee> item = new TreeItem<>(employee);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    employeeTable.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem<Employee> root = new TreeItem<>(currEmployees.get(0));
    // Set the root in the table
    employeeTable.setRoot(root);
    // Set the rest of the tree items to the root, including the one we set as the root
    root.getChildren().addAll(treeItems);
  }

  @FXML
  public void updateServiceRequestTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    serviceRequestIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("serviceID"));
    serviceRequestEmployeeIDSCol.setCellValueFactory(
        new TreeItemPropertyValueFactory<>("patientID"));
    serviceTypeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
    locationCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("location"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<ServiceRequest> currRequests = selectedPatient.getServiceRequestList();
    // Create a list for our tree items
    ArrayList<TreeItem<ServiceRequest>> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currRequests.isEmpty()) {
      serviceRequestTable.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (ServiceRequest request : currRequests) {
      TreeItem<ServiceRequest> item = new TreeItem<>(request);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    serviceRequestTable.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem<ServiceRequest> root = new TreeItem<>(currRequests.get(0));
    // Set the root in the table
    serviceRequestTable.setRoot(root);
    // Set the rest of the tree items to the root, including the one we set as the root
    root.getChildren().addAll(treeItems);
  }

  @FXML
  public void checkIfSelected() {
    try {
      selectedPatient = patientTable.getSelectionModel().getSelectedItem().getValue();
      updateEmployeeTreeTable();
      updateServiceRequestTable();
    } catch (NullPointerException e) {
      // selectedEmployee = null;
    }
  }

  @Override
  public void init() {
    setTitleText("Patient Database");
    fillTopPane();

    setColumnSizes(910);

    patientPane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = patientPane.getWidth();
                patientPane.setPrefWidth(w - 30);
                setColumnSizes(w);
              }
            });

    patientPane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = patientPane.getHeight();
                patientTable.setPrefHeight(h - 75);
              }
            });

    employeePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = employeePane.getWidth();
                employeeTable.setPrefWidth(w - 30);
                setColumnSizes2(w);
              }
            });

    employeePane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = employeePane.getHeight();
                employeeTable.setPrefHeight(h - 75);
              }
            });

    serviceRequestPane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = serviceRequestPane.getWidth();
                serviceRequestTable.setPrefWidth(w - 30);
                setColumnSizes3(w);
              }
            });

    serviceRequestPane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = serviceRequestPane.getHeight();
                serviceRequestTable.setPrefHeight(h - 75);
              }
            });
  }

  void setColumnSizes(double w) {
    setColumnSize(patientIDCol, (w - 30) / 5);
    setColumnSize(patientEmployeeIDCol, (w - 30) / 5);
    setColumnSize(firstNameCol, (w - 30) / 5);
    setColumnSize(lastNameCol, (w - 30) / 5);
    setColumnSize(patientServiceIDSCol, (w - 30) / 5);
  }

  void setColumnSizes2(double w) {
    setColumnSize(employeeIDCol, (w - 30) / 6);
    setColumnSize(employeeFirstNameCol, (w - 30) / 6);
    setColumnSize(employeeLastNameCol, (w - 30) / 6);
    setColumnSize(specialtiesCol, (w - 30) / 6);
    setColumnSize(otherServiceRequestsCol, (w - 30) / 6);
    setColumnSize(positionCol, (w - 30) / 6);
  }

  void setColumnSizes3(double w) {
    setColumnSize(serviceRequestIDCol, (w - 30) / 5);
    setColumnSize(serviceRequestEmployeeIDSCol, (w - 30) / 5);
    setColumnSize(serviceTypeCol, (w - 30) / 5);
    setColumnSize(statusCol, (w - 30) / 5);
    setColumnSize(locationCol, (w - 30) / 5);
  }

  @FXML
  public void validateButton() {
    if (updating) {
      sendRequest.setText("Update Request");
    } else {
      sendRequest.setText("Send Request");
    }

    try {
      if ((firstName.getText().equals("") && lastName.getText().equals(""))) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Blank");
        statusLabel.setTextFill(Color.web("Black"));
      } else if ((firstName.getText().equals("") || lastName.getText().equals(""))) {
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

  /** Determines if an employee is valid, and sends it to the Dao */
  public void sendRequest() {
    // If any field is left blank, (except for request details) throw an error
    // Make sure the patient ID is an integer
    {
      Patient patient =
          new Patient(
              firstName.getText(), lastName.getText(), new ArrayList<>(), new ArrayList<>());
      // Send the request to the Dao pattern
      try {
        if (updating) {
          selectedPatient.setFirstName(firstName.getText());
          selectedPatient.setLastName(lastName.getText());
          patientDao.updatePatient(selectedPatient, selectedPatient.getPatientID());
          updating = false;
        } else {
          patientDao.addPatient(patient);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      resetForm(); // Set all fields to blank for another entry
      updatePatientTreeTable();
    }
  }

  @Override
  void updateTreeTable() {}

  @FXML
  public void resetForm() {
    updating = false;
    firstName.setText("");
    lastName.setText("");
    sendRequest.setDisable(true);
  }

  @FXML
  private void removeSelectedRow(MouseEvent event) {
    try {

      Patient patient = patientTable.getSelectionModel().getSelectedItem().getValue();
      patientDao.removePatient(patient);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updatePatientTreeTable();
  }

  @FXML
  private void updateSelectedRow() throws NullPointerException {
    updating = true;
    selectedPatient = patientTable.getSelectionModel().getSelectedItem().getValue();

    firstName.setText(String.valueOf(selectedPatient.getFirstName()));
    lastName.setText(String.valueOf(selectedPatient.getLastName()));

    updatePatientTreeTable();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
