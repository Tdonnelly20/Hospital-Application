package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.stage.Stage;

public class HomeController extends RequestController {

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @Override
  public void init() {
    setTitleText("Home");
    fillTopPane();
  }

  @Override
  void updateTreeTable() {}

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}
}
