package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AboutController extends RequestController {
  @Override
  public void start(Stage primaryStage) throws Exception {}

  @FXML private Label labelM;
  @FXML private Label labelM1;
  @FXML private Label labelM2;
  @FXML private Label labelM3;
  @FXML private Label labelM4;
  @FXML private Label labelM5;
  @FXML private Label labelM6;
  @FXML private Pane paneM;
  private boolean visibleM = false;

  @FXML private Label labelT;
  @FXML private Label labelT1;
  @FXML private Label labelT2;
  @FXML private Label labelT3;
  @FXML private Label labelT4;
  @FXML private Label labelT5;
  @FXML private Label labelT6;
  @FXML private Pane paneT;
  private boolean visibleT = false;

  @FXML private Label labelJ;
  @FXML private Label labelJ1;
  @FXML private Label labelJ2;
  @FXML private Label labelJ3;
  @FXML private Label labelJ4;
  @FXML private Label labelJ5;
  @FXML private Label labelJ6;
  @FXML private Pane paneJ;
  private boolean visibleJ = false;

  @Override
  public void init() {
    setTitleText("About");

    labelM.setVisible(false);
    labelM1.setVisible(false);
    labelM2.setVisible(false);
    labelM3.setVisible(false);
    labelM4.setVisible(false);
    labelM5.setVisible(false);
    labelM6.setVisible(false);
    paneM.setVisible(false);

    labelT.setVisible(false);
    labelT1.setVisible(false);
    labelT2.setVisible(false);
    labelT3.setVisible(false);
    labelT4.setVisible(false);
    labelT5.setVisible(false);
    labelT6.setVisible(false);
    paneT.setVisible(false);

    labelJ.setVisible(false);
    labelJ1.setVisible(false);
    labelJ2.setVisible(false);
    labelJ3.setVisible(false);
    labelJ4.setVisible(false);
    labelJ5.setVisible(false);
    labelJ6.setVisible(false);
    paneJ.setVisible(false);

    fillTopPane();
  }

  @Override
  void updateTreeTable() {}

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}

  @FXML
  private void showInfoMatt() {
    visibleM = !visibleM;
    labelM.setVisible(visibleM);
    labelM1.setVisible(visibleM);
    labelM2.setVisible(visibleM);
    labelM3.setVisible(visibleM);
    labelM4.setVisible(visibleM);
    labelM5.setVisible(visibleM);
    labelM6.setVisible(visibleM);
    paneM.setVisible(visibleM);
  }

  @FXML
  private void showInfoTate() {
    visibleT = !visibleT;
    labelT.setVisible(visibleT);
    labelT1.setVisible(visibleT);
    labelT2.setVisible(visibleT);
    labelT3.setVisible(visibleT);
    labelT4.setVisible(visibleT);
    labelT5.setVisible(visibleT);
    labelT6.setVisible(visibleT);
    paneT.setVisible(visibleT);
  }

  @FXML
  private void showInfoJason() {
    visibleJ = !visibleJ;
    labelJ.setVisible(visibleJ);
    labelJ1.setVisible(visibleJ);
    labelJ2.setVisible(visibleJ);
    labelJ3.setVisible(visibleJ);
    labelJ4.setVisible(visibleJ);
    labelJ5.setVisible(visibleJ);
    labelJ6.setVisible(visibleJ);
    paneJ.setVisible(visibleJ);
  }
}
