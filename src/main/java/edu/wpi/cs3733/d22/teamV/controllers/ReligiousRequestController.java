package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.dao.ReligiousRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.ReligiousRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class ReligiousRequestController extends RequestController {
  @FXML private TextField employeeID;
  @FXML private TextField patientID;
  @FXML private TextField religion;
  @FXML private TextField roomNumber;
  @FXML private TextField details;
  @FXML private Button sendRequest;
  @FXML private Label statusLabel;

  @FXML private TreeTableView<ReligiousRequest> ReligiousRequestTable;
  @FXML private TreeTableColumn<ReligiousRequest, Integer> employeeIDCol;
  @FXML private TreeTableColumn<ReligiousRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> firstNameCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> lastNameCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> roomCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> religionCol;
  @FXML private TreeTableColumn<ReligiousRequest, String> requestDetailsCol;
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

  @Override
  public void init() {
    setTitleText("Religious Request Servic" + "e");
    updateTreeTable();
    fillTopPane();
    // mapSetUp();
    updating = false;
    // filterCheckBox.getCheckModel().check("Religious Requests Service");
    validateButton();
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
        new TreeItemPropertyValueFactory("employeeID")); // issue, but it matches textfield
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    roomCol.setCellValueFactory(new TreeItemPropertyValueFactory("roomNumber"));
    religionCol.setCellValueFactory(new TreeItemPropertyValueFactory("religion"));
    requestDetailsCol.setCellValueFactory(new TreeItemPropertyValueFactory("details"));
    ArrayList<ReligiousRequest> requests =
        (ArrayList<ReligiousRequest>)
            RequestSystem.getSystem().getAllServiceRequests(RequestSystem.Dao.ReligiousRequest);
    ArrayList<TreeItem> treeItems = new ArrayList<>();
    // TreeItemPropertyVvalueFactory claims unable to retrieve property
    if (requests.isEmpty()) {
      ReligiousRequestTable.setRoot(null);
    } else {
      for (ReligiousRequest r : requests) {
        System.out.println(
            "details " + r.getLocation().getNodeID()); // so the request is fully made,
        TreeItem<ReligiousRequest> item = new TreeItem(r);
        treeItems.add(item);
      }
      ReligiousRequestTable.setShowRoot(false);
      TreeItem root = new TreeItem(requests.get(0));
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
    validateButton();
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
    if (!isInteger(employeeID.getText()) || employeeID.getText().isEmpty()) {
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

  @FXML
  void sendRequest()
      throws SQLException, IOException { // must check to see if its updating or new req
    ReligiousRequest request =
        new ReligiousRequest(
            Integer.parseInt(patientID.getText()),
            Integer.parseInt(employeeID.getText()),
            roomNumber.getText(),
            religion.getText(),
            details.getText());
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

      employeeID.setText(String.valueOf(request.getEmpID()));
      patientID.setText(String.valueOf(request.getPatientID()));
      religion.setText(request.getReligion());
      roomNumber.setText(request.getLocation().getNodeID());
      details.setText(request.getDetails());
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
