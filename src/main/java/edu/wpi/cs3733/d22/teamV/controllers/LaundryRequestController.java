package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.dao.LaundryRequestDao;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem.Dao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.LaundryRequest;
import java.awt.*;
import java.sql.SQLException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LaundryRequestController extends MapController {

  @FXML private Label Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomNumber;
  @FXML private TextArea details;
  @FXML private JFXComboBox statusDropDown;
  @FXML private Button sendRequest;

  private static class SingletonHelper {
    private static final LaundryRequestController controller = new LaundryRequestController();
  }

  public static LaundryRequestController getController() {
    return LaundryRequestController.SingletonHelper.controller;
  }

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Laundry Requests");
  }

  // TODO
  // laundryRequestDAO is not in VDB
  // fix database connection for add / remove Laundry Request

  private static final LaundryRequestDao laundryRequestDao =
      (LaundryRequestDao) Vdb.requestSystem.getDao(Dao.LaundryRequest);

  @FXML
  private void resetForm() {
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomNumber.setText("");
    details.setText("");
    statusDropDown.setValue("Status");
    Status.setText("Status: Blank");
    sendRequest.setDisable(true);
    System.out.println("reset form");
  }

  // Checks to see if the user can submit info
  @FXML
  private void validateButton() {
    if ((!userID.getText().isEmpty())
        && !(patientID.getText().isEmpty())
        && !(firstName.getText().isEmpty())
        && !(lastName.getText().isEmpty())
        && !(roomNumber.getText().isEmpty())) {
      // Information verification and submission needed
      Status.setText("Status: Done");
      sendRequest.setDisable(false);

    } else if ((userID.getText().isEmpty())
        || (patientID.getText().isEmpty())
        || (firstName.getText().isEmpty())
        || (lastName.getText().isEmpty())
        || (roomNumber.getText().isEmpty())) {
      sendRequest.setDisable(true);
      Status.setText("Status: Processing");

    } else {
      sendRequest.setDisable(true);
      Status.setText("Status: Blank");
    }
  }

  @FXML
  private void sendRequest() throws SQLException {
    LaundryRequest l =
        new LaundryRequest(
            Integer.parseInt(userID.getText()),
            Integer.parseInt(patientID.getText()),
            roomNumber.getText(),
            details.getText());
    try {
      laundryRequestDao.addServiceRequest(l);
    } catch (Exception e) {
      e.printStackTrace();
    }
    resetForm();
  }

  @Override
  public void start(Stage primaryStage) {}

  public void updateTreeTable(Event event) {}

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
