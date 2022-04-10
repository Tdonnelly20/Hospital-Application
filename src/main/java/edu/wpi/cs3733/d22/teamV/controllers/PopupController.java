package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.ServiceRequests.LabRequest;
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
  @FXML VBox sceneVbox = new VBox(25);
  @FXML VBox content = new VBox(25);
  @FXML Scene scene = new Scene(sceneVbox, 450, 450);
  @FXML Stage stage = new Stage();
  @FXML Button locationButton = new Button("Location");
  @FXML Button equipmentButton = new Button("Equipment");
  @FXML Button requestButton = new Button("Request");
  @FXML Button submitIcon = new Button("Add Location");
  @FXML Button clearResponse = new Button("Clear Text");
  @FXML Button closeButton = new Button("Close");
  @FXML Button returnButton = new Button("<- Go Back");
  @FXML Label title = new Label();
  @FXML HBox titleBox = new HBox(25, title);
  @FXML HBox buttonBox = new HBox(15, submitIcon, clearResponse, closeButton);
  @FXML Text missingFields = new Text("Please fill all fields");
  @FXML TextField field1 = new TextField();
  @FXML TextField field2 = new TextField();
  @FXML TextField field3 = new TextField();
  @FXML TextField field4 = new TextField();
  @FXML JFXComboBox<String> comboBox1 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox2 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox3 = new JFXComboBox<>();

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
    title.setTextFill(Color.WHITE);
    title.setFont(new Font("System Bold", 28));
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");

    buttonBox.setAlignment(Pos.CENTER);
    content.setAlignment(Pos.TOP_CENTER);

    sceneVbox.getChildren().addAll(titleBox, buttonBox, content);
    sceneVbox.setAlignment(Pos.TOP_CENTER);

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

  @FXML
  public void iconWindow(MouseEvent event) {
    title.setText("Please choose an option");
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(locationButton, equipmentButton, requestButton, closeButton);
    locationButton.setOnAction(
        event2 -> {
          locationAdditionForm(event, false);
        });
    equipmentButton.setOnAction(
        event2 -> {
          equipmentAdditionForm(event);
        });
    requestButton.setOnAction(
        event1 -> {
          requestAdditionForm(event);
        });
    formSetup(event);
    stage.setTitle("Options");
    showPopUp();
  }

  /** Opens the corresponding icon's request window */
  @FXML
  public void openIconRequestWindow(Icon icon) {
    // Display requests/info
    content.getChildren().clear();
    MapManager.getManager().isTempIconVisible(false);
    stage.setTitle(icon.getLocation().getShortName());
    if (icon.iconType.equals("Equipment")) {
      EquipmentIcon equipmentIcon = ((EquipmentIcon) icon);
      title.setText("Equipment");
      // equipmentAdditionForm();
    } else if (icon.iconType.equals("Request")) {
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
          content.getChildren().add(accordion);
        }
      } else if (icon.iconType.equals("Location")) {
        Text noRequests = new Text("There are no requests in this area");
        noRequests.setTextAlignment(TextAlignment.CENTER);
        content.getChildren().add(noRequests);
      }
    }
    showPopUp();
  }

  @FXML
  public void formSetup(MouseEvent event) {
    MapManager.getManager().isTempIconVisible(true);

    clearResponse.setOnAction(
        event1 -> {
          clearPopupForm();
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          MapManager.getManager().isTempIconVisible(false);
        });
    returnButton.setOnAction(
        event1 -> {
          clearPopupForm();
          content.getChildren().clear();
          iconWindow(event);
        });
  }

  @FXML
  public void locationAdditionForm(MouseEvent event, boolean isEquipment) {
    title.setText("Add A Location");
    content.getChildren().clear();
    content.getChildren().addAll(field1, field2, field3, field4);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    // Form

    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    // Scene and Stage
    stage.setTitle("Add New Location");
    showPopUp();
  }

  @FXML
  public void requestAdditionForm(MouseEvent event) {
    title.setText("Add A Service Request");
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, clearResponse, closeButton);
    // Form
    comboBox1 =
        new JFXComboBox<>(
            FXCollections.observableArrayList(
                "Lab Request",
                "Equipment Delivery",
                "Medicine Delivery",
                "Internal Patient Transport",
                "Laundry Request",
                "Meal Delivery",
                "Sanitation Request",
                "Religious Request"));
    comboBox1.setValue("Service Request");
    comboBox1.setOnAction(
        event1 -> {
          fitServiceRequest(event, comboBox1.getValue().toString());
        });
    comboBox2 =
        new JFXComboBox<>(FXCollections.observableArrayList("Not Started", "Processing", "Done"));
    comboBox2.setValue("Status");

    content.getChildren().clear();
    content.getChildren().addAll(comboBox1);

    // Scene and Stage
    stage.setTitle("Add New Service Request");
    showPopUp();
  }

  @FXML
  public void fitServiceRequest(MouseEvent event, String serviceRequest) {
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    submitIcon.setText("Add Request");
    content.getChildren().clear();
    field1.setPromptText("Employee ID");
    comboBox3 =
        new JFXComboBox<>(FXCollections.observableArrayList("Not Started", "Processing", "Done"));
    content.getChildren().addAll(comboBox1, field1);

    switch (serviceRequest) {
      case "Lab Request":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood Sample", "Urine Sample", "X-Ray", "CAT", "MRI"));
        content.getChildren().addAll(field2, field3, comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              LabRequest labRequest =
                  new LabRequest(
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      "",
                      "",
                      comboBox2.getValue().toString(),
                      comboBox3.getValue().toString());
            });
        break;
      case "Equipment Delivery":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Quantity");
        field4.setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Bed", "Portable X-Ray", "Blood Infusion Machine", "Patient Recliner"));
        content.getChildren().addAll(field2, field3, field4, comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              EquipmentDelivery equipmentDelivery =
                  new EquipmentDelivery(
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      "",
                      "",
                      "node id",
                      comboBox2.getValue().toString(),
                      field4.getText(),
                      Integer.parseInt(field3.getText()),
                      comboBox3.getValue().toString());
            });
        break;
      case "Medicine Delivery":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Tylenol", "Morphine", "Epinephrine", "Adderall", "Cyclosporine"));
        content.getChildren().addAll(field2, field3, comboBox2);
        submitIcon.setOnAction(event1 -> {});
        break;
      case "Internal Patient Transport":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Transport to");
        field4.setPromptText("Notes");
        content.getChildren().addAll(field2, field3, field4);
        submitIcon.setOnAction(event1 -> {});
        break;
      case "Laundry Request":
        field2.setPromptText("Notes");
        submitIcon.setOnAction(event1 -> {});
        break;
      case "Meal Delivery":
        field2.setPromptText("Allergies");
        field3.setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Cheeseburger", "Chicken Tender", "Hot Dog", "Fries", "Crackers"));
        submitIcon.setOnAction(event1 -> {});
        break;
      case "Sanitation Request":
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood",
                    "Vomit",
                    "Other Bodily Fluids",
                    "Broken Glass",
                    "No Hazard/General Mess"));
        submitIcon.setOnAction(event1 -> {});
        break;
      case "Religious Request":
        field2.setPromptText("Patient ID");
        content.getChildren().addAll(field2);

        submitIcon.setOnAction(event1 -> {});
        break;
    }
    content.getChildren().addAll(comboBox3);
  }

  @FXML
  public void equipmentAdditionForm(MouseEvent event) {
    title.setText("Add Equipment");
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);

    field1.setPromptText("Type of Equipment");
    field2.setPromptText("Quantity");
    comboBox1.setValue("Status");
    comboBox1 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    content.getChildren().clear();
    content.getChildren().addAll(field1, field2, comboBox1);

    submitIcon.setOnAction(
        event1 -> {
          // RequestSystem.Dao.
          EquipmentIcon equipmentIcon =
              new EquipmentIcon(
                  new Location(
                      field1.getText() + field2.getText() + comboBox1.getValue(),
                      event.getX(),
                      event.getY(),
                      "Tower",
                      MapController.getController().currFloor.getFloorName(),
                      "Equipment Storage",
                      "",
                      ""));
        });

    // Scene and Stage
    stage.setTitle("Equipment");
    showPopUp();
  }

  public boolean checkLocationFields() {
    return !field1.getText().isEmpty()
        && !field2.getText().isEmpty()
        && !field3.getText().isEmpty()
        && !field4.getText().isEmpty();
  }

  public boolean checkEquipmentFields() {
    return !field1.getText().isEmpty()
        && !field2.getText().isEmpty()
        && !comboBox1.getValue().toString().equals("Status");
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
    comboBox1.setValue("Status");
  }
}
