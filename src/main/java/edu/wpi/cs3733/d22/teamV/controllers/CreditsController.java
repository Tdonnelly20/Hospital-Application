package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.stage.Stage;

public class CreditsController extends RequestController {

  @Override
  public void init() {
    setTitleText("Credits");
    fillTopPane();
  }

  @Override
  void updateTreeTable() {}

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}

  @Override
  public void start(Stage primaryStage) throws Exception {}
}
