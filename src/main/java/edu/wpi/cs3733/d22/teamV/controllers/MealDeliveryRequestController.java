package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.MealRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.MealRequest;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class MealDeliveryRequestController extends RequestController {
  @FXML private TreeTableView<MealRequest> table;
  @FXML private TreeTableColumn<MealRequest, Integer> userIDCol;
  @FXML private TreeTableColumn<MealRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<MealRequest, String> firstNameCol;
  @FXML private TreeTableColumn<MealRequest, String> lastNameCol;
  @FXML private TreeTableColumn<MealRequest, String> requestedMealCol;

  private static final MealRequestDao mealRequestDao =
      (MealRequestDao) Vdb.requestSystem.getDao(Dao.MealRequestDao);
  @FXML private TextField Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private JFXComboBox<Object> requestedMeal;
  @FXML private Button sendRequest;

  @Override
  public void init() {
    setTitleText("Meal Delivery Service");
    fillTopPane();
    mapSetUp();
    filterCheckBox.getCheckModel().check("Meal Delivery Requests");
  }

  @FXML
  void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    requestedMeal.setValue("Select Meal");
    sendRequest.setDisable(true);
  }

  // Checks to see if the user can submit info
  @FXML
  void validateButton() {
    try {
      if (!(userID.getText().isEmpty())
          && !(patientID.getText().isEmpty())
          && !(firstName.getText().isEmpty())
          && !(lastName.getText().isEmpty())
          && !(requestedMeal.getValue().equals("Select Meal"))) {
        // Information verification and submission needed
        sendRequest.setDisable(false);
        Status.setText("Status: Done");
      } else if (!(userID.getText().isEmpty())
          || !(patientID.getText().isEmpty())
          || !(firstName.getText().isEmpty())
          || !(lastName.getText().isEmpty())
          || !(requestedMeal.getValue().equals("Select Meal"))) {
        Status.setText("Status: Processing");
      } else {
        Status.setText("Status: Blank");
        sendRequest.setDisable(true);
      }
    } catch (NullPointerException e) {

    }
  }

  /** Runs whenever we switch to the table, or update a value */
  @FXML
  void updateTreeTable() {
    // Set our cell values based on the LabRequest Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    userIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("userID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("lastName"));
    requestedMealCol.setCellValueFactory(new TreeItemPropertyValueFactory("meal"));

    // Get the current list of lab requests from the DAO
    ArrayList<MealRequest> currMealRequests =
        (ArrayList<MealRequest>) mealRequestDao.getAllServiceRequests();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (!currMealRequests.isEmpty()) {

      // for each loop cycling through each lab request currently entered into the system
      for (MealRequest meal : currMealRequests) {
        TreeItem<MealRequest> item = new TreeItem(meal);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      table.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(mealRequestDao.getAllServiceRequests().get(0));
      // Set the root in the table
      table.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
      resetForm();
    }
  }

  @FXML
  private void sendRequest() {
    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(userID.getText())) {
      Status.setText("Status: Failed. Patient/Hospital ID must be a number!");

      // If all conditions pass, create the request
    } else {
      // Send the request to the Dao pattern
      System.out.println(requestedMeal.getValue().toString());
      MealRequest m =
          new MealRequest(
              Integer.parseInt(userID.getText()),
              Integer.parseInt(patientID.getText()),
              "test",
              requestedMeal.getValue().toString());
      try {
        mealRequestDao.addServiceRequest(m);
      } catch (Exception e) {
        e.printStackTrace();
      }
      resetForm();
      updateTreeTable();
    }
  }

  @Override
  public void start(Stage primaryStage) {}
}
