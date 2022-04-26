package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServiceRequestController extends RequestController {

  @FXML private Label labelM;
  @FXML private Label labelD;
  @FXML private Label labelP;
  @FXML private Label labelJ;
  @FXML private Label labelA;
  @FXML private Label labelJo;
  @FXML private Label labelJa;
  @FXML private Label labelD2;
  private boolean visible = true;
  private boolean visible2 = false;

  @FXML private Label l1;
  @FXML private Label l2;

  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML
  private void hideLabels() {
    visible = !visible;
    labelM.setVisible(visible);
    labelD.setVisible(visible);
    labelP.setVisible(visible);
    labelJ.setVisible(visible);
    labelA.setVisible(visible);
    labelJo.setVisible(visible);
    labelJa.setVisible(visible);
    labelD2.setVisible(visible);
  }

  @FXML
  private void clickMe() {
    visible2 = !visible2;
    l1.setVisible(visible2);
    l2.setVisible(visible2);
  }

  @Override
  public void init() {
    setTitleText("Services");
    fillTopPane();
  }

  @Override
  void updateTreeTable() {}

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}
}
