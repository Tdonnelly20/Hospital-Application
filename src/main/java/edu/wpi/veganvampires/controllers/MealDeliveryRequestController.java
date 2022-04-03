package edu.wpi.veganvampires.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.veganvampires.dao.MealRequestDao;
import edu.wpi.veganvampires.interfaces.RequestInterface;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.MealRequest;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MealDeliveryRequestController extends Controller implements RequestInterface {

  @FXML private TreeTableView<MealRequest> mealRequestTable;

  @FXML private TreeTableColumn<MealRequest, Integer> hospitalIDCol;
  @FXML private TreeTableColumn<MealRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<MealRequest, String> firstNameCol;
  @FXML private TreeTableColumn<MealRequest, String> lastNameCol;
  @FXML private TreeTableColumn<MealRequest, String> roomNumberCol;
  @FXML private TreeTableColumn<MealRequest, String> mealCol;
  @FXML private TreeTableColumn<MealRequest, String> allergyCol;
  @FXML private TreeTableColumn<MealRequest, String> otherInfoCol;

  @FXML private TextField patientID;
  @FXML private TextField hospitalID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomNum;
  @FXML private TextField allergy;
  @FXML private JFXComboBox<Object> mealDropDown;
  @FXML private Button sendRequest;
  @FXML private TextArea requestDetails;
  @FXML private Label statusLabel;

  // MUST take from Vdb, do NOT create
  private static MealRequestDao mealRequestDao = Vdb.mealRequestDao;

  /** Update the table with values from fields and the DB */
  @Override
  public void updateTreeTable() {
    // Set our cell values based on the MedicineDelivery Class, the Strings represent the actual
    // name of the variable we are adding to a specific column
    hospitalIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("hospitalID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    roomNumberCol.setCellValueFactory(new TreeItemPropertyValueFactory("roomNumber"));
    mealCol.setCellValueFactory(new TreeItemPropertyValueFactory("mealName"));
    allergyCol.setCellValueFactory(new TreeItemPropertyValueFactory("allergy"));
    otherInfoCol.setCellValueFactory(new TreeItemPropertyValueFactory("requestDetails"));

    // Get the current list of medicine deliveries from the DAO
    ArrayList<MealRequest> currMealDeliveries =
        (ArrayList<MealRequest>) mealRequestDao.getAllMealRequests();

    // Create a list for our tree items
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    // Need to make sure the list isn't empty
    if (!currMealDeliveries.isEmpty()) {

      // for each loop cycling through each medicine delivery currently entered into the system
      for (MealRequest delivery : currMealDeliveries) {
        TreeItem<MealRequest> item = new TreeItem(delivery);
        treeItems.add(item);
      }
      // VERY IMPORTANT: Because this is a Tree Table, we need to create a root, and then hide it so
      // we get the standard table functionality
      mealRequestTable.setShowRoot(false);
      // Root is just the first entry in our list
      TreeItem root = new TreeItem(mealRequestDao.getAllMealDeliveries().get(0));
      // Set the root in the table
      mealRequestTable.setRoot(root);
      // Set the rest of the tree items to the root, including the one we set as the root
      root.getChildren().addAll(treeItems);
    }
  }

  /** Determine whether or not all fields have been filled out, so we can submit the info */
  @FXML
  public void validateButton() {

    try {
      if ((hospitalID.getText().equals("")
          && patientID.getText().equals("")
          && firstName.getText().equals("")
          && lastName.getText().equals("")
          && roomNum.getText().equals("")
          && allergy.getText().equals("")
          && mealDropDown.getValue().equals("Select Meal"))) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Blank");
        statusLabel.setTextFill(Color.web("Black"));
      } else if ((hospitalID.getText().equals("")
          || patientID.getText().equals("")
          || firstName.getText().equals("")
          || lastName.getText().equals("")
          || roomNum.getText().equals("")
          || allergy.getText().equals("")
          || mealDropDown.getValue().equals("Select Meal"))) {
        sendRequest.setDisable(true);
        statusLabel.setText("Status: Processing");
        statusLabel.setTextFill(Color.web("Black"));
      } else {
        sendRequest.setDisable(false);
        statusLabel.setText("Status: Done");
      }
    } catch (Exception e) {
      sendRequest.setDisable(true);
    }
  }

  /** Determines if a meal delivery request is valid, and sends it to the Dao */
  @Override
  public void sendRequest() {
    // If any field is left blank, (except for request details) throw an error

    // Make sure the patient ID is an integer
    if (!isInteger(patientID.getText()) || !isInteger(hospitalID.getText())) {
      statusLabel.setText("Status: Failed. Patient/Hospital ID must be a number!");
      statusLabel.setTextFill(Color.web("Red"));

      // If all conditions pass, create the request
    } else {

      // Send the request to the Dao pattern
      mealRequestDao.addMealRequest(
          firstName.getText(),
          lastName.getText(),
          roomNum.getText(),
          Integer.parseInt(patientID.getText()),
          Integer.parseInt(hospitalID.getText()),
          mealDropDown.getValue().toString(),
          allergy.getText(),
          requestDetails.getText());

      // For testing purposes
      System.out.println(
          "\nHospital ID: "
              + hospitalID.getText()
              + "\nPatient ID: "
              + patientID.getText()
              + "\nRoom #: "
              + roomNum.getText()
              + "\nName: "
              + firstName.getText()
              + " "
              + lastName.getText()
              + "\nMedication: "
              + mealDropDown.getValue()
              + "\nAllergy: "
              + allergy.getText()
              + "\n\nRequest Details: "
              + requestDetails.getText());

      resetForm(); // Set all fields to blank for another entry
      updateTreeTable();
    }
  }

  /** Sets all the fields to their default value for another entry */
  @FXML
  public void resetForm() {
    patientID.setText("");
    hospitalID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomNum.setText("");
    allergy.setText("");

    mealDropDown.setValue("Select Meal");
    requestDetails.setText("");
    sendRequest.setDisable(true);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
