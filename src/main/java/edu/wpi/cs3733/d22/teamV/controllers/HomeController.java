package edu.wpi.cs3733.d22.teamV.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeController extends Controller {

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML private Text currTime;

  @FXML
  protected void switchToPatientDB(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/PatientDatabase.fxml"));
    switchScene(event);
  }

  @FXML
  protected void switchToEmployeeDB(ActionEvent event) {
    loader.setLocation(getClass().getClassLoader().getResource("FXML/EmployeeDatabase.fxml"));
    switchScene(event);
  }

  public String getDateAndTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy 'at' HH:mm:ss z");
    Date date = new Date(System.currentTimeMillis());
    return formatter.format(date);
  }

  @Override
  public void init() {
    long max = 0;
    // do {
    // currTime.setText("  " + getDateAndTime());
    // max = Long.MAX_VALUE;
    // } while (System.currentTimeMillis() < max);
  }
}
