package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.dao.LabRequestDao;
import edu.wpi.veganvampires.interfaces.RequestInterface;
import edu.wpi.veganvampires.objects.LabRequest;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class LabRequestController extends Controller implements RequestInterface {
  @FXML private TreeTableView<LabRequest> table;
  @FXML private TreeTableColumn<LabRequest, Integer> userIDCol;
  @FXML private TreeTableColumn<LabRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<LabRequest, String> firstNameCol;
  @FXML private TreeTableColumn<LabRequest, String> lastNameCol;
  @FXML private TreeTableColumn<LabRequest, String> requestedLabCol;
  @FXML private TreeTableColumn<LabRequest, String> statusCol;

  private static LabRequestDao labRequestDao = new LabRequestDao();
  @FXML private TextField Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private JFXComboBox<Object> requestedLab;
  @FXML private Button sendRequest;

  @Override
  @FXML
  public void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    requestedLab.setValue("Select Lab");
    sendRequest.setDisable(true);
  }

  // Checks to see if the user can submit info
  @Override
  public void validateButton() {
    if (!(userID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(requestedLab.getValue().equals("Select Lab"))) {
      // Information verification and submission needed
      sendRequest.setDisable(false);
      Status.setText("Status: Done");
    } else if (!(userID.getText().isEmpty())
        || !(patientID.getText().isEmpty())
        || !(firstName.getText().isEmpty())
        || !(lastName.getText().isEmpty())
        || !(requestedLab.getValue().equals("Select Lab"))) {
      Status.setText("Status: Processing");
    } else {
      Status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }

  /** Runs whenever we switch to the table, or update a value */
  @Override
  public void updateTreeTable() {
    System.out.println("Here");
    // Set our cell values based on the LabRequest Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    userIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("userID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("lastName"));
    requestedLabCol.setCellValueFactory(new TreeItemPropertyValueFactory("lab"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory("status"));

    // Get the current list of lab requests from the DAO
    ArrayList<LabRequest> currLabRequests =
        (ArrayList<LabRequest>) labRequestDao.getAllLabRequests();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (!currLabRequests.isEmpty()) {

      // for each loop cycling through each lab request currently entered into the system
      for (LabRequest lab : currLabRequests) {
        TreeItem<LabRequest> item = new TreeItem(lab);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      table.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(labRequestDao.getAllLabRequests().get(0));
      // Set the root in the table
      table.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
      resetForm();
    }
  }

  @Override
  public void sendRequest() {
    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(userID.getText())) {
      Status.setText("Status: Failed. Patient/Hospital ID must be a number!");

      // If all conditions pass, create the request
    } else {
      // Send the request to the Dao pattern
      System.out.println(requestedLab.getValue().toString());
      labRequestDao.addLabRequest(
          Integer.parseInt(userID.getText()),
          Integer.parseInt(patientID.getText()),
          firstName.getText(),
          lastName.getText(),
          requestedLab.getValue().toString(),
          "Processing");
      resetForm();
      updateTreeTable();
    }
  }

  @Override
  public void start(Stage primaryStage) {}
}
