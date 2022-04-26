package edu.wpi.cs3733.d22.teamV.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DBPopupController extends Controller {

  @FXML Button yesButton = new Button("Yes");
  @FXML Button noButton = new Button("No");
  @FXML HBox content = new HBox(25);
  @FXML VBox sceneVbox = new VBox(25);
  @FXML Scene scene = new Scene(sceneVbox, 300, 150);
  @FXML Stage stage = new Stage();
  @FXML Label title = new Label();
  @FXML HBox titleBox = new HBox(25, title);

  private static class SingletonHelper {
    private static final DBPopupController controller = new DBPopupController();
  }

  public static DBPopupController getController() {
    return DBPopupController.SingletonHelper.controller;
  }

  public void init() {
    setUpPopup();
    scene.getStylesheets().add("CSS/Popup.css");
  }

  /** Closes the popup window */
  @FXML
  public void closePopUp() {
    if (stage.isShowing()) {
      stage.close();
      sceneVbox.getChildren().clear();
      // MapController.getController().checkDropDown();
    }
  }

  /** Opens the popup window */
  @FXML
  public void showPopUp() {
    stage.setScene(scene);
    if (!stage.isShowing()) {
      stage.show();
    }
  }

  /** Sets up the popup window */
  @FXML
  public void setUpPopup() {
    stage.setAlwaysOnTop(true);
    title.setTextFill(Color.WHITE);
    title.setFont(new Font("System Bold", 28));
    titleBox.setAlignment(Pos.TOP_CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");

    content.setAlignment(Pos.TOP_CENTER);
    content.setStyle("-fx-background-color: #5f6c83;");

    sceneVbox.getChildren().addAll(titleBox, content);
    sceneVbox.setAlignment(Pos.TOP_CENTER);
    sceneVbox.setStyle("-fx-background-color: #5f6c83;");

    stage.setMaxHeight(150);
    stage.setMaxWidth(300);
    stage.setMinHeight(150);
    stage.setMinWidth(300);

    yesButton.setMinWidth(100);
    noButton.setMinWidth(100);

    title.setText("Are you sure?");
    stage.setTitle("Please choose an option");
  }

  /** Opens the general icon window and sets up actions */
  @FXML
  public void iconWindow() {
    content.getChildren().addAll(yesButton, noButton);
    yesButton.setOnAction(
        event2 -> {
          sceneVbox.getChildren().clear();
          EmployeeController.getController().setIsSure(true);
          closePopUp();
        });
    noButton.setOnAction(
        event2 -> {
          sceneVbox.getChildren().clear();
          EmployeeController.getController().setIsSure(false);
          closePopUp();
        });
    showPopUp();
  }

  public void start(Stage stage) {}
}
