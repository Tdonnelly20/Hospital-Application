package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.ReligiousRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReligiousRequestController extends RequestController {
  @FXML private TextField Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField religion;
  @FXML private TextField roomNumber;
  @FXML private Button sendRequest;
  @FXML private Label statusLabel;

  @FXML private TreeTableView<ReligiousRequest> ReligiousRequestTable;
  @FXML private TreeTableColumn<ReligiousRequest, Integer> empIDCol;
  @FXML private TreeTableColumn<ReligiousRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> firstNameCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> lastNameCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> nodeCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> religionCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> requestDetailsCol;

  private static class SingletonHelper {
    private static final ReligiousRequestController controller = new ReligiousRequestController();
  }

  public static ReligiousRequestController getController() {
    return ReligiousRequestController.SingletonHelper.controller;
  }

  private static final LocationDao LocationDao =
      (LocationDao) Vdb.requestSystem.getDao(RequestSystem.Dao.LocationDao);

  @Override
  public void init() {
    setTitleText("Religious Request Service");
    fillTopPane();
    mapSetUp();
    filterCheckBox.getCheckModel().check("Religious Requests Service");
    validateButton();
  }

  @Override
  void updateTreeTable() {}

  @FXML
  void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    roomNumber.setText("");
    religion.setText("");
    sendRequest.setDisable(true);
  }

  // Checks to see if the user can submit info
  @FXML
  void validateButton() {
    sendRequest.setDisable(true);
    boolean valid = true;
    String issues = "Issues:\n";
    // Information verification and submission needed
    if (!isInteger(patientID.getText()) || patientID.getText().isEmpty()) {
      issues += "Invalid Patient.\n";
      valid = false;
    }
    if (!isInteger(userID.getText()) || userID.getText().isEmpty()) {
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
    if (valid) {
      issues = "";
      sendRequest.setDisable(false);
    }
    statusLabel.setText(issues);
  }

  @Override
  public void start(Stage primaryStage) {}
}
