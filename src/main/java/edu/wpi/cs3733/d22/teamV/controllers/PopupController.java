package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.*;
import java.io.IOException;
import java.sql.SQLException;
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
  MapManager mapManager = MapManager.getManager();
  MapController mapController = MapController.getController();
  RequestSystem requestSystem = RequestSystem.getSystem();

  private static class SingletonHelper {
    private static final PopupController controller = new PopupController();
  }

  public static PopupController getController() {
    return SingletonHelper.controller;
  }

  public void init() {
    setUpPopup();
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

  /** */
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

  /** */
  @FXML
  public void formSetup(MouseEvent event) {
    // MapManager.getManager().isTempIconVisible(true);

    clearResponse.setOnAction(event1 -> clearPopupForm());
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          content.getChildren().clear();
          // MapManager.getManager().isTempIconVisible(false);
        });
    returnButton.setOnAction(
        event1 -> {
          clearPopupForm();
          content.getChildren().clear();
          iconWindow(event);
        });
  }

  /** Adds an icon to the map */
  void addIcon(Location location) {
    if (requestSystem.getLocation(location.getNodeID()) == null) {
      MapController.getController().addIcon(location.getIcon());
      clearPopupForm();
    }
  }

  /** Deletes an icon from the map and removes the request */
  void deleteIcon(Location location) {
    System.out.println("Deleted");
    for (ServiceRequest request : location.getIcon().getRequestsArr()) {
      try {
        RequestSystem.getSystem().removeServiceRequest(request);
      } catch (IOException | SQLException e) {
        e.printStackTrace();
      }
    }
    mapController.deleteIcon(location.getIcon());
    clearPopupForm();
  }

  /** */
  void deleteIcon(String nodeID) {
    mapController.deleteIcon(requestSystem.getLocationDao().getLocation(nodeID).getIcon());
    clearPopupForm();
  }

  /**
   * Shows the general location form (add, modify, and remove an unspecified location) if icon ==
   * null and shows the specific location information if icon !=null
   */
  @FXML
  public void locationForm(MouseEvent event, LocationIcon icon) {
    double xPos = event.getX() - 15;
    double yPos = event.getY() - 25;
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
      addButton.setOnAction(event1 -> locationAdditionForm(event));
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
    content.getChildren().clear();
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
        System.out.println(request.toString());
        Label idLabel = new Label("Employee: " + request.getHospitalEmployee().getEmployeeID());
        Label locationLabel =
            new Label(
                "X: " + icon.getLocation().getXCoord() + " Y: " + icon.getLocation().getYCoord());

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setValue(request.getStatus());
        updateStatus.setPromptText(request.getStatus());
        updateStatus.setOnAction(
            event1 -> {
              request.setStatus(updateStatus.getValue().toString());
            });
        Button deleteRequest = new Button("Delete");
        deleteRequest.setOnAction(
            event -> {
              icon.removeRequests(request);
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
    submitIcon.setText("Add Location");
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
    clearPopupForm();
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
    field8.setPromptText("Short Name");
    field7.setPromptText("Long Name");
    if (icon == null) {
      content
          .getChildren()
          .addAll(field1, field2, field3, field4, field5, comboBox1, field6, field7, field8);
      submitIcon.setOnAction(
          event1 -> {
            deleteIcon(field1.getText());

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

            addIcon(newLocation);
            locationForm(event, newLocation.getIcon());
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
            System.out.println(icon.getLocation().toString());
            if (!(field2.getText().equals(icon.getLocation().getNodeID())
                && field3.getText().equals(String.valueOf(icon.getLocation().getXCoord()))
                && field4.getText().equals(String.valueOf(icon.getLocation().getYCoord()))
                && comboBox1.getValue().equals(icon.getLocation().getFloor())
                && field5.getText().equals(icon.getLocation().getBuilding())
                && field6.getText().equals(icon.getLocation().getNodeType())
                && field7.getText().equals(icon.getLocation().getLongName())
                && field8.getText().equals(icon.getLocation().getShortName()))) {
              Location newLocation = ifFilterEmpty(icon);
              deleteIcon(icon.getLocation());

              System.out.println(newLocation.toString());
              addIcon(newLocation);
              clearPopupForm();
              MapController.getController().checkDropDown();

            } else {
              missingFields.setText("No information has been modified. Please input corrections");
              content.getChildren().add(missingFields);
            }
          });
    }
    stage.setTitle("Modify Existing Location");
    showPopUp();
  }

  /** If location modification fields are empty then the info from the old icon is filled in */
  public Location ifFilterEmpty(Icon icon) {
    Location location = new Location();
    if (field2.getText().isEmpty()) {
      field2.setText(icon.getLocation().getNodeID());
      location.setNodeID(icon.getLocation().getNodeID());
    } else {
      location.setNodeID(field2.getText());
    }
    if (field3.getText().isEmpty()) {
      field3.setText(String.valueOf(icon.getLocation().getXCoord()));
      System.out.println("'" + icon.getLocation().getXCoord() + "'");
      System.out.println("'" + field3.getText() + "'");
      location.setXCoord(icon.getLocation().getXCoord());
    } else {
      location.setXCoord(Double.parseDouble(field3.getText()));
    }
    if (field4.getText().isEmpty()) {
      field4.setText(String.valueOf(icon.getLocation().getYCoord()));
      System.out.println("'" + icon.getLocation().getYCoord() + "'");
      System.out.println("'" + field4.getText() + "'");
      location.setYCoord(icon.getLocation().getYCoord());
    } else {
      location.setYCoord(Double.parseDouble(field4.getText()));
    }
    if (comboBox1.getValue().equals("")) {
      location.setFloor(icon.getLocation().getFloor());
    } else {
      location.setFloor(comboBox1.getValue());
    }
    if (field5.getText().isEmpty()) {
      field5.setText(icon.getLocation().getBuilding());
      location.setBuilding(icon.getLocation().getBuilding());
    } else {
      location.setBuilding(field5.getText());
    }
    if (field6.getText().isEmpty()) {
      field6.setText(icon.getLocation().getNodeType());
      location.setNodeType(icon.getLocation().getNodeType());
    } else {
      location.setNodeType(field6.getText());
    }
    if (field7.getText().isEmpty()) {
      field7.setText(icon.getLocation().getLongName());
      location.setLongName(icon.getLocation().getLongName());
    } else {
      location.setLongName(field7.getText());
    }
    if (field8.getText().isEmpty()) {
      location.setShortName(icon.getLocation().getShortName());
    } else {
      location.setShortName(field8.getText());
    }
    return location;
  }

  /** */
  @FXML
  public void locationRemoveForm(MouseEvent event, LocationIcon icon) {
    title.setText("Delete A Location");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    submitIcon.setText("Delete Location");
    field1.setPromptText("Old Node ID");
    if (icon == null) {
      buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
      content.getChildren().addAll(field1);
    } else {
      buttonBox.getChildren().addAll(returnButton, submitIcon, closeButton);
      field1.setText(icon.getLocation().getNodeID());
    }
    submitIcon.setOnAction(
        event1 -> {
          if (icon != null) {
            deleteIcon(icon.getLocation());
          } else {
            String nodeID = field1.getText();
            deleteIcon(nodeID);
          }
          closePopUp();
        });
  }

  /** */
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

  /** */
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

  /** Creates and adds a service request to icon based off the type and the field info */
  @FXML
  public void fitServiceRequest(MouseEvent event, String serviceRequest, LocationIcon icon) {
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    submitIcon.setText("Add Request");
    field1.setPromptText("Employee ID");
    comboBox3 =
        new JFXComboBox<>(FXCollections.observableArrayList("Not Started", "Processing", "Done"));
    comboBox3.setPromptText("Not Started");
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    content.getChildren().addAll(comboBox1, field1);

    switch (serviceRequest) {
      case "Lab Request":
        field2.setPromptText("Patient ID");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood Sample", "Urine Sample", "X-Ray", "CAT", "MRI"));
        content.getChildren().addAll(field2, comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              LabRequest request =
                  new LabRequest(
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      icon.getLocation().getNodeID(),
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
                      icon.getLocation().getNodeID(),
                      comboBox2.getValue().toString(),
                      field4.getText(),
                      Integer.parseInt(field3.getText()),
                      comboBox3.getValue().toString());

              addRequest(event, icon, request);
            });
        break;
      case "Medicine Delivery":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Dosage");
        field4.setPromptText("Request Details");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Tylenol", "Morphine", "Epinephrine", "Adderall", "Cyclosporine"));
        content.getChildren().addAll(field2, field3, field4, comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              MedicineDelivery request =
                  new MedicineDelivery(
                      icon.getLocation().getNodeID(),
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      comboBox2.getValue(),
                      field3.getText(),
                      comboBox3.getValue(),
                      field4.getText());
              addRequest(event, icon, request);
            });
        break;
      case "Internal Patient Transport":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Notes");
        content.getChildren().addAll(field2, field3);
        submitIcon.setOnAction(
            event1 -> {
              InternalPatientTransportation request =
                  new InternalPatientTransportation(
                      icon.getLocation().getNodeID(),
                      Integer.parseInt(field2.getText()),
                      Integer.parseInt(field1.getText()),
                      field3.getText());
              addRequest(event, icon, request);
            });
        break;
      case "Laundry Request":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Notes");
        content.getChildren().addAll(field2, field3);
        submitIcon.setOnAction(
            event1 -> {
              LaundryRequest request =
                  new LaundryRequest(
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      icon.getLocation().getNodeID(),
                      field3.getText());
              addRequest(event, icon, request);
            });
        break;
      case "Meal Delivery":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Allergy");
        field4.setPromptText("Request Details");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Cheeseburger", "Chicken Nuggies", "Hot Dog", "Fries", "Crackers"));
        content.getChildren().addAll(field2, field3, field4, comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              MedicineDelivery request =
                  new MedicineDelivery(
                      icon.getLocation().getNodeID(),
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      comboBox2.getValue(),
                      field3.getText(),
                      comboBox3.getValue(),
                      field4.getText());
              addRequest(event, icon, request);
            });
        break;
      case "Sanitation Request":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood",
                    "Vomit",
                    "Other Bodily Fluids",
                    "Broken Glass",
                    "No Hazard/General Mess"));
        content.getChildren().addAll(field2, field3, comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              SanitationRequest request =
                  new SanitationRequest(
                      Integer.parseInt(field1.getText()),
                      Integer.parseInt(field2.getText()),
                      icon.getLocation().getNodeID(),
                      comboBox2.getValue(),
                      field3.getText());

              addRequest(event, icon, request);
            });
        break;
      case "Religious Request":
        field2.setPromptText("Patient ID");
        field3.setPromptText("Religion");
        field4.setPromptText("Special Requests");

        content.getChildren().addAll(field2);

        submitIcon.setOnAction(
            event1 -> {
              ReligiousRequest request =
                  new ReligiousRequest(
                      Integer.parseInt(field2.getText()),
                      Integer.parseInt(field1.getText()),
                      icon.getLocation().getNodeID(),
                      field3.getText(),
                      field4.getText());
              addRequest(event, icon, request);
            });
        break;
    }
    content.getChildren().addAll(comboBox3);
  }

  /** Adds the service request to the icon's request list */
  public void addRequest(MouseEvent event, LocationIcon icon, ServiceRequest request) {
    if (icon == null) {
      Location location =
          new Location(
              request.getType() + field2.getText() + field1.getText() + comboBox2.getValue(),
              event.getX(),
              event.getY(),
              MapController.getController().getFloor(),
              "Tower",
              request.getType(),
              request.getType() + field2.getText() + field1.getText() + comboBox2.getValue(),
              request.getType() + field2.getText());
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

  /** */
  @FXML
  public void requestModifyForm(MouseEvent event) {}

  /** */
  @FXML
  public void requestRemoveForm(MouseEvent event) {}

  /** Sets up and displays the popup's equipment addition, modification, and removal buttons */
  @FXML
  public void equipmentForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Equipment");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    formSetup(event);
    buttonBox.getChildren().addAll(returnButton, addButton, modifyButton, removeButton);
    if (icon != null) {
      insertEquipment(icon);
    }
    addButton.setText("Add Equipment");
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

  /** Displays Equipment addition form */
  @FXML
  public void equipmentAdditionForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Add Equipment");
    submitIcon.setText("Add Equipment");
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);

    field1.setPromptText("Equipment ID");
    field2.setPromptText("Type of Equipment");
    field3.setPromptText("Notes");
    comboBox1.setValue("Status");
    comboBox1 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    content.getChildren().clear();
    content.getChildren().addAll(field1, field2, field3, comboBox1);

    submitIcon.setOnAction(
        event1 -> {
          if (icon == null) {
            Equipment equipment =
                new Equipment(
                    field1.getText(),
                    field2.getText(),
                    MapController.getController().currFloor.getFloorName(),
                    event.getX(),
                    event.getY(),
                    field3.getText(),
                    false);
            equipment.setIcon(
                new EquipmentIcon(
                    new Location(
                        field1.getText() + field2.getText() + comboBox1.getValue(),
                        event.getX(),
                        event.getY(),
                        "Tower",
                        MapController.getController().currFloor.getFloorName(),
                        field1.getText(),
                        "",
                        "")));
            addEquipmentIcon(equipment);
            clearPopupForm();
            closePopUp();
          } else {
            Equipment equipment =
                new Equipment(
                    field1.getText(),
                    field2.getText(),
                    MapController.getController().currFloor.getFloorName(),
                    event.getX(),
                    event.getY(),
                    field3.getText(),
                    false);
            submitIcon.setOnAction(
                event2 -> {
                  boolean isDirty = false;
                  if (comboBox1.getValue().equals("Dirty")) {
                    isDirty = true;
                  } else {
                    isDirty = false;
                  }
                  equipment.setIsDirty(isDirty);
                  icon.getEquipmentList().add(equipment);
                  clearPopupForm();
                  closePopUp();
                });
          }
        });
    // Scene and Stage
    stage.setTitle("Equipment");
    showPopUp();
  }

  /** adds equipment icon */
  public void addEquipmentIcon(Equipment equipment) {
    MapController.getController().addEquipmentIcon(equipment);
  }

  /** deletes equipment icon */
  public void deleteEquipmentIcon(Equipment equipment) {
    RequestSystem.getSystem().deleteEquipment(equipment);
    MapManager.getManager().setUpFloors();
  }

  /** Populates a location icon's popup window with its service requests */
  @FXML
  public void insertEquipment(EquipmentIcon icon) {
    ObservableList<String> statusStrings = FXCollections.observableArrayList("Clean", "Dirty");

    if (icon.getEquipmentList().size() > 0) {
      VBox vBox = new VBox();
      ScrollPane scrollPane = new ScrollPane(vBox);
      scrollPane.setFitToHeight(true);
      scrollPane.setPannable(false);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      vBox.setPrefWidth(450);
      vBox.setPrefHeight(400);
      for (Equipment equipment : icon.getEquipmentList()) {
        Label idLabel = new Label("ID: " + equipment.getID());
        Button deleteEquipment = new Button("Delete");
        deleteEquipment.setOnAction(
            event -> {
              icon.removeEquipment(equipment);
              if (icon.getEquipmentList().size() == 0) {
                deleteEquipmentIcon(equipment);
              }
            });
        Label locationLabel =
            new Label(
                "X: " + icon.getLocation().getXCoord() + " Y: " + icon.getLocation().getYCoord());

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setPromptText(equipment.getIsDirtyString());
        updateStatus.setValue(equipment.getIsDirtyString());
        updateStatus.setOnAction(
            event1 -> {
              if (comboBox2.getValue().equals("Dirty")) {
                equipment.setIsDirty(true);
              } else {
                equipment.setIsDirty(false);
              }
            });
        HBox hbox = new HBox(15, updateStatus, deleteEquipment);
        Accordion accordion =
            new Accordion(
                new TitledPane(
                    equipment.getName()
                        + " ("
                        + equipment.getIsDirtyString()
                        + "): "
                        + equipment.getDescription(),
                    new VBox(15, idLabel, locationLabel, hbox)));
        accordion.setPrefWidth(450);
        vBox.getChildren().add(accordion);
      }
      content.getChildren().add(scrollPane);
    }
  }

  /** Displays the equipment modification form */
  @FXML
  public void equipmentModifyForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Modify Equipment");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    clearPopupForm();
    submitIcon.setText("Modify Equipment");
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    field1.setPromptText("Old Equipment ID");
    field2.setPromptText("Equipment ID");
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
    field6.setPromptText("Type");
    field7.setPromptText("Description");
    comboBox2.setValue("Status");
    comboBox2 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    content
        .getChildren()
        .addAll(field1, field2, field3, field4, field5, comboBox1, field6, field7, comboBox2);
    submitIcon.setOnAction(
        event1 -> {
          deleteEquipmentIcon(RequestSystem.getSystem().getEquipment(field1.getText()));

          Equipment equipment =
              new Equipment(
                  field2.getText(),
                  field6.getText(),
                  comboBox1.getValue().toString(),
                  Double.parseDouble(field3.getText()),
                  Double.parseDouble(field4.getText()),
                  field7.getText(),
                  false);
          if (comboBox2.getValue().equals("Dirty")) {
            equipment.setIsDirty(true);
          } else {
            equipment.setIsDirty(false);
          }
          addEquipmentIcon(equipment);
          closePopUp();
        });

    stage.setTitle("Modify Equipment");
    showPopUp();
  }

  /** Displays the equipment removal form */
  @FXML
  public void equipmentRemoveForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Delete Equipment");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    submitIcon.setText("Delete Equipment");
    field1.setPromptText("Equipment ID");
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    content.getChildren().addAll(field1);
    submitIcon.setOnAction(
        event1 -> {
          deleteEquipmentIcon(RequestSystem.getSystem().getEquipment(field1.getText()));
          closePopUp();
        });
  }

  /** Makes sure the location fields aren't empty */
  public boolean checkLocationFields() {
    return !field1.getText().isEmpty()
        && !field2.getText().isEmpty()
        && !field3.getText().isEmpty()
        && !field4.getText().isEmpty();
  }

  /** Gets a new location based on the field info and mouse position */
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

  /** Clears the pop up form fields */
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
    comboBox1.setPromptText("Status");
    comboBox2.setPromptText("Status");
    comboBox3.setPromptText("Status");
  }
}
