package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.dao.EmployeeDao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EmployeeController extends MapController {

  private static final EmployeeDao employeeDao = (EmployeeDao) Vdb.requestSystem.getEmployeeDao();
  private boolean updating = false;

  private static class SingletonHelper {
    private static final EmployeeController controller = new EmployeeController();
  }

  public static EmployeeController getController() {
    return EmployeeController.SingletonHelper.controller;
  }

  @FXML private TreeTableView<Employee> table;
  @FXML private TreeTableColumn<Employee, Integer> employeeIDCol;
  @FXML private TreeTableColumn<Employee, String> firstNameCol;
  @FXML private TreeTableColumn<Employee, String> lastNameCol;
  @FXML private TreeTableColumn<Employee, String> employeePositionCol;
  @FXML private TreeTableColumn<Employee, String> patientIDCol;
  @FXML private TreeTableColumn<Employee, String> specialtiesCol;
  @FXML private TreeTableColumn<Employee, String> serviceRequestIDCol;

  @FXML private TextField employeeFirstName;
  @FXML private TextField employeeLastName;
  @FXML private TextField employeePosition;
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
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientIDs"));
    employeePositionCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeePosition"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("lastName"));
    specialtiesCol.setCellValueFactory(new TreeItemPropertyValueFactory("specialties"));
    serviceRequestIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("serviceRequestIDs"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<Employee> currEmployees = (ArrayList<Employee>) employeeDao.getAllEmployees();
    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (currEmployees.isEmpty()) {
      table.setRoot(null);
      return;
    }

    // for each loop cycling through each medicine delivery currently entered into the system
    for (Employee delivery : currEmployees) {
      TreeItem<Employee> item = new TreeItem(delivery);
      treeItems.add(item);
    }
    // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
    // we get the standard table functionality
    table.setShowRoot(false);
    // Root is just the first entry in our list
    TreeItem root = new TreeItem(currEmployees.get(0));
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
      if ((employeeFirstName.getText().equals("")
          && employeeLastName.getText().equals("")
          && employeePosition.getText().equals(""))) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Blank");
        statusLabel.setTextFill(Color.web("Black"));
      } else if ((employeeFirstName.getText().equals("")
          || employeeLastName.getText().equals("")
          || employeePosition.getText().equals(""))) {
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
      Employee employee =
          new Employee(
              Vdb.requestSystem.getEmployeeID(),
              employeeFirstName.getText(),
              employeeLastName.getText(),
              employeePosition.getText(),
              new ArrayList<String>(),
              new ArrayList<Integer>(),
              new ArrayList<Integer>(),
              false);
      // Send the request to the Dao pattern
      try {
        if (updating) {
          employeeDao.updateEmployee(employee, Vdb.requestSystem.getEmployeeID());
          updating = false;
        } else {
          employeeDao.addEmployee(employee);
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
    employeeFirstName.setText("");
    employeeLastName.setText("");
    employeePosition.setText("");
    requestDetails.setText("");
    sendRequest.setDisable(true);
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      Employee employee = table.getSelectionModel().getSelectedItem().getValue();
      employeeDao.removeEmployee(employee);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  @FXML
  private void updateSelectedRow() throws IOException, NullPointerException, SQLException {
    updating = true;
    Employee employee = table.getSelectionModel().getSelectedItem().getValue();

    employeeFirstName.setText(String.valueOf(employee.getFirstName()));
    employeeLastName.setText(String.valueOf(employee.getLastName()));
    employeePosition.setText(employee.getEmployeePosition());
    updateTreeTable();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
