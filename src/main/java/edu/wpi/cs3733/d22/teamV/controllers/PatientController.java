package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.dao.PatientDao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PatientController extends MapController {

  private static final PatientDao patientDao = (PatientDao) Vdb.requestSystem.getPatientDao();
  private boolean updating = false;

  private static class SingletonHelper {
    private static final PatientController controller = new PatientController();
  }

  public static PatientController getController() {
    return PatientController.SingletonHelper.controller;
  }

  @FXML private TreeTableView<Patient> table;
  @FXML private TreeTableColumn<Patient, Integer> patientIDCol;
  @FXML private TreeTableColumn<Patient, String> firstNameCol;
  @FXML private TreeTableColumn<Patient, String> lastNameCol;
  @FXML private TreeTableColumn<Patient, Integer> employeeIDCol;
  @FXML private TreeTableColumn<Patient, Integer> serviceIDCol;

  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private Button sendRequest;
  @FXML private Button removeButton;
  @FXML private Button updateButton;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;

  @FXML
  public void updateTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("lastName"));
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeIDs"));
    serviceIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("serviceIDs"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<Patient> currPatients = (ArrayList<Patient>) patientDao.getAllPatients();
    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currPatients.isEmpty()) {
      table.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (Patient delivery : currPatients) {
      TreeItem<Patient> item = new TreeItem(delivery);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    table.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem root = new TreeItem(currPatients.get(0));
    // Set the root in the table
    table.setRoot(root);
    // Set the rest of the tree items to the root, including the one we set as the root
    root.getChildren().addAll(treeItems);
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
              firstName.getText(),
              lastName.getText(),
              new ArrayList<Integer>(),
              new ArrayList<Integer>());
      // Send the request to the Dao pattern
      try {
        if (updating) {
          patientDao.updatePatient(patient, Vdb.requestSystem.getPatientID());
          updating = false;
        } else {
          patientDao.addPatient(patient);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      resetForm(); // Set all fields to blank for another entry
      updateTreeTable();
    }
  }

  @FXML
  public void resetForm() {
    updating = false;
    firstName.setText("");
    lastName.setText("");
    requestDetails.setText("");
    sendRequest.setDisable(true);
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      Patient patient = table.getSelectionModel().getSelectedItem().getValue();
      patientDao.removePatient(patient);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    Patient patient = table.getSelectionModel().getSelectedItem().getValue();

    firstName.setText(String.valueOf(patient.getFirstName()));
    lastName.setText(String.valueOf(patient.getLastName()));
    updateTreeTable();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
