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
  @FXML VBox content = new VBox(25);
  @FXML VBox sceneVbox = new VBox(25);
  @FXML Scene scene = new Scene(sceneVbox, 400, 200);
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
      content.getChildren().clear();
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
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");

    content.setAlignment(Pos.TOP_CENTER);

    sceneVbox.getChildren().addAll(titleBox, content);
    sceneVbox.setAlignment(Pos.TOP_CENTER);

    stage.setMaxHeight(200);
    stage.setMaxWidth(400);
    stage.setMinHeight(200);
    stage.setMinWidth(400);

    yesButton.setMinWidth(100);
    noButton.setMinWidth(100);

    yesButton.setOnAction(event1 -> {});

    noButton.setOnAction(
        event1 -> {
          closePopUp();
          clear();
        });
  }

  /** Opens the general icon window and sets up actions */
  @FXML
  public void iconWindow() {
    clear("Are you sure you want to delete?", "Please choose an option");
    content.getChildren().addAll(yesButton, noButton);
    yesButton.setOnAction(
        event2 -> {
          content.getChildren().clear();
          closePopUp();
          clear();
        });
    noButton.setOnAction(
        event2 -> {
          content.getChildren().clear();
          closePopUp();
          clear();
        });
    showPopUp();
  }

  /** Clears the popup's contents */
  private void clear() {
    content.getChildren().clear();
    titleBox.getChildren().clear();
  }
  /** Clears the popup's contents and sets up the stage name + header */
  private void clear(String stageTitle, String headerTitle) {
    clear();
    title.setText(headerTitle);
    stage.setTitle(stageTitle);
  }

  public void start(Stage stage) {}
}
