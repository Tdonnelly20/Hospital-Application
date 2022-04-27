package edu.wpi.cs3733.d22.teamV.controllers;

import java.awt.*;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class RequestController extends Controller {

  // This is the header Gridpane
  @FXML private GridPane headerPane = new GridPane();

  // The logo and headers in the upper right corner
  @FXML private ImageView logo = new ImageView(new Image("Brigham_and_Womens_Hospital_logo.png"));
  @FXML private Label headerTitle = new Label();

  // The gridpane that the close, home and back buttons go in
  @FXML private GridPane buttonBox = new GridPane();

  // The close home and back buttons
  @FXML private ImageView backButton = new ImageView(new Image("BackButton.png"));
  @FXML private ImageView homeButton = new ImageView(new Image("HomeButton.png"));
  @FXML private ImageView closeButton = new ImageView(new Image("ExitButton.png"));
  @FXML private ImageView APIButton = new ImageView(new Image("APIButton.png"));

  /** This function is used to fill the header on a request page */
  void fillTopPane() {
    fillTopPaneHelper(1);
  }

  void fillTopPaneAPI() {
    fillTopPaneHelper(2);
  }

  void fillTopPaneHelper(int type) {
    // Set the basic attributes of the headerPane
    headerPane.setMinSize(Region.USE_COMPUTED_SIZE, 150);
    headerPane.setPrefSize(1920, 150);
    headerPane.setMaxSize(Region.USE_COMPUTED_SIZE, 150);
    headerPane.setAlignment(Pos.CENTER);
    headerPane.setStyle("-fx-background-color: #1F355B");

    // Set the basic attributes of the buttonBox
    buttonBox.setMinSize(180, 150);
    buttonBox.setPrefSize(180, 150);
    buttonBox.setMaxSize(180, 150);
    buttonBox.setAlignment(Pos.CENTER);

    // Creates the constraints for the buttonBox
    ColumnConstraints bCol0Cons = new ColumnConstraints(80);
    bCol0Cons.setHalignment(HPos.CENTER);
    ColumnConstraints bCol1Cons = new ColumnConstraints(80);
    bCol1Cons.setHalignment(HPos.CENTER);
    ColumnConstraints bCol2Cons = new ColumnConstraints(80);
    bCol2Cons.setHalignment(HPos.CENTER);
    RowConstraints bRowCons = new RowConstraints(240);
    bRowCons.setValignment(VPos.CENTER);

    // Make the back button
    backButton.setFitHeight(55);
    backButton.setFitWidth(55);
    backButton.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            try {
              switchToServiceRequest(event);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

    // Make the API button
    APIButton.setFitHeight(55);
    APIButton.setFitWidth(55);
    APIButton.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            try {
              switchToAPI(event);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

    // Make the home button
    homeButton.setFitHeight(55);
    homeButton.setFitWidth(55);
    homeButton.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            switchToHome(event);
          }
        });

    // Make the close button
    closeButton.setFitHeight(55);
    closeButton.setFitWidth(55);
    closeButton.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            stop();
          }
        });

    // Add the buttons to the button box
    if (type == 1) {
      buttonBox.add(backButton, 0, 0);
    } else {
      buttonBox.add(APIButton, 0, 0);
    }
    buttonBox.add(homeButton, 1, 0);
    buttonBox.add(closeButton, 2, 0);

    // Set the row and column constraints for the button box
    buttonBox.getColumnConstraints().add(bCol0Cons);
    buttonBox.getColumnConstraints().add(bCol1Cons);
    buttonBox.getColumnConstraints().add(bCol2Cons);
    buttonBox.getRowConstraints().add(bRowCons);

    // Set the logo size
    logo.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent event) {
            switchToHome(event);
          }
        });
    logo.setFitHeight(80);
    logo.setFitWidth(80);

    // Set the header
    headerTitle.setFont(new Font("Ebrima", 40));
    headerTitle.setTextFill(Color.WHITE);

    // Make the column constraints for the header pane
    ColumnConstraints hCol0Cons =
        new ColumnConstraints(150, 150, 150, Priority.NEVER, HPos.CENTER, false);
    ColumnConstraints hCol1Cons =
        new ColumnConstraints(650, 650, 650, Priority.NEVER, HPos.LEFT, false);

    ColumnConstraints hCol2Cons =
        new ColumnConstraints(
            Region.USE_COMPUTED_SIZE,
            300,
            Region.USE_COMPUTED_SIZE,
            Priority.ALWAYS,
            HPos.CENTER,
            false);

    ColumnConstraints hCol3Cons =
        new ColumnConstraints(240, 240, 240, Priority.NEVER, HPos.CENTER, false);

    RowConstraints hRowCons = new RowConstraints(150, 150, 150, Priority.NEVER, VPos.CENTER, false);

    // Add the items to the header pane
    headerPane.addColumn(0, logo);
    headerPane.addColumn(1, headerTitle);
    headerPane.addColumn(3, buttonBox);

    // Add the constraints to the header pane
    headerPane.getColumnConstraints().clear();
    headerPane.getColumnConstraints().add(hCol0Cons);
    headerPane.getColumnConstraints().add(hCol1Cons);
    headerPane.getColumnConstraints().add(hCol2Cons);
    headerPane.getColumnConstraints().add(hCol3Cons);
    headerPane.getRowConstraints().add(hRowCons);
  }

  void setTitleText(String str) {
    headerTitle.setText(str);
  }

  void setColumnSize(TreeTableColumn c, double w) {
    c.setPrefWidth(w);
    c.setMaxWidth(w);
  }

  abstract void updateTreeTable();

  abstract void resetForm();

  abstract void validateButton();

  public boolean isInteger(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
