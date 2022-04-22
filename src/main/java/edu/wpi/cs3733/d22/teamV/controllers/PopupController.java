package edu.wpi.cs3733.d22.teamV.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.map.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.*;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
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
  @FXML TextField[] fields = new TextField[10];
  @FXML JFXComboBox<String> comboBox1 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox2 = new JFXComboBox<>();
  @FXML JFXComboBox<String> comboBox3 = new JFXComboBox<>();
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
      MapManager.getManager().setUpFloors();
      MapController.getController().setFloor(MapController.getController().getFloorName());
      content.getChildren().clear();
      clearPopupForm();
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
  private void setUpPopup() {
    stage.setAlwaysOnTop(true);
    title.setTextFill(Color.WHITE);
    title.setFont(new Font("System Bold", 28));
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setStyle("-fx-background-color: #012D5Aff;");

    buttonBox.setAlignment(Pos.CENTER);
    content.setAlignment(Pos.CENTER);
    contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    contentScroll.setPrefHeight(400);
    contentScroll.setFitToWidth(true);

    sceneVbox.getChildren().addAll(titleBox, buttonBox, contentScroll);
    sceneVbox.setAlignment(Pos.TOP_CENTER);

    for (int i = 0; i < fields.length; i++) {
      fields[i] = new TextField();
      fields[i].setMaxWidth(250);
    }

    submitIcon.setMinWidth(100);
    clearResponse.setMinWidth(100);

    stage.setMaxHeight(400);
    stage.setMinHeight(400);
    stage.setOnCloseRequest(
        event -> {
          clearPopupForm();
        });
    clearResponse.setOnAction(
        event1 -> {
          clearPopupForm();
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          clear();
        });
  }

  private void insertFields() {
    for (TextField field : fields) {
      if (!field.getPromptText().isEmpty()) {
        Label prompt = new Label(field.getPromptText() + ": ");
        prompt.setPrefWidth(100);
        prompt.setAlignment(Pos.CENTER_LEFT);
        HBox hBox = new HBox(prompt, field);
        hBox.setAlignment(Pos.CENTER);
        content.getChildren().add(hBox);
      }
    }
  }

  /** Makes sure the location fields aren't empty */
  private boolean checkFields() {
    for (TextField field : fields) {
      if ((!field.getPromptText().isEmpty())
          && field.getText().isEmpty()
          && !content.getChildren().contains(missingFields)) {
        missingFields.setText("Please fill out all fields");
        content.getChildren().add(missingFields);
        return false;
      }
    }
    System.out.println("Valid submission");
    return true;
  }

  /** Gets a new location based on the field info and mouse position */
  private Location getLocation(double xPos, double yPos, String floor) {
    return new Location(
        fields[0].getText(),
        xPos,
        yPos,
        floor,
        "Tower",
        fields[1].getText(),
        fields[2].getText(),
        fields[3].getText());
  }

  private String convertFloor(String floor) {
    switch (floor) {
      case "Ground Floor":
        return "G";
      case "Lower Level 2":
        return "L2";
      case "Lower Level 1":
        return "L1";
      case "1st Floor":
        return "1";
      case "2nd Floor":
        return "2";
      case "3rd Floor":
        return "3";
      case "4th Floor":
        return "4";
      case "5th Floor":
        return "5";
      default:
        return floor;
    }
  }

  /** Clears the pop up form fields */
  @FXML
  private void clearPopupForm() {
    for (TextField field : fields) {
      field.setText("");
      field.setPromptText("");
    }
    comboBox1.setValue("Status");
    comboBox2.setValue("Status");
    comboBox3.setValue("Status");
    comboBox1.setPromptText("Status");
    comboBox2.setPromptText("Status");
    comboBox3.setPromptText("Status");
  }

  private void clear(String stageTitle, String headerTitle) {
    clear();
    title.setText(headerTitle);
    stage.setTitle(stageTitle);
  }

  private void clear() {
    clearPopupForm();
    content.getChildren().clear();
    buttonBox.getChildren().clear();
  }

  @FXML
  public void iconWindow(MouseEvent event) {
    clear("Options", "Please choose an option");
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
    showPopUp();
  }

  void addIcon(Location location) {
    if (requestSystem.getLocation(location.getNodeID()) == null) {
      MapController.getController().addIcon(location.getIcon());
      clearPopupForm();
    }
  }

  void deleteIcon(Location location) throws SQLException, IOException {
    System.out.println("Deleted");
    RequestSystem.getSystem().deleteLocation(location.getNodeID());
    MapManager.getManager().setUpFloors();
    MapManager.getManager().setUpFloors();
    clear();
  }

  void deleteIcon(String nodeID) {
    mapController.deleteIcon(requestSystem.getLocation(nodeID).getIcon());
    clear();
  }

  /**
   * Shows the general location form (add, modify, and remove an unspecified location) if icon ==
   * null and shows the specific location information if icon !=null
   */
  @FXML
  public void locationForm(MouseEvent event, LocationIcon icon) {
    addButton.setText("Add");
    modifyButton.setText("Modify");
    removeButton.setText("Remove");
    if (icon == null) {
      clear("Location", "Location");
      addButton.setOnAction(
          event1 -> {
            locationAdditionForm(event);
          });
      addButton.setText("Add a Location");
    } else {
      clear(icon.getLocation().getLongName(), icon.getLocation().getShortName());
      // Populates a location icon's popup window with its service requests
      if (icon.getRequestsArr().size() > 0) {
        content.getChildren().add(icon.compileList());
      }
      addButton.setText("Add a Service Request");
      addButton.setOnAction(
          event1 -> {
            requestAdditionForm(event, icon);
          });
    }
    modifyButton.setOnAction(
        event1 -> {
          locationModifyForm(event, icon);
        });
    removeButton.setOnAction(
        event1 -> {
          locationRemoveForm(event, icon);
        });
    buttonBox.getChildren().addAll(addButton, modifyButton, removeButton, closeButton);
    if (!stage.isShowing()) {
      showPopUp();
    }
  }

  /** Opens a form that allows users to create a new location */
  @FXML
  public void locationAdditionForm(MouseEvent event) {
    clear("Add a Location", "Add a Location");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    // Form
    submitIcon.setText("Add Location");
    fields[0].setPromptText("Node ID");
    fields[1].setPromptText("Node Type");
    fields[2].setPromptText("Short Name");
    fields[3].setPromptText("Long Name");
    insertFields();
    submitIcon.setOnAction(
        event1 -> {
          if (checkFields()) {
            RequestSystem.getSystem()
                .addLocation(
                    new Location(
                        fields[0].getText(),
                        event.getX(),
                        event.getY(),
                        MapController.getController().getFloorName(),
                        "Tower",
                        fields[1].getText(),
                        fields[3].getText(),
                        fields[2].getText()));
            MapManager.getManager().setUpFloors();
            closePopUp();
          }
        });
    showPopUp();
  }

  /**
   * Opens a form that allows users to modify locations if icon == null then the user must put in an
   * old node ID, if icon !=null then field 1 isn't shown
   */
  @FXML
  public void locationModifyForm(MouseEvent event, LocationIcon icon) {
    clear("Modify a Location", "Modify a Location");
    submitIcon.setText("Modify Location");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    fields[0].setPromptText("Old Node ID");
    fields[1].setPromptText("Node ID");
    fields[2].setPromptText("X-Coordinate");
    fields[3].setPromptText("Y-Coordinate");
    comboBox1 =
        new JFXComboBox<>(
            FXCollections.observableArrayList(
                "Ground Floor",
                "Lower Level 2",
                "Lower Level 1",
                "1st Floor",
                "2nd Floor",
                "3rd Floor",
                "4th Floor",
                "5th Floor"));
    fields[4].setPromptText("Building");
    fields[5].setPromptText("Node Type");
    fields[7].setPromptText("Short Name");
    fields[6].setPromptText("Long Name");
    if (icon == null) {
      content
          .getChildren()
          .addAll(
              fields[0], fields[1], fields[2], fields[3], fields[4], comboBox1, fields[5],
              fields[6], fields[7]);
      submitIcon.setOnAction(
          event1 -> {
            if (checkFields()) {
              deleteIcon(fields[0].getText());

              Location newLocation =
                  new Location(
                      fields[1].getText(),
                      Double.parseDouble(fields[2].getText()),
                      Double.parseDouble(fields[3].getText()),
                      convertFloor(comboBox1.getValue()),
                      fields[4].getText(),
                      fields[5].getText(),
                      fields[6].getText(),
                      fields[7].getText());

              addIcon(newLocation);
              locationForm(event, newLocation.getIcon());
              closePopUp();
            }
          });
    } else {
      fields[1].setPromptText("Node ID: " + icon.getLocation().getNodeID());
      fields[2].setPromptText("X-Coordinate: " + icon.getLocation().getXCoord());
      fields[3].setPromptText("Y-Coordinate: " + icon.getLocation().getYCoord());
      comboBox1.setValue(icon.getLocation().getFloor());
      comboBox1.setPromptText(icon.getLocation().getFloor());
      fields[4].setPromptText("Building: " + icon.getLocation().getBuilding());
      fields[5].setPromptText("Node Type: " + icon.getLocation().getNodeType());
      fields[6].setPromptText("Long Name: " + icon.getLocation().getLongName());
      fields[7].setPromptText("Short Name: " + icon.getLocation().getShortName());
      content.getChildren().add(comboBox1);
      insertFields();
      submitIcon.setOnAction(
          event1 -> { // If user doesn't fill in information, assume old information is retained
            System.out.println(icon.getLocation().toString());
            if (!(fields[1].getText().equals(icon.getLocation().getNodeID())
                && fields[2].getText().equals(String.valueOf(icon.getLocation().getXCoord()))
                && fields[3].getText().equals(String.valueOf(icon.getLocation().getYCoord()))
                && convertFloor(comboBox1.getValue()).equals(icon.getLocation().getFloor())
                && fields[4].getText().equals(icon.getLocation().getBuilding())
                && fields[5].getText().equals(icon.getLocation().getNodeType())
                && fields[6].getText().equals(icon.getLocation().getLongName())
                && fields[7].getText().equals(icon.getLocation().getShortName()))) {
              Location newLocation = ifFilterEmpty(icon);
              try {
                deleteIcon(icon.getLocation());
                System.out.println(newLocation.toString());
                addIcon(newLocation);
                clearPopupForm();
                MapController.getController().checkFilter();
                closePopUp();
              } catch (SQLException | IOException e) {
                e.printStackTrace();
              }
            } else {
              missingFields.setText("No information has been modified. Please input corrections");
              content.getChildren().add(missingFields);
            }
          });
    }
    showPopUp();
  }

  public Location ifFilterEmpty(Icon icon) {
    Location location = new Location();
    if (fields[1].getText().isEmpty()) {
      fields[1].setText(icon.getLocation().getNodeID());
      location.setNodeID(icon.getLocation().getNodeID());
    } else {
      location.setNodeID(fields[1].getText());
    }
    if (fields[2].getText().isEmpty()) {
      fields[2].setText(String.valueOf(icon.getLocation().getXCoord()));
      System.out.println("'" + icon.getLocation().getXCoord() + "'");
      System.out.println("'" + fields[2].getText() + "'");
      location.setXCoord(icon.getLocation().getXCoord());
    } else {
      location.setXCoord(Double.parseDouble(fields[2].getText()));
    }
    if (fields[3].getText().isEmpty()) {
      fields[3].setText(String.valueOf(icon.getLocation().getYCoord()));
      System.out.println("'" + icon.getLocation().getYCoord() + "'");
      System.out.println("'" + fields[3].getText() + "'");
      location.setYCoord(icon.getLocation().getYCoord());
    } else {
      location.setYCoord(Double.parseDouble(fields[3].getText()));
    }
    if (comboBox1.getValue().equals("")) {
      location.setFloor(icon.getLocation().getFloor());
    } else {
      location.setFloor(convertFloor(comboBox1.getValue()));
    }
    if (fields[4].getText().isEmpty()) {
      fields[4].setText(icon.getLocation().getBuilding());
      location.setBuilding(icon.getLocation().getBuilding());
    } else {
      location.setBuilding(fields[4].getText());
    }
    if (fields[5].getText().isEmpty()) {
      fields[5].setText(icon.getLocation().getNodeType());
      location.setNodeType(icon.getLocation().getNodeType());
    } else {
      location.setNodeType(fields[5].getText());
    }
    if (fields[6].getText().isEmpty()) {
      fields[6].setText(icon.getLocation().getLongName());
      location.setLongName(icon.getLocation().getLongName());
    } else {
      location.setLongName(fields[6].getText());
    }
    if (fields[7].getText().isEmpty()) {
      location.setShortName(icon.getLocation().getShortName());
    } else {
      location.setShortName(fields[7].getText());
    }
    return location;
  }

  @FXML
  public void locationRemoveForm(MouseEvent event, LocationIcon icon) {
    submitIcon.setText("Delete Location");
    if (icon == null) {
      clear("Delete a Location", "Delete a Location");
      fields[0].setPromptText("Node ID");
      buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    } else {
      clear(icon.getLocation().getLongName(), icon.getLocation().getShortName());
      buttonBox.getChildren().addAll(submitIcon, closeButton);
    }
    insertFields();
    submitIcon.setOnAction(
        event1 -> {
          if (icon != null) {
            closePopUp();
            try {
              deleteIcon(icon.getLocation());
            } catch (SQLException | IOException e) {
              e.printStackTrace();
            }
          } else {
            if (checkFields()) {
              String nodeID = fields[0].getText();
              deleteIcon(nodeID);
              closePopUp();
            }
          }
          closePopUp();
        });
  }

  @FXML
  public void requestForm(MouseEvent event) {
    clear("Service Request", "Service Request");
    buttonBox.getChildren().addAll(addButton);
    addButton.setOnAction(
        event1 -> {
          requestAdditionForm(event, null);
        });
  }

  @FXML
  public void requestAdditionForm(MouseEvent event, LocationIcon icon) {
    clear("Add A Service Request", "Add A Service Request");
    addButton.setText("Add Request");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    // Form
    comboBox1.setPromptText("Service Request");
    comboBox2 =
        new JFXComboBox<>(FXCollections.observableArrayList("Not Started", "Processing", "Done"));
    comboBox2.setPromptText("Request Status");
    comboBox1 =
        new JFXComboBox<>(
            FXCollections.observableArrayList(
                "Lab Request",
                "Equipment Delivery Request",
                "Medicine Delivery Request",
                "Internal Patient Transport Request",
                "Laundry Request",
                "Meal Delivery Request",
                "Sanitation Request",
                "Religious Request",
                "Robot Request"));
    comboBox1.setOnAction(
        event1 -> {
          fitServiceRequest(event, comboBox1.getValue(), icon);
        });
    content.getChildren().add(comboBox1);
  }

  /** Creates and adds a service request to icon based off the type and the field info */
  @FXML
  public void fitServiceRequest(MouseEvent event, String serviceRequest, LocationIcon icon) {
    buttonBox.getChildren().clear();
    content.getChildren().clear();
    content.getChildren().add(comboBox1);
    submitIcon.setText("Add Request");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    fields[0].setPromptText("Employee ID");
    comboBox3 =
        new JFXComboBox<>(FXCollections.observableArrayList("Not Started", "Processing", "Done"));
    comboBox3.setPromptText("Not Started");
    switch (serviceRequest) {
      case "Lab Request":
        fields[1].setPromptText("Patient ID");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood Sample", "Urine Sample", "X-Ray", "CAT", "MRI"));
        comboBox2.setPromptText("Lab");
        insertFields();
        content.getChildren().addAll(comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                LabRequest request =
                    new LabRequest(
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        icon.getLocation().getNodeID(),
                        comboBox2.getValue(),
                        comboBox3.getValue());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Equipment Delivery Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Quantity");
        fields[3].setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Bed", "Portable X-Ray", "Blood Infusion Machine", "Patient Recliner"));
        comboBox2.setPromptText("Equipment");
        insertFields();
        content.getChildren().addAll(comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                EquipmentDelivery request =
                    new EquipmentDelivery(
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        icon.getLocation().getNodeID(),
                        comboBox2.getValue(),
                        fields[3].getText(),
                        Integer.parseInt(fields[2].getText()),
                        comboBox3.getValue());

                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Medicine Delivery Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Dosage");
        fields[3].setPromptText("Request Details");
        insertFields();
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Tylenol", "Morphine", "Epinephrine", "Adderall", "Cyclosporine"));
        comboBox2.setPromptText("Medicine");
        content.getChildren().addAll(comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                MedicineDelivery request =
                    new MedicineDelivery(
                        icon.getLocation().getNodeID(),
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        comboBox2.getValue(),
                        fields[2].getText(),
                        comboBox3.getValue(),
                        fields[3].getText());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Internal Patient Transport Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Notes");
        insertFields();
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                InternalPatientTransportation request =
                    new InternalPatientTransportation(
                        icon.getLocation().getNodeID(),
                        Integer.parseInt(fields[1].getText()),
                        Integer.parseInt(fields[0].getText()),
                        fields[2].getText());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Laundry Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Notes");
        insertFields();
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                LaundryRequest request =
                    new LaundryRequest(
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        icon.getLocation().getNodeID(),
                        fields[2].getText(),
                        comboBox3.getValue());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Meal Delivery Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Allergy");
        fields[3].setPromptText("Request Details");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Cheeseburger", "Chicken Nuggies", "Hot Dog", "Fries", "Crackers"));
        comboBox2.setPromptText("Meal");
        insertFields();
        content.getChildren().add(comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                MealRequest request =
                    new MealRequest(
                        icon.getLocation().getNodeID(),
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        comboBox2.getValue(),
                        fields[2].getText(),
                        comboBox3.getValue(),
                        fields[3].getText());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Sanitation Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Notes");
        insertFields();
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood",
                    "Vomit",
                    "Other Bodily Fluids",
                    "Broken Glass",
                    "No Hazard/General Mess"));
        comboBox2.setPromptText("Reason");
        content.getChildren().addAll(comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                SanitationRequest request =
                    new SanitationRequest(
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        icon.getLocation().getNodeID(),
                        comboBox2.getValue(),
                        fields[2].getText());

                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Religious Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Religion");
        fields[3].setPromptText("Special Requests");
        insertFields();

        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                ReligiousRequest request =
                    new ReligiousRequest(
                        Integer.parseInt(fields[1].getText()),
                        Integer.parseInt(fields[0].getText()),
                        icon.getLocation().getNodeID(),
                        fields[2].getText(),
                        fields[3].getText());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
      case "Robot Request":
        fields[1].setPromptText("Bot ID");
        fields[2].setPromptText("Details");
        insertFields();
        submitIcon.setOnAction(
            event1 -> {
              if (checkFields()) {
                RobotRequest request =
                    new RobotRequest(
                        Integer.parseInt(fields[0].getText()),
                        Integer.parseInt(fields[1].getText()),
                        icon.getLocation().getNodeID(),
                        fields[2].getText(),
                        comboBox3.getValue());
                addRequest(event, icon, request);
                closePopUp();
              }
            });
        break;
    }
    content.getChildren().addAll(comboBox3);
  }

  public void addRequest(MouseEvent event, LocationIcon icon, ServiceRequest request) {
    if (icon == null) {
      Location location =
          new Location(
              request.getType() + fields[1].getText() + fields[0].getText() + comboBox2.getValue(),
              event.getX(),
              event.getY(),
              MapController.getController().currFloor.getFloorName(),
              "Tower",
              request.getType(),
              request.getType() + fields[1].getText() + fields[0].getText() + comboBox2.getValue(),
              request.getType() + fields[1].getText());
      RequestSystem.getSystem().addLocation(location);
      MapController.getController().currFloor.getIconList().add(new LocationIcon(location));
    } else {
      RequestSystem.getSystem().addServiceRequest(request);
      icon.addToRequests(request);
    }
    clearPopupForm();
    locationForm(event, icon);
  }

  @FXML
  public void equipmentForm(MouseEvent event, EquipmentIcon icon) {
    clear("Equipment", "Equipment");
    if (icon != null) {
      insertEquipment(icon);
      buttonBox.getChildren().addAll(addButton, closeButton);
    } else {
      buttonBox.getChildren().addAll(addButton, modifyButton, removeButton, closeButton);
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

  @FXML
  public void equipmentAdditionForm(MouseEvent event, EquipmentIcon icon) {
    clear("Add Equipment", "Add Equipment");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    submitIcon.setText("Add Equipment");
    fields[0].setPromptText("Equipment ID");
    fields[1].setPromptText("Type of Equipment");
    fields[2].setPromptText("Notes");
    insertFields();
    comboBox1.setPromptText("Clean");
    comboBox1.setValue("Clean");
    comboBox1 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    content.getChildren().addAll(comboBox1);

    submitIcon.setOnAction(
        event1 -> {
          if (checkFields()) {
            if (icon == null) {
              Equipment equipment =
                  new Equipment(
                      fields[0].getText(),
                      fields[1].getText(),
                      MapController.getController().currFloor.getFloorName(),
                      event.getX(),
                      event.getY(),
                      fields[2].getText(),
                      false);
              equipment.setIcon(
                  new EquipmentIcon(
                      new Location(
                          fields[0].getText() + fields[1].getText() + comboBox1.getValue(),
                          event.getX(),
                          event.getY(),
                          "Tower",
                          MapController.getController().currFloor.getFloorName(),
                          fields[0].getText(),
                          "",
                          "")));
              addEquipmentIcon(equipment);
              clearPopupForm();
            } else {
              Equipment equipment =
                  new Equipment(
                      fields[0].getText(),
                      fields[1].getText(),
                      MapController.getController().currFloor.getFloorName(),
                      event.getX(),
                      event.getY(),
                      fields[2].getText(),
                      false);
              boolean isDirty = false;
              if (comboBox1.getValue().equals("Dirty")) {
                isDirty = true;
              } else {
                isDirty = false;
              }
              equipment.setIsDirty(isDirty);
              icon.getEquipmentList().add(equipment);
              clearPopupForm();
            }
            closePopUp();
          }
        });
    // Scene and Stage
    stage.setTitle("Equipment");
    showPopUp();
  }

  /** adds equipment icon */
  public void addEquipmentIcon(Equipment equipment) {
    RequestSystem.getSystem().addEquipment(equipment);
    MapManager.getManager().setUpFloors();
    MapController.getController().mapPane.getChildren().clear();
    MapController.getController().setFloor(MapController.getController().currFloor.getFloorName());
  }

  /** deletes equipment icon */
  public void deleteEquipmentIcon(Equipment equipment) {
    RequestSystem.getSystem().removeEquipment(equipment);
    MapManager.getManager().setUpFloors();
    MapController.getController().mapPane.getChildren().clear();
    MapController.getController().setFloor(MapController.getController().currFloor.getFloorName());
    // MapController.getController().setFloor(equipment.getFloor());
  }

  /** Populates a location icon's popup window with its service requests */
  @FXML
  public void insertEquipment(EquipmentIcon icon) {
    content.getChildren().clear();
    content.getChildren().addAll(icon.compileList());
  }
  // TODO Rework modification and deletion forms
  /** Displays the equipment modification form */
  @FXML
  public void equipmentModifyForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Modify Equipment");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    clearPopupForm();
    submitIcon.setText("Modify Equipment");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    fields[0].setPromptText("Old Equipment ID");
    fields[1].setPromptText("Equipment ID");
    fields[2].setPromptText("X-Coordinate");
    fields[3].setPromptText("Y-Coordinate");
    comboBox1.setPromptText("Floor");
    comboBox1 =
        new JFXComboBox<>(
            FXCollections.observableArrayList(
                "Ground Floor",
                "Lower Level 2",
                "Lower Level 1",
                "1st Floor",
                "2nd Floor",
                "3rd Floor",
                "4th Floor",
                "5th Floor"));
    fields[4].setPromptText("Building");
    fields[5].setPromptText("Type");
    fields[6].setPromptText("Description");
    comboBox2.setPromptText("Clean");
    comboBox2 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    insertFields();
    content.getChildren().addAll(comboBox1, comboBox2);
    submitIcon.setOnAction(
        event1 -> {
          if (checkFields()) {
            deleteEquipmentIcon(RequestSystem.getSystem().getEquipment(fields[0].getText()));

            Equipment equipment =
                new Equipment(
                    fields[1].getText(),
                    fields[5].getText(),
                    convertFloor(comboBox1.getValue()),
                    Double.parseDouble(fields[2].getText()),
                    Double.parseDouble(fields[3].getText()),
                    fields[6].getText(),
                    false);
            if (comboBox2.getValue().equals("Dirty")) {
              equipment.setIsDirty(true);
            } else {
              equipment.setIsDirty(false);
            }
            addEquipmentIcon(equipment);
            closePopUp();
          }
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
    fields[0].setPromptText("Equipment ID");
    buttonBox.getChildren().addAll(submitIcon, clearResponse, closeButton);
    content.getChildren().addAll(fields[0]);
    submitIcon.setOnAction(
        event1 -> {
          if (checkFields()) {
            deleteEquipmentIcon(RequestSystem.getSystem().getEquipment(fields[0].getText()));
            closePopUp();
          }
        });
  }
}
