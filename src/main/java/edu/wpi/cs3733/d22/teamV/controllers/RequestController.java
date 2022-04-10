package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public abstract class RequestController extends MapController {

  // Main Header Gridpane stuff
  @FXML private GridPane headerPane = new GridPane();
  private ColumnConstraints headerCol0Constraints = new ColumnConstraints();
  private ColumnConstraints headerCol1Constraints = new ColumnConstraints();
  private ColumnConstraints headerCol3Constraints = new ColumnConstraints();

  private RowConstraints rowConstraints = new RowConstraints();
  @FXML private ImageView logo = new ImageView(new Image("Brigham_and_Womens_Hospital_logo.png"));
  @FXML private Label title = new Label();

  @FXML private GridPane buttonBox = new GridPane();
  @FXML private Button closeButton = new Button();
  @FXML private Button homeButton = new Button();
  @FXML private Button backButton = new Button();

  @FXML protected VBox sideBar = new VBox();

  @FXML protected VBox tableBox = new VBox();

  void fillTopPane() {
    title.setFont(new Font("Arial", 30));
    logo.setFitHeight(80);
    logo.setFitWidth(80);

    headerPane.add(logo, 0, 0);
    headerPane.add(title, 1, 0);
    headerPane.add(buttonBox, 3, 0);

    headerCol0Constraints.setHalignment(HPos.CENTER);
    headerCol1Constraints.setHalignment(HPos.CENTER);
    headerCol3Constraints.setHalignment(HPos.CENTER);

    headerCol0Constraints.setMinWidth(150);
    headerCol0Constraints.setMaxWidth(150);
    headerCol0Constraints.setPrefWidth(150);

    headerCol1Constraints.setMinWidth(500);
    headerCol1Constraints.setMaxWidth(500);
    headerCol1Constraints.setPrefWidth(500);

    headerCol3Constraints.setMinWidth(450);
    headerCol3Constraints.setMaxWidth(450);
    headerCol3Constraints.setPrefWidth(450);

    rowConstraints.setMinHeight(150);
    rowConstraints.setMaxHeight(150);
    rowConstraints.setPrefHeight(150);

    headerPane.getColumnConstraints().add(0, headerCol0Constraints);
    headerPane.getColumnConstraints().add(1, headerCol1Constraints);
    headerPane.getColumnConstraints().add(3, headerCol3Constraints);
    headerPane.getRowConstraints().add(rowConstraints);
  }

  void setTitleText(String str) {
    title.setText(str);
  }

  abstract void updateTreeTable();

  abstract void resetForm();

  abstract void validateButton();

  //  abstract void fillSideBar();
  //
  //  abstract void fillTableBox();
}
