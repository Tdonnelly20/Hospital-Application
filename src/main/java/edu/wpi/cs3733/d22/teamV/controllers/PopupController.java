package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.RequestIcon;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupController {
  @FXML VBox content = new VBox(15);
  @FXML Scene scene = new Scene(content, 450, 450);
  @FXML Stage stage = new Stage();
  @FXML Button locationButton = new Button("Add Location");
  @FXML Button equipmentButton = new Button("Add Equipment");
  @FXML Button submitIcon = new Button("Add icon");
  @FXML Button clearResponse = new Button("Clear Text");
  @FXML Button closeButton = new Button("Close");
  @FXML Label title = new Label("Add");
  @FXML HBox titleBox = new HBox(15, title);
  @FXML HBox buttonBox = new HBox(15);
  @FXML Text missingFields = new Text("Please fill all fields");
  @FXML TextField field1 = new TextField();
  @FXML TextField field2 = new TextField();
  @FXML TextField field3 = new TextField();
  @FXML TextField field4 = new TextField();

  private static class SingletonHelper {
    private static final PopupController controller = new PopupController();
  }

  public static PopupController getController() {
    return PopupController.SingletonHelper.controller;
  }

  /** Closes the popup window */
  @FXML
  public void closePopUp() {
    if (stage.isShowing()) {
      stage.close();
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
    field1.setMaxWidth(250);
    field2.setMaxWidth(250);
    field3.setMaxWidth(250);
    field4.setMaxWidth(250);
    submitIcon.setMinWidth(100);
    clearResponse.setMinWidth(100);

    stage.setMaxHeight(450);
    stage.setMaxWidth(450);
    stage.setMinHeight(450);
    stage.setMinWidth(450);
    stage.setOnCloseRequest(
        event -> {
          MapManager.getManager().isTempIconVisible(false);
        });
  }

  /** Opens the corresponding icon's request window */
  @FXML
  public void openIconRequestWindow(Icon icon) {
    // Display requests/info
    content.getChildren().clear();
    MapManager.getManager().isTempIconVisible(false);
    Label title = new Label(icon.getLocation().getShortName());
    title.setTextFill(Color.WHITE);
    title.setFont(new Font("System Bold", 28));
    HBox titleBox = new HBox(25, title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");
    content.getChildren().add(titleBox);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.TOP_CENTER);
    if (icon.iconType.equals("Equipment")) {
      EquipmentIcon equipmentIcon = ((EquipmentIcon) icon);
      title.setText("Equipment");
      vbox.getChildren().addAll(title);
    } else {
      RequestIcon requestIcon = (RequestIcon) icon;
      ObservableList<String> statusStrings =
          FXCollections.observableArrayList("Not Started", "Processing", "Done");
      if (requestIcon.getRequestsArr().size() > 0) {
        for (ServiceRequest request : requestIcon.getRequestsArr()) {
          Label idLabel = new Label("Employee: " + request.getHospitalEmployee().getHospitalID());
          Label locationLabel =
              new Label(
                  "X: " + icon.getLocation().getXCoord() + " Y: " + icon.getLocation().getYCoord());

          JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
          updateStatus.setValue(request.getStatus());
          updateStatus.setOnAction(
              event -> {
                request.setStatus(updateStatus.getValue().toString());
                // TODO: Update request CSV
              });
          Accordion accordion =
              new Accordion(
                  new TitledPane(
                      request.getRequestName() + ": " + request.getStatus(),
                      new VBox(15, idLabel, locationLabel, updateStatus)));
          vbox.getChildren().add(accordion);
        }
      } else {
        Text noRequests = new Text("There are no requests in this area");
        noRequests.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().add(noRequests);
      }
    }
    content.getChildren().add(vbox);
    showPopUp();
  }

  @FXML
  public void formSetup() {
    MapManager.getManager().isTempIconVisible(true);
    title.setText("Add a Location");
    title.setTextFill(Color.WHITE);
    title.setAlignment(Pos.CENTER);
    // title.setAlignment(TextAlignment.CENTER);
    title.setFont(new Font("System Bold", 38));
    title.setWrapText(true);
    HBox titleBox = new HBox(15, title);

    titleBox.setStyle("-fx-background-color: #012D5Aff;");
    titleBox.setAlignment(Pos.CENTER);
    HBox buttonBox = new HBox(submitIcon, clearResponse, closeButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setSpacing(15);
    content.getChildren().addAll(titleBox, buttonBox);

    clearResponse.setOnAction(
        event1 -> {
          clearPopupForm();
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          MapManager.getManager().isTempIconVisible(false);
        });
    VBox vBox = new VBox(15, field1, field2, field3, field4);
    vBox.setAlignment(Pos.TOP_CENTER);
    content.getChildren().addAll(vBox);
  }

  @FXML
  public void locationForm(MouseEvent event, boolean isEquipment) {
    content.getChildren().clear();
    // Form
    formSetup();

    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    // Scene and Stage
    stage.setTitle("Add New Location");
    showPopUp();
  }

  @FXML
  public void equipmentForm(MouseEvent event) {
    content.getChildren().clear();
    // Form
    formSetup();

    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    // Scene and Stage
    stage.setTitle("Add New Location");
    showPopUp();
  }

  public boolean checkFields() {
    return !field1.getText().isEmpty()
        && !field2.getText().isEmpty()
        && !field3.getText().isEmpty()
        && !field4.getText().isEmpty();
  }

  public Location getLocation(double xPos, double yPos, String floor) {
    return new Location(
        field1.getText(),
        xPos,
        yPos,
        floor,
        "Tower",
        field2.getText(),
        field3.getText(),
        field4.getText());
  }

  @FXML
  private void clearPopupForm() {
    field1.setText("");
    field2.setText("");
    field3.setText("");
    field4.setText("");
    buttonBox.getChildren().clear();
    titleBox.getChildren().clear();
  }
}
