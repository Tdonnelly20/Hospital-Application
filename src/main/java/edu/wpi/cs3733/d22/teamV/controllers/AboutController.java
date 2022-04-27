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
  @FXML private Label labelJ7;
  @FXML private Pane paneJ;
  private boolean visibleJ = false;

  @FXML private Label labelJA;
  @FXML private Label labelJA1;
  @FXML private Label labelJA2;
  @FXML private Label labelJA3;
  @FXML private Label labelJA4;
  @FXML private Label labelJA5;
  @FXML private Label labelJA6;
  @FXML private Pane paneJA;
  private boolean visibleJA = false;

  @FXML private Label labelB;
  @FXML private Label labelB1;
  @FXML private Label labelB2;
  @FXML private Label labelB3;
  @FXML private Label labelB4;
  @FXML private Label labelB5;
  @FXML private Label labelB6;
  @FXML private Pane paneB;
  private boolean visibleB = false;

  @FXML private Label labelA;
  @FXML private Label labelA1;
  @FXML private Label labelA2;
  @FXML private Label labelA3;
  @FXML private Label labelA4;
  @FXML private Label labelA5;
  @FXML private Label labelA6;
  @FXML private Label labelA7;
  @FXML private Pane paneA;
  private boolean visibleA = false;

  @FXML private Label labelJO;
  @FXML private Label labelJO1;
  @FXML private Label labelJO2;
  @FXML private Label labelJO3;
  @FXML private Label labelJO4;
  @FXML private Label labelJO5;
  @FXML private Label labelJO6;
  @FXML private Pane paneJO;
  private boolean visibleJO = false;

  @FXML private Label labelP;
  @FXML private Label labelP1;
  @FXML private Label labelP2;
  @FXML private Label labelP3;
  @FXML private Label labelP4;
  @FXML private Label labelP5;
  @FXML private Label labelP6;
  @FXML private Pane paneP;
  private boolean visibleP = false;

  @FXML private Label labelD;
  @FXML private Label labelD1;
  @FXML private Label labelD2;
  @FXML private Label labelD3;
  @FXML private Label labelD4;
  @FXML private Label labelD5;
  @FXML private Label labelD6;
  @FXML private Pane paneD;
  private boolean visibleD = false;

  @FXML private Label labelTU;
  @FXML private Label labelTU1;
  @FXML private Label labelTU2;
  @FXML private Label labelTU3;
  @FXML private Label labelTU4;
  @FXML private Label labelTU5;
  @FXML private Label labelTU6;
  @FXML private Pane paneTU;
  private boolean visibleTU = false;

  @FXML private Label labelTR;
  @FXML private Label labelTR1;
  @FXML private Label labelTR2;
  @FXML private Label labelTR3;
  @FXML private Label labelTR4;
  @FXML private Label labelTR5;
  @FXML private Label labelTR6;
  @FXML private Pane paneTR;
  private boolean visibleTR = false;

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
    labelJ7.setVisible(false);
    paneJ.setVisible(false);

    labelJA.setVisible(false);
    labelJA1.setVisible(false);
    labelJA2.setVisible(false);
    labelJA3.setVisible(false);
    labelJA4.setVisible(false);
    labelJA5.setVisible(false);
    labelJA6.setVisible(false);
    paneJA.setVisible(false);

    labelB.setVisible(false);
    labelB1.setVisible(false);
    labelB2.setVisible(false);
    labelB3.setVisible(false);
    labelB4.setVisible(false);
    labelB5.setVisible(false);
    labelB6.setVisible(false);
    paneB.setVisible(false);

    labelA.setVisible(false);
    labelA1.setVisible(false);
    labelA2.setVisible(false);
    labelA3.setVisible(false);
    labelA4.setVisible(false);
    labelA5.setVisible(false);
    labelA6.setVisible(false);
    labelA7.setVisible(false);
    paneA.setVisible(false);

    labelJO.setVisible(false);
    labelJO1.setVisible(false);
    labelJO2.setVisible(false);
    labelJO3.setVisible(false);
    labelJO4.setVisible(false);
    labelJO5.setVisible(false);
    labelJO6.setVisible(false);
    paneJO.setVisible(false);

    labelP.setVisible(false);
    labelP1.setVisible(false);
    labelP2.setVisible(false);
    labelP3.setVisible(false);
    labelP4.setVisible(false);
    labelP5.setVisible(false);
    labelP6.setVisible(false);
    paneP.setVisible(false);

    labelD.setVisible(false);
    labelD1.setVisible(false);
    labelD2.setVisible(false);
    labelD3.setVisible(false);
    labelD4.setVisible(false);
    labelD5.setVisible(false);
    labelD6.setVisible(false);
    paneD.setVisible(false);

    labelTU.setVisible(false);
    labelTU1.setVisible(false);
    labelTU2.setVisible(false);
    labelTU3.setVisible(false);
    labelTU4.setVisible(false);
    labelTU5.setVisible(false);
    labelTU6.setVisible(false);
    paneTU.setVisible(false);

    labelTR.setVisible(false);
    labelTR1.setVisible(false);
    labelTR2.setVisible(false);
    labelTR3.setVisible(false);
    labelTR4.setVisible(false);
    labelTR5.setVisible(false);
    labelTR6.setVisible(false);
    paneTR.setVisible(false);

    fillTopPaneAPI();
  }

  @Override
  void updateTreeTable() {}

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}

  @FXML
  private void showInfoMatt() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

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
    if (visibleM) showInfoMatt();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

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
    if (visibleT) showInfoTate();
    if (visibleM) showInfoMatt();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleJ = !visibleJ;
    labelJ.setVisible(visibleJ);
    labelJ1.setVisible(visibleJ);
    labelJ2.setVisible(visibleJ);
    labelJ3.setVisible(visibleJ);
    labelJ4.setVisible(visibleJ);
    labelJ5.setVisible(visibleJ);
    labelJ6.setVisible(visibleJ);
    labelJ7.setVisible(visibleJ);
    paneJ.setVisible(visibleJ);
  }

  @FXML
  private void showInfoJakob() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleM) showInfoMatt();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleJA = !visibleJA;
    labelJA.setVisible(visibleJA);
    labelJA1.setVisible(visibleJA);
    labelJA2.setVisible(visibleJA);
    labelJA3.setVisible(visibleJA);
    labelJA4.setVisible(visibleJA);
    labelJA5.setVisible(visibleJA);
    labelJA6.setVisible(visibleJA);
    paneJA.setVisible(visibleJA);
  }

  @FXML
  private void showInfoBen() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleM) showInfoMatt();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleB = !visibleB;
    labelB.setVisible(visibleB);
    labelB1.setVisible(visibleB);
    labelB2.setVisible(visibleB);
    labelB3.setVisible(visibleB);
    labelB4.setVisible(visibleB);
    labelB5.setVisible(visibleB);
    labelB6.setVisible(visibleB);
    paneB.setVisible(visibleB);
  }

  @FXML
  private void showInfoAndres() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleM) showInfoMatt();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleA = !visibleA;
    labelA.setVisible(visibleA);
    labelA1.setVisible(visibleA);
    labelA2.setVisible(visibleA);
    labelA3.setVisible(visibleA);
    labelA4.setVisible(visibleA);
    labelA5.setVisible(visibleA);
    labelA6.setVisible(visibleA);
    labelA7.setVisible(visibleA);
    paneA.setVisible(visibleA);
  }

  @FXML
  private void showInfoJolene() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleM) showInfoMatt();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleJO = !visibleJO;
    labelJO.setVisible(visibleJO);
    labelJO1.setVisible(visibleJO);
    labelJO2.setVisible(visibleJO);
    labelJO3.setVisible(visibleJO);
    labelJO4.setVisible(visibleJO);
    labelJO5.setVisible(visibleJO);
    labelJO6.setVisible(visibleJO);
    paneJO.setVisible(visibleJO);
  }

  @FXML
  private void showInfoParker() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleM) showInfoMatt();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleP = !visibleP;
    labelP.setVisible(visibleP);
    labelP1.setVisible(visibleP);
    labelP2.setVisible(visibleP);
    labelP3.setVisible(visibleP);
    labelP4.setVisible(visibleP);
    labelP5.setVisible(visibleP);
    labelP6.setVisible(visibleP);
    paneP.setVisible(visibleP);
  }

  @FXML
  private void showInfoDylan() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleM) showInfoMatt();
    if (visibleTU) showInfoTucker();
    if (visibleTR) showInfoTrevor();

    visibleD = !visibleD;
    labelD.setVisible(visibleD);
    labelD1.setVisible(visibleD);
    labelD2.setVisible(visibleD);
    labelD3.setVisible(visibleD);
    labelD4.setVisible(visibleD);
    labelD5.setVisible(visibleD);
    labelD6.setVisible(visibleD);
    paneD.setVisible(visibleD);
  }

  @FXML
  private void showInfoTucker() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleM) showInfoMatt();
    if (visibleTR) showInfoTrevor();

    visibleTU = !visibleTU;
    labelTU.setVisible(visibleTU);
    labelTU1.setVisible(visibleTU);
    labelTU2.setVisible(visibleTU);
    labelTU3.setVisible(visibleTU);
    labelTU4.setVisible(visibleTU);
    labelTU5.setVisible(visibleTU);
    labelTU6.setVisible(visibleTU);
    paneTU.setVisible(visibleTU);
  }

  @FXML
  private void showInfoTrevor() {
    if (visibleT) showInfoTate();
    if (visibleJ) showInfoJason();
    if (visibleJA) showInfoJakob();
    if (visibleB) showInfoBen();
    if (visibleA) showInfoAndres();
    if (visibleJO) showInfoJolene();
    if (visibleP) showInfoParker();
    if (visibleD) showInfoDylan();
    if (visibleTU) showInfoTucker();
    if (visibleM) showInfoMatt();

    visibleTR = !visibleTR;
    labelTR.setVisible(visibleTR);
    labelTR1.setVisible(visibleTR);
    labelTR2.setVisible(visibleTR);
    labelTR3.setVisible(visibleTR);
    labelTR4.setVisible(visibleTR);
    labelTR5.setVisible(visibleTR);
    labelTR6.setVisible(visibleTR);
    paneTR.setVisible(visibleTR);
  }
}
