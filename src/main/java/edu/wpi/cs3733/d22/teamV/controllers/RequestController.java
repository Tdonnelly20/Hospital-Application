package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public abstract class RequestController extends MapController {

  // This is the header Gridpane
  @FXML private GridPane headerPane = new GridPane();

  @FXML private ImageView logo = new ImageView(new Image("Brigham_and_Womens_Hospital_logo.png"));
  @FXML private Label title = new Label();

  @FXML private Region region = new Region();

  @FXML private GridPane buttonBox = new GridPane();
  @FXML private Button closeButton = new Button();
  @FXML private Button homeButton = new Button();
  @FXML private Button backButton = new Button();

  @FXML protected VBox sideBar = new VBox();

  @FXML protected VBox tableBox = new VBox();

  void fillTopPane() {
    headerPane.setMinSize(Region.USE_COMPUTED_SIZE, 150);
    headerPane.setPrefSize(1920, 150);
    headerPane.setMaxSize(Region.USE_COMPUTED_SIZE, 150);
    headerPane.setAlignment(Pos.CENTER);

    buttonBox.setMinSize(450, 150);
    buttonBox.setPrefSize(450, 150);
    buttonBox.setMaxSize(450, 150);
    buttonBox.setAlignment(Pos.CENTER);

    // Creates the box to put the buttons in at the top right
    ColumnConstraints bCol0Cons = new ColumnConstraints(150);
    bCol0Cons.setHalignment(HPos.CENTER);
    ColumnConstraints bCol1Cons = new ColumnConstraints(150);
    bCol1Cons.setHalignment(HPos.CENTER);
    ColumnConstraints bCol2Cons = new ColumnConstraints(150);
    bCol2Cons.setHalignment(HPos.CENTER);
    RowConstraints bRowCons = new RowConstraints(150);
    bRowCons.setValignment(VPos.CENTER);

    backButton.setText("Service Requests");
    homeButton.setText("Home");
    closeButton.setText("Close");

    buttonBox.add(backButton, 0, 0);
    buttonBox.add(homeButton, 1, 0);
    buttonBox.add(closeButton, 2, 0);

    buttonBox.getColumnConstraints().add(bCol0Cons);
    buttonBox.getColumnConstraints().add(bCol1Cons);
    buttonBox.getColumnConstraints().add(bCol2Cons);
    buttonBox.getRowConstraints().add(bRowCons);
    buttonBox.setGridLinesVisible(true);

    logo.setFitHeight(80);
    logo.setFitWidth(80);
    title.setFont(new Font("Arial", 30));
    title.setText("Use setTitleText");
    region.setStyle("-fx-background-color: blue;");
    region.setMinSize(10, 10);
    region.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

    ColumnConstraints hCol0Cons =
        new ColumnConstraints(150, 150, 150, Priority.NEVER, HPos.CENTER, false);
    ColumnConstraints hCol1Cons =
        new ColumnConstraints(500, 500, 500, Priority.NEVER, HPos.CENTER, false);

    ColumnConstraints hCol2Cons =
        new ColumnConstraints(
            Region.USE_COMPUTED_SIZE,
            300,
            Region.USE_COMPUTED_SIZE,
            Priority.ALWAYS,
            HPos.CENTER,
            false);

    ColumnConstraints hCol3Cons =
        new ColumnConstraints(450, 450, 450, Priority.NEVER, HPos.CENTER, false);

    RowConstraints hRowCons = new RowConstraints(150, 150, 150, Priority.NEVER, VPos.CENTER, false);

    headerPane.addColumn(0, logo);
    headerPane.addColumn(1, title);
    headerPane.addColumn(2, region);
    headerPane.addColumn(3, buttonBox);

    headerPane.getColumnConstraints().clear();
    headerPane.getColumnConstraints().add(hCol0Cons);
    headerPane.getColumnConstraints().add(hCol1Cons);
    headerPane.getColumnConstraints().add(hCol2Cons);
    headerPane.getColumnConstraints().add(hCol3Cons);
    headerPane.getRowConstraints().add(0, hRowCons);
    headerPane.setGridLinesVisible(true);
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
