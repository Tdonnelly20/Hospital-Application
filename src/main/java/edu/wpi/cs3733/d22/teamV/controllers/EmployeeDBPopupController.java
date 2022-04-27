package edu.wpi.cs3733.d22.teamV.controllers;

import edu.wpi.cs3733.d22.teamV.objects.Employee;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EmployeeDBPopupController extends Controller {

  @FXML Button yesButton = new Button("Yes");
  @FXML Button noButton = new Button("No");
  @FXML HBox content = new HBox(25);
  @FXML VBox sceneVbox = new VBox(25);
  @FXML Scene scene = new Scene(sceneVbox, 300, 125);
  @FXML Stage stage = new Stage();
  @FXML Label title = new Label();
  @FXML HBox titleBox = new HBox(25, title);

  private static class SingletonHelper {
    private static final EmployeeDBPopupController controller = new EmployeeDBPopupController();
  }

  public static EmployeeDBPopupController getController() {
    return EmployeeDBPopupController.SingletonHelper.controller;
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
    title.setFont(new Font("System", 15));
    titleBox.setAlignment(Pos.TOP_CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");

    content.setAlignment(Pos.TOP_CENTER);
    content.setStyle("-fx-background-color: #5f6c83;");

    sceneVbox.getChildren().addAll(titleBox, content);
    sceneVbox.setAlignment(Pos.TOP_CENTER);
    sceneVbox.setStyle("-fx-background-color: #5f6c83;");

    stage.setMaxHeight(125);
    stage.setMaxWidth(300);
    stage.setMinHeight(125);
    stage.setMinWidth(300);

    yesButton.setMinWidth(100);
    noButton.setMinWidth(100);
    content.getChildren().addAll(yesButton, noButton);

    title.setText("This will delete all associated service requests");
    stage.setTitle("Are you sure?");
  }

  /** Opens the general icon window and sets up actions */
  @FXML
  public void iconWindow(Employee last) {
    yesButton.setOnAction(
        event1 -> {
          sceneVbox.getChildren().clear();
          content.getChildren().clear();
          System.out.println("########  Before calling removeSelectedRow()" + last);
          EmployeeController.getController().removeSelectedRow(last);
          closePopUp();
        });
    noButton.setOnAction(
        event2 -> {
          sceneVbox.getChildren().clear();
          content.getChildren().clear();
          closePopUp();
        });
    showPopUp();
  }

  public void start(Stage stage) {}
}
