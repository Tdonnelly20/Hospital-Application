package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.stage.Stage;

public class ServiceRequestController extends MapController {

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @Override
  public void init() {
    mapSetUp();
    filterCheckBox.getCheckModel().check("Service Requests");
  }
}
