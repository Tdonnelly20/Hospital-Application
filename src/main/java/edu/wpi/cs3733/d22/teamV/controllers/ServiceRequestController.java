package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

  @FXML private AnchorPane servicePane;
  @FXML private ImageView serviceImage;
  @FXML private Group buttonGroup;
  @FXML private Button namesButton;

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

    servicePane
        .widthProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = servicePane.getWidth();
                double h = servicePane.getHeight();

                if (w / 16 * 9 > h) {
                  serviceImage.setFitWidth(w);
                  serviceImage.setFitHeight(w / 16 * 9);
                }

                if (h / 9 * 16 > w) {
                  serviceImage.setFitWidth(h / 9 * 16);
                  serviceImage.setFitHeight(h);
                }

                buttonGroup.setLayoutX(w / 2 - 582);

                namesButton.setLayoutX(w - 153);
              }
            });

    servicePane
        .heightProperty()
        .addListener(
            new ChangeListener<Number>() {
              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double w = servicePane.getWidth();
                double h = servicePane.getHeight();

                if (w / 16 * 9 > h) {
                  serviceImage.setFitWidth(w);
                  serviceImage.setFitHeight(w / 16 * 9);
                }

                if (h / 9 * 16 > w) {
                  serviceImage.setFitWidth(h / 9 * 16);
                  serviceImage.setFitHeight(h);
                }

                buttonGroup.setLayoutY(h / 2 - 261);

                namesButton.setLayoutY(h - 41);
              }
            });
  }

  @Override
  void updateTreeTable() {}

  @Override
  void resetForm() {}

  @Override
  void validateButton() {}
}
