package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class HomeController extends Controller {

  @Override
  public void start(Stage primaryStage) throws Exception {}

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
}
