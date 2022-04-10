package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.EquipmentDeliveryDao;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.serviceRequest.EquipmentDelivery;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class EquipmentRequestController extends MapController {
  @FXML private TreeTableView<EquipmentDelivery> equipmentRequestTable;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> patientIDCol;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> employeeIDCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> firstNameCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> lastNameCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> posCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> equipCol;
  @FXML private TreeTableColumn<EquipmentDelivery, Integer> quantCol;
  @FXML private TreeTableColumn<EquipmentDelivery, String> notesCol;

  @FXML private TextField patientID;
  @FXML private TextField employeeID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private Label status;
  @FXML private TextField pos;
  @FXML private JFXComboBox<Object> dropDown;
  @FXML private TextField quant;
  @FXML private TextArea notes;
  @FXML private Button sendRequest;

  private static EquipmentDeliveryDao equipmentDeliveryDao = Vdb.equipmentDeliveryDao;

  private static LocationDao locationDao = Vdb.locationDao;

  @FXML private TreeTableView<Location> table;
  @FXML private TreeTableColumn<Location, String> nodeIDCol;
  @FXML private TreeTableColumn<Location, Integer> xCol;
  @FXML private TreeTableColumn<Location, Integer> yCol;
  @FXML private TreeTableColumn<Location, String> floorCol;
  @FXML private TreeTableColumn<Location, String> buildingCol;
  @FXML private TreeTableColumn<Location, String> nodeTypeCol;
  @FXML private TreeTableColumn<Location, Boolean> shortNameCol;

  private static class SingletonHelper {
    private static final EquipmentRequestController controller = new EquipmentRequestController();
  }

  public static EquipmentRequestController getController() {
    return EquipmentRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Equipment");
    filterCheckBox.getCheckModel().check("Clean Equipment");
    filterCheckBox.getCheckModel().check("Equipment Delivery Requests");
  }

  @FXML
  private void updateTreeTable() {
    employeeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientFirstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("patientLastName"));
    posCol.setCellValueFactory(new TreeItemPropertyValueFactory("locationName"));
    equipCol.setCellValueFactory(new TreeItemPropertyValueFactory("equipment"));
    quantCol.setCellValueFactory(new TreeItemPropertyValueFactory("quantity"));
    notesCol.setCellValueFactory(new TreeItemPropertyValueFactory("notes"));

    ArrayList<EquipmentDelivery> currEquipmentDeliveries =
        (ArrayList<EquipmentDelivery>) equipmentDeliveryDao.getAllServiceRequests();

    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currEquipmentDeliveries.isEmpty()) {

      for (EquipmentDelivery delivery : currEquipmentDeliveries) {
        TreeItem<EquipmentDelivery> item = new TreeItem(delivery);
        treeItems.add(item);
      }
      equipmentRequestTable.setShowRoot(false);
      TreeItem root = new TreeItem(currEquipmentDeliveries.get(0));
      equipmentRequestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void updateEquipmentTable() {
    nodeIDCol.setCellValueFactory(new TreeItemPropertyValueFactory("ID"));
    xCol.setCellValueFactory(new TreeItemPropertyValueFactory("x"));
    yCol.setCellValueFactory(new TreeItemPropertyValueFactory("y"));
    floorCol.setCellValueFactory(new TreeItemPropertyValueFactory("floor"));
    buildingCol.setCellValueFactory(new TreeItemPropertyValueFactory("name"));
    nodeTypeCol.setCellValueFactory(new TreeItemPropertyValueFactory("description"));
    shortNameCol.setCellValueFactory(new TreeItemPropertyValueFactory("isDirty"));

    ArrayList<Equipment> currEquipment = (ArrayList<Equipment>) Vdb.equipmentDao.getAllEquipment();
    ArrayList<TreeItem> treeItems = new ArrayList<>();

    if (!currEquipment.isEmpty()) {

      for (Equipment pos : currEquipment) {
        TreeItem<Equipment> item = new TreeItem(pos);
        treeItems.add(item);
      }

      table.setShowRoot(false);
      TreeItem root = new TreeItem(locationDao.getAllLocations().get(0));
      table.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void resetForm() {
    employeeID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    status.setText("Status: Blank");
    pos.setText("");
    notes.setText("");
    quant.setText("");
    dropDown.setValue(null);
    sendRequest.setDisable(true);
  }

  @FXML
  private void validateButton() {
    if (!(employeeID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(employeeID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(pos.getText().isEmpty())
        && !(dropDown.getValue() == null)
        && !(notes.getText().isEmpty())
        && !(quant.getText().isEmpty())
        && isInteger(quant.getText())) {
      status.setText("Status: Done");
      sendRequest.setDisable(false);

    } else if (!(employeeID.getText().isEmpty())
        || !(patientID.getText().isEmpty())
        || !(employeeID.getText().isEmpty())
        || !(firstName.getText().isEmpty())
        || !(lastName.getText().isEmpty())
        || !(status.getText().isEmpty())
        || !(pos.getText().isEmpty())
        || !(dropDown.getValue() == null)
        || !(notes.getText().isEmpty())
        || !(quant.getText().isEmpty())) {
      status.setText("Status: Processing");
      sendRequest.setDisable(true);

      if (!isInteger(quant.getText()) && !quant.getText().isEmpty()) {
        status.setText("Status: Quantity is not a number");
      }

    } else {
      status.setText("Status: Blank");
      sendRequest.setDisable(true);
    }
  }

  @FXML
  private void sendRequest() throws SQLException {

    EquipmentDelivery delivery =
        new EquipmentDelivery(
            Integer.parseInt(employeeID.getText()),
            Integer.parseInt(patientID.getText()),
            firstName.getText(),
            lastName.getText(),
            pos.getText(),
            dropDown.getValue().toString(),
            notes.getText(),
            Integer.parseInt(quant.getText()),
            status.getText());

    try {
      equipmentDeliveryDao.addServiceRequest(delivery);
    } catch (Exception e) {

    }

    resetForm();
    updateTreeTable();
  }

  @Override
  public void start(Stage primaryStage) {}

  // used to get coordinates after clicking map
  @FXML private TextArea coordinates;
  private Point point = new Point();
  private int xCoord, yCoord;

  @FXML
  private void mapCoordTracker() {
    point = MouseInfo.getPointerInfo().getLocation();
    xCoord = point.x - 712;
    yCoord = point.y - 230;
    coordinates.setText("X: " + xCoord + " Y: " + yCoord);
  }
}
