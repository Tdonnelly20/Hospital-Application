package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LabRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.*;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.LabRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
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

  public static LabRequestController getManager() {
    return LabRequestController.SingletonHelper.manager;
  }

  @Override
  public void init() {
    setTitleText("Lab Request");
    fillTopPane();
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
  }

  // Checks to see if the user can submit info
  @Override
  public void validateButton() {
    try {
      if (!(employeeID.getText().isEmpty())
          && !(patientID.getText().isEmpty())
          && !(requestedLab.getValue().equals("Select Lab"))) {
        // Information verification and submission needed
        sendRequest.setDisable(false);
      } else if (!(employeeID.getText().isEmpty())
          || !(patientID.getText().isEmpty())
          || !(requestedLab.getValue().equals("Select Lab"))) {
        status.setText("Status: Processing");
      } else {
        status.setText("Status: Blank");
        sendRequest.setDisable(true);
      }
    } catch (NullPointerException e) {

    }
  }

  /** Runs whenever we switch to the table, or update a value */
  @Override
  public void updateTreeTable() {
    // Set our cell values based on the LabRequest Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("nodeID"));
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("lastName"));
    requestedLabCol.setCellValueFactory(new TreeItemPropertyValueFactory("lab"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory("status"));

    // Get the current list of lab requests from the DAO
    ArrayList<LabRequest> currLabRequests =
        (ArrayList<LabRequest>) labRequestDao.getAllServiceRequests();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currLabRequests.isEmpty()) {
      table.setRoot(null);
    } else {
      // for each loop cycling through each lab request currently entered into the system
      for (LabRequest lab : currLabRequests) {
        TreeItem<LabRequest> item = new TreeItem(lab);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      table.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(labRequestDao.getAllServiceRequests().get(0));
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
              statusDropDown.getValue().toString());
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

  @Override
  public void start(Stage primaryStage) {}
}
