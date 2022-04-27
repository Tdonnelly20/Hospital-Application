package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreditsController extends RequestController {

  @FXML private AnchorPane creditPane;
  @FXML private Group creditGroup;

  @Override
  public void init() {
    setTitleText("Credits");
    fillTopPaneAPI();

    creditPane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = creditPane.getWidth();

                creditGroup.setLayoutX(w / 2 - 398);
              }
            });
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
