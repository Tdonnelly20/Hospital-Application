package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ServiceRequestController extends Controller {

  @FXML private Label labelM;
  @FXML private Label labelD;
  @FXML private Label labelP;
  @FXML private Label labelJ;
  @FXML private Label labelA;
  @FXML private Label labelJo;
  private boolean visible = true;

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
  }
}
