package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.serviceRequest.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.serviceRequest.LabRequest;
import edu.wpi.cs3733.d22.teamV.serviceRequest.ServiceRequest;
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
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupController {
  @FXML Button locationButton = new Button("Location");
  @FXML Button equipmentButton = new Button("Equipment");
  @FXML Button requestButton = new Button("Request");
  @FXML Button submitIcon = new Button("Add Location");
  @FXML Button clearResponse = new Button("Clear Text");
  @FXML Button closeButton = new Button("Close");
  @FXML Button returnButton = new Button("<- Go Back");
  @FXML Button addButton = new Button("Add");
  @FXML Button modifyButton = new Button("Modify");
  @FXML Button removeButton = new Button("Remove");
  @FXML VBox content = new VBox(25);
  @FXML ScrollPane contentScroll = new ScrollPane(content);
  @FXML VBox sceneVbox = new VBox(25);
  @FXML Scene scene = new Scene(sceneVbox, 450, 450);
  @FXML Stage stage = new Stage();
  @FXML Label title = new Label();
  @FXML HBox titleBox = new HBox(25, title);
  @FXML HBox buttonBox = new HBox(15, submitIcon, clearResponse, closeButton);
  @FXML Text missingFields = new Text("Please fill all fields");
  @FXML TextField field1 = new TextField();
  @FXML TextField field2 = new TextField();
  @FXML TextField field3 = new TextField();
  @FXML TextField field4 = new TextField();
  @FXML TextField field5 = new TextField();
  @FXML TextField field6 = new TextField();
  @FXML TextField field7 = new TextField();
  @FXML TextField field8 = new TextField();
  @FXML TextField field9 = new TextField();
  @FXML JFXComboBox<String> comboBox1 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox2 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox3 = new JFXComboBox<>();
  RequestSystem requestSystem = RequestSystem.getSystem();

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

    buttonBox.setAlignment(Pos.CENTER);
    content.setAlignment(Pos.TOP_CENTER);
    contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    contentScroll.setPrefHeight(400);
    contentScroll.setFitToWidth(true);

    sceneVbox.getChildren().addAll(titleBox, buttonBox, contentScroll);
    sceneVbox.setAlignment(Pos.TOP_CENTER);

    field1.setMaxWidth(250);
    field2.setMaxWidth(250);
    field3.setMaxWidth(250);
    field4.setMaxWidth(250);
    field5.setMaxWidth(250);
    field6.setMaxWidth(250);
    field7.setMaxWidth(250);
    field8.setMaxWidth(250);
    field9.setMaxWidth(250);
    submitIcon.setMinWidth(100);
    clearResponse.setMinWidth(100);

    stage.setMaxHeight(450);
    stage.setMaxWidth(450);
    stage.setMinHeight(450);
    stage.setMinWidth(450);
    stage.setOnCloseRequest(
        event -> {
          // MapManager.getManager().isTempIconVisible(false);
        });
  }

  @FXML
  public void iconWindow(MouseEvent event) {
    title.setText("Please choose an option");
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(locationButton, equipmentButton, requestButton, closeButton);
    locationButton.setOnAction(
        event2 -> {
          buttonBox.getChildren().clear();
          locationForm(event, null);
        });
    equipmentButton.setOnAction(
        event2 -> {
          buttonBox.getChildren().clear();
          equipmentForm(event, null);
        });
    requestButton.setOnAction(
        event1 -> {
          buttonBox.getChildren().clear();
          requestForm(event);
        });
    formSetup(event);
    stage.setTitle("Options");
    showPopUp();
  }

  @FXML
  public void formSetup(MouseEvent event) {
    // MapManager.getManager().isTempIconVisible(true);

    clearResponse.setOnAction(
        event1 -> {
          clearPopupForm();
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          // MapManager.getManager().isTempIconVisible(false);
        });
    returnButton.setOnAction(
        event1 -> {
          clearPopupForm();
          content.getChildren().clear();
          iconWindow(event);
        });
  }

  void addLocation(Location location) {
    requestSystem.addLocation(location);
    MapController.getController().addIcon(location.getIcon());
    MapController.getController().populateFloorIconArr();
    // MapController.getController().addIcon(location.getIcon());
    // MapController.getController().checkDropDown();
    // MapManager.getManager().getFloor(location.getFloor()).getIconList().add(location.getIcon());
    // MapController.getController().mapPane.getChildren().add(location.getIcon().getImage());
  }

  void deleteLocation(LocationIcon location) {
    MapManager.getManager()
        .getFloor(location.getLocation().getFloor())
        .getIconList()
        .remove(location);
    requestSystem.deleteLocation(location.getLocation().getNodeID());
  }

  /**
   * Shows the general location form (add, modify, and remove an unspecified location) if icon ==
   * null and shows the specific location information if icon !=null
   */
  @FXML
  public void locationForm(MouseEvent event, LocationIcon icon) {
    double xPos = event.getX() - 15;
    double yPos = event.getY() - 25;
    MapManager.getManager().placeTempIcon(xPos, yPos);
    buttonBox.getChildren().clear();
    content.getChildren().clear();
    addButton.setText("Add");
    modifyButton.setText("Modify");
    removeButton.setText("Remove");
    if (icon == null) {
      title.setText("Location");
      stage.setTitle("Location");
      buttonBox
          .getChildren()
          .addAll(returnButton, addButton, modifyButton, removeButton, closeButton);
      addButton.setOnAction(
          event1 -> {
            locationAdditionForm(event);
          });
      addButton.setText("Add a Location");
    } else {
      title.setText(icon.getLocation().getShortName());
      stage.setTitle(icon.getLocation().getLongName());
      buttonBox.getChildren().addAll(addButton, modifyButton, removeButton, closeButton);
      addButton.setText("Add a Service Request");
      addButton.setOnAction(
          event1 -> {
            requestAdditionForm(event, icon);
          });
      insertServiceRequests(icon);
    }
    formSetup(event);
    modifyButton.setOnAction(
        event1 -> {
          locationModifyForm(event, icon);
        });
    removeButton.setOnAction(
        event1 -> {
          locationRemoveForm(event, icon);
        });
    if (!stage.isShowing()) {
      showPopUp();
    }
  }

  /** Populates a location icon's popup window with its service requests */
  @FXML
  public void insertServiceRequests(LocationIcon icon) {
    ObservableList<String> statusStrings =
        FXCollections.observableArrayList("Not Started", "Processing", "Done");
    if (icon.getRequestsArr().size() > 0) {
      VBox vBox = new VBox();
      ScrollPane scrollPane = new ScrollPane(vBox);
      scrollPane.setFitToHeight(true);
      scrollPane.setPannable(false);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      vBox.setPrefWidth(450);
      vBox.setPrefHeight(400);
      for (ServiceRequest request : icon.getRequestsArr()) {
        Label idLabel = new Label("Employee: " + request.getHospitalEmployee().getHospitalID());
        Label locationLabel =
            new Label(
                "X: " + icon.getLocation().getXCoord() + " Y: " + icon.getLocation().getYCoord());

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setValue(request.getStatus());
        updateStatus.setOnAction(
            event1 -> {
              request.setStatus(updateStatus.getValue().toString());
              // TODO: Update request CSV
            });

        Accordion accordion =
            new Accordion(
                new TitledPane(
                    request.getRequestName() + ": " + request.getStatus(),
                    new VBox(15, idLabel, locationLabel, updateStatus)));
        accordion.setPrefWidth(450);
        vBox.getChildren().add(accordion);
      }
      content.getChildren().add(scrollPane);
    }
  }

  /** Opens a form that allows users to create a new location */
  @FXML
  public void locationAdditionForm(MouseEvent event) {
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

  /**
   * Opens a form that allows users to modify locations if icon == null then the user must put in an
   * old node ID, if icon !=null then field 1 isn't shown
   */
  @FXML
  public void locationModifyForm(MouseEvent event, LocationIcon icon) {
    title.setText("Modify A Location");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    submitIcon.setText("Modify Location");
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    field1.setPromptText("Old Node ID");
    field2.setPromptText("Node ID");
    field3.setPromptText("X-Coordinate");
    field4.setPromptText("Y-Coordinate");
    comboBox1 =
        new JFXComboBox<>(
            FXCollections.observableArrayList(
                "Lower Level 2",
                "Lower Level 1",
                "1st Floor",
                "2nd Floor",
                "3rd Floor",
                "4th Floor",
                "5th Floor"));
    field5.setPromptText("Building");
    field6.setPromptText("Node Type");
    field7.setPromptText("Short Name");
    field8.setPromptText("Long Name");
    if (icon == null) {
      content
          .getChildren()
          .addAll(field1, field2, field3, field4, field5, comboBox1, field6, field7, field8);
      submitIcon.setOnAction(
              event1 -> {
                deleteLocation(requestSystem.getLocation(field1.getText()).getIcon());

                Location newLocation =
                        new Location(
                                field2.getText(),
                                Double.parseDouble(field3.getText()),
                                Double.parseDouble(field4.getText()),
                                MapManager.getManager()
                                        .getFloor(comboBox1.getValue().toString())
                                        .getFloorName(),
                                field5.getText(),
                                field6.getText(),
                                field7.getText(),
                                field8.getText());

                addLocation(newLocation);
                LocationIcon newIcon = new LocationIcon(newLocation);
                MapManager.getManager().getFloor(newLocation.getFloor()).getIconList().remove(icon);
                MapManager.getManager().getFloor(newLocation.getFloor()).getIconList().add(newIcon);
                clearPopupForm();
                locationForm(event, newIcon);
                // MapController.getController().checkDropDown();
              });
    } else {
      field2.setPromptText("Node ID: " + icon.getLocation().getNodeID());
      field3.setPromptText("X-Coordinate: " + icon.getLocation().getXCoord());
      field4.setPromptText("Y-Coordinate: " + icon.getLocation().getYCoord());
      comboBox1.setValue(icon.getLocation().getFloor());
      comboBox1.setPromptText(icon.getLocation().getFloor());
      field5.setPromptText("Building: " + icon.getLocation().getBuilding());
      field6.setPromptText("Node Type: " + icon.getLocation().getNodeType());
      field7.setPromptText("Long Name: " + icon.getLocation().getLongName());
      field8.setPromptText("Short Name: " + icon.getLocation().getShortName());
      content
          .getChildren()
          .addAll(field2, field3, field4, field5, comboBox1, field6, field7, field8);
      field1.setText(icon.getLocation().getNodeID());
      submitIcon.setOnAction(
          event1 -> { // If user doesn't fill in information, assume old information is retained
            if (!(field2.getText().equals(icon.getLocation().getNodeID())
                && field3.getText().equals("" + icon.getLocation().getXCoord())
                && field4.getText().equals("" + icon.getLocation().getYCoord())
                && comboBox1.getValue().equals(icon.getLocation().getFloor())
                && field5.getText().equals(icon.getLocation().getBuilding())
                && field6.getText().equals(icon.getLocation().getNodeType())
                && field7.getText().equals(icon.getLocation().getLongName())
                && field8.getText().equals(icon.getLocation().getShortName()))) {
              if (field2.getText().isEmpty()) {
                field2.setText(icon.getLocation().getNodeID());
              }
              if (field3.getText().isEmpty()) {
                field3.setText("" + icon.getLocation().getXCoord());
              }
              if (field4.getText().isEmpty()) {
                field4.setText("" + icon.getLocation().getYCoord());
              }
              if (field5.getText().isEmpty()) {
                field5.setText(icon.getLocation().getBuilding());
              }
              if (field6.getText().isEmpty()) {
                field6.setText(icon.getLocation().getNodeType());
              }
              if (field7.getText().isEmpty()) {
                field7.setText(icon.getLocation().getLongName());
              }
              if (field8.getText().isEmpty()) {
                field8.setText("" + icon.getLocation().getShortName());
              }
              MapManager.getManager()
                  .getFloor(icon.getLocation().getFloor())
                  .getIconList()
                  .remove(icon);
              deleteLocation(requestSystem.getLocation(field1.getText()).getIcon());

              Location l =
                  new Location(
                      field2.getText(),
                      Double.parseDouble(field3.getText()),
                      Double.parseDouble(field4.getText()),
                      icon.getLocation().getFloor(),
                      field5.getText(),
                      field6.getText(),
                      field7.getText(),
                      field8.getText());
              addLocation(l);
              clearPopupForm();
              LocationIcon newIcon = new LocationIcon(l);
              MapManager.getManager().getFloor(l.getFloor()).getIconList().add(newIcon);
              locationForm(event, newIcon);
              // MapController.getController().checkDropDown();
            } else {
              missingFields.setText("No information has been modified. Please input corrections");
              content.getChildren().add(missingFields);
            }
          });
    }
    stage.setTitle("Modify Existing Location");
    showPopUp();
  }

  @FXML
  public void locationRemoveForm(MouseEvent event, LocationIcon icon) {
    title.setText("Delete A Location");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    submitIcon.setText("Delete Location");
    field1.setPromptText("Old Node ID");
    if (icon == null) {
      content.getChildren().addAll(field1);
    } else {
      field1.setText(icon.getLocation().getNodeID());
    }
    submitIcon.setOnAction(
        event1 -> {
          if (icon != null) {
            deleteLocation(icon);
          } else {
            // TODO
          }
          closePopUp();
        });
  }

  @FXML
  public void requestForm(MouseEvent event) {
    title.setText("Request");
    content.getChildren().clear();
    formSetup(event);
    buttonBox.getChildren().addAll(returnButton, addButton, modifyButton, removeButton);
    addButton.setOnAction(
        event1 -> {
          requestAdditionForm(event, null);
        });
    modifyButton.setOnAction(
        event1 -> {
          requestModifyForm(event);
        });
    removeButton.setOnAction(
        event1 -> {
          requestRemoveForm(event);
        });
  }

  @FXML
  public void requestAdditionForm(MouseEvent event, LocationIcon icon) {
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
          fitServiceRequest(event, comboBox1.getValue().toString(), icon);
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
  public void fitServiceRequest(MouseEvent event, String serviceRequest, LocationIcon icon) {
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
              LabRequest request =
                  new LabRequest(
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      "",
                      "",
                      comboBox2.getValue().toString(),
                      comboBox3.getValue().toString());
              addRequest(event, icon, request);
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
              EquipmentDelivery request =
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

              addRequest(event, icon, request);
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
        submitIcon.setOnAction(
            event1 -> {
              /*MedicineDelivery request=new MedicineDelivery();
              addRequest(event, icon, request);*/
            });
        break;
      case "Internal Patient Transport":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Transport to");
        field4.setPromptText("Notes");
        content.getChildren().addAll(field2, field3, field4);
        submitIcon.setOnAction(
            event1 -> {
              /*MedicineDelivery request=new MedicineDelivery();
              addRequest(event, icon, request);*/
            });
        break;
      case "Laundry Request":
        field2.setPromptText("Notes");
        submitIcon.setOnAction(
            event1 -> {
              /*MedicineDelivery request=new MedicineDelivery();
              addRequest(event, icon, request);*/
            });
        break;
      case "Meal Delivery":
        field2.setPromptText("Allergies");
        field3.setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Cheeseburger", "Chicken Tender", "Hot Dog", "Fries", "Crackers"));
        submitIcon.setOnAction(
            event1 -> {
              /*MedicineDelivery request=new MedicineDelivery();
              addRequest(event, icon, request);*/
            });
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
        submitIcon.setOnAction(
            event1 -> {
              /*MedicineDelivery request=new MedicineDelivery();
              addRequest(event, icon, request);*/
            });
        break;
      case "Religious Request":
        field2.setPromptText("Patient ID");
        content.getChildren().addAll(field2);

        submitIcon.setOnAction(
            event1 -> {
              /*MedicineDelivery request=new MedicineDelivery();
              addRequest(event, icon, request);*/
            });
        break;
    }
    content.getChildren().addAll(comboBox3);
  }

  public void addRequest(MouseEvent event, LocationIcon icon, ServiceRequest request) {
    if (icon == null) {
      Location location =
          new Location(
              request.getType() + field2.getText() + field1.getText() + comboBox2.getValue(),
              event.getX(),
              event.getY(),
              MapController.getController().getFloor(),
              "Tower",
              "Lab Request",
              "LabRequest" + field2.getText() + field1.getText() + comboBox2.getValue(),
              "LabRequest" + field2.getText());
      MapManager.getManager()
          .getFloor(MapController.getController().getFloor())
          .getIconList()
          .add(new LocationIcon(location));
    } else {
      icon.addToRequests(request);
    }
    clearPopupForm();
    locationForm(event, icon);
  }

  @FXML
  public void requestModifyForm(MouseEvent event) {}

  @FXML
  public void requestRemoveForm(MouseEvent event) {}

  @FXML
  public void equipmentForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Equipment");
    content.getChildren().clear();
    formSetup(event);
    buttonBox.getChildren().addAll(returnButton, addButton, modifyButton, removeButton);
    addButton.setOnAction(
        event1 -> {
          equipmentAdditionForm(event, icon);
        });
    modifyButton.setOnAction(
        event1 -> {
          equipmentModifyForm(event, icon);
        });
    removeButton.setOnAction(
        event1 -> {
          equipmentRemoveForm(event, icon);
        });
    stage.setTitle("Equipment");
    if (!stage.isShowing()) {
      showPopUp();
    }
  }

  @FXML
  public void equipmentAdditionForm(MouseEvent event, EquipmentIcon icon) {
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

  @FXML
  public void equipmentModifyForm(MouseEvent event, EquipmentIcon icon) {}

  @FXML
  public void equipmentRemoveForm(MouseEvent event, EquipmentIcon icon) {}

  public boolean checkLocationFields() {
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
  void clearPopupForm() {
    field1.setText("");
    field2.setText("");
    field3.setText("");
    field4.setText("");
    field5.setText("");
    field6.setText("");
    field7.setText("");
    field8.setText("");
    field9.setText("");
    comboBox1.setValue("Status");
    comboBox2.setValue("Status");
    comboBox3.setValue("Status");
  }
}
