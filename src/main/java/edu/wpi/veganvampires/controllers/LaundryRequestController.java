package edu.wpi.veganvampires.controllers;

import edu.wpi.veganvampires.dao.LaundryRequestDao;
import java.awt.*;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LaundryRequestController extends Controller {

  @FXML private Label Status;
  @FXML private TextField userID;
  @FXML private TextField patientID;
  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField roomNumber;
  @FXML private TextArea details;
  @FXML private Button sendRequest;

  // TODO
  // laundryRequestDAO is not in VDB
  // fix database connection for add / remove Laundry Request

  private static LaundryRequestDao laundryRequestDao;
  // = Vdb.laundryRequestDao;

  @FXML
  private void resetForm() {
    Status.setText("Status: Blank");
    userID.setText("");
    patientID.setText("");
    firstName.setText("");
    lastName.setText("");
    roomNumber.setText("");
    sendRequest.setDisable(true);
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

  private void sendRequest() throws SQLException {
    laundryRequestDao.addLaundryRequest(
        userID.getText(),
        patientID.getText(),
        firstName.getText(),
        lastName.getText(),
        Integer.parseInt(roomNumber.getText()),
        details.getText());
    resetForm();
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
