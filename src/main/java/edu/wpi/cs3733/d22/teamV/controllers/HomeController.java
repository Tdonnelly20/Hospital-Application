package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeController extends Controller {

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  private Text currTime;

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
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date date = new Date(System.currentTimeMillis());
    return formatter.format(date);
  }

  @Override
  public void init() {
    while(true){
      currTime.setText(getDateAndTime());
      if(System.currentTimeMillis() >= 2147483647) break;
    }
  }
}
