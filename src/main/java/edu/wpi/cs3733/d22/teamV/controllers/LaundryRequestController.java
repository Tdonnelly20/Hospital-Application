package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LaundryRequestDao;
import edu.wpi.cs3733.d22.teamV.dao.LocationDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.LaundryRequest;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LaundryRequestController extends RequestController {

  @FXML private Label status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField roomNumber;
  @FXML private TextArea details;
  @FXML private JFXComboBox statusDropDown;
  @FXML private Button sendRequest;

  @FXML private TreeTableView<LaundryRequest> requestTable;
  @FXML private TreeTableColumn<LaundryRequest, Integer> userIDCol;
  @FXML private TreeTableColumn<LaundryRequest, Integer> patientIDCol;
  @FXML private TreeTableColumn<LaundryRequest, String> firstNameCol;
  @FXML private TreeTableColumn<LaundryRequest, String> lastNameCol;
  @FXML private TreeTableColumn<LaundryRequest, String> locationCol;
  @FXML private TreeTableColumn<LaundryRequest, String> detailsCol;
  @FXML private TreeTableColumn<LaundryRequest, String> statusCol;
  @FXML private Pane tablePane;

  private boolean updating = false;
  private int updateServiceID;

  private static class SingletonHelper {
    private static final LaundryRequestController controller = new LaundryRequestController();
  }

  private static final LocationDao LocationDao = Vdb.requestSystem.getLocationDao();

  public static LaundryRequestController getController() {
    return LaundryRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    setTitleText("Laundry Request");
    fillTopPane();

    setColumnSizes(910);

    tablePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = tablePane.getWidth();
                requestTable.setPrefWidth(w - 30);
                setColumnSizes(w);
              }
            });

    tablePane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double h = tablePane.getHeight();
                requestTable.setPrefHeight(h - 75);
              }
            });
  }

  private static final LaundryRequestDao laundryRequestDao =
      (LaundryRequestDao) Vdb.requestSystem.getDao(Dao.LaundryRequest);

  @FXML
  void resetForm() {
    userID.setText("");
    patientID.setText("");
    roomNumber.setText("");
    details.setText("");
    statusDropDown.setValue("Status");
    status.setText("Status: Blank");
    sendRequest.setDisable(true);
    sendRequest.setText("Send Request");
  }

  // Checks to see if the user can submit info
  @FXML
  void validateButton() {
    if ((!userID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && (LocationDao.getLocation(roomNumber.getText()) != null)
        && (statusDropDown.getValue() != null)) {
      // Information verification and submission needed
      status.setText("Status: Done");
      sendRequest.setDisable(false);

    } else if ((userID.getText().isEmpty())
        || (patientID.getText().isEmpty())
        || (roomNumber.getText().isEmpty())
        || (statusDropDown.getValue() == null)) {
      sendRequest.setDisable(true);
      status.setText("Status: Processing");

    } else if (LocationDao.getLocation(roomNumber.getText()) == null) {
      sendRequest.setDisable(true);
      status.setText("Status: Needs valid room");
    } else {
      sendRequest.setDisable(true);
      status.setText("Status: Blank");
    }
  }

  @FXML
  private void sendRequest() {
    LaundryRequest l =
        new LaundryRequest(
            Integer.parseInt(userID.getText()),
            Integer.parseInt(patientID.getText()),
            roomNumber.getText(),
            details.getText(),
            statusDropDown.getValue().toString());
    try {
      if (updating) {
        Vdb.requestSystem.getDao(Dao.LaundryRequest).updateServiceRequest(l, updateServiceID);
        updating = false;
      } else {
        RequestSystem.getSystem().addServiceRequest(l, Dao.LaundryRequest);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    resetForm();
    updateTreeTable();
  }

  public void updateTreeTable() {
    userIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("employeeID"));
    patientIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("patientID"));
    firstNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstName"));
    lastNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastName"));
    locationCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("locationID"));
    detailsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("details"));
    statusCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));

    ArrayList<LaundryRequest> currLaundryRequest =
        (ArrayList<LaundryRequest>)
            RequestSystem.getSystem().getAllServiceRequests(Dao.LaundryRequest);

    ArrayList<TreeItem<LaundryRequest>> treeItems = new ArrayList<>();

    if (currLaundryRequest.isEmpty()) {
      requestTable.setRoot(null);
    } else {
      for (LaundryRequest request : currLaundryRequest) {
        TreeItem<LaundryRequest> item = new TreeItem<>(request);
        treeItems.add(item);
      }
      requestTable.setShowRoot(false);
      TreeItem<LaundryRequest> root = new TreeItem<>(currLaundryRequest.get(0));
      requestTable.setRoot(root);
      root.getChildren().addAll(treeItems);
    }
  }

  @FXML
  private void updateSelectedRow() throws NullPointerException {
    updating = true;
    LaundryRequest l = requestTable.getSelectionModel().getSelectedItem().getValue();

    userID.setText(String.valueOf(l.getEmployeeID()));
    patientID.setText(String.valueOf(l.getPatientID()));
    roomNumber.setText(l.getLocationID());
    details.setText(l.getDetails());
    statusDropDown.setValue(l.getStatus());
    updateServiceID = l.getServiceID();
    sendRequest.setText("Update");
    updateTreeTable();
  }

  @FXML
  private void removeSelectedRow() throws IOException, NullPointerException, SQLException {
    try {
      LaundryRequest l = requestTable.getSelectionModel().getSelectedItem().getValue();
      RequestSystem.getSystem().getDao(Dao.LaundryRequest).removeServiceRequest(l);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    updateTreeTable();
  }

  void setColumnSizes(double w) {
    setColumnSize(userIDCol, (w - 30) / 7);
    setColumnSize(patientIDCol, (w - 30) / 7);
    setColumnSize(firstNameCol, (w - 30) / 7);
    setColumnSize(lastNameCol, (w - 30) / 7);
    setColumnSize(locationCol, (w - 30) / 7);
    setColumnSize(detailsCol, (w - 30) / 7);
    setColumnSize(statusCol, (w - 30) / 7);
  }

  @Override
  public void start(Stage primaryStage) {}
}
