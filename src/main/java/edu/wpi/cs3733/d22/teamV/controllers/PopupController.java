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
    clearResponse.setOnAction(
        event1 -> {
          clearPopupForm();
        });
    closeButton.setOnAction(
        event1 -> {
          closePopUp();
          content.getChildren().clear();
        });
    returnButton.setOnAction(
        event1 -> {
          clearPopupForm();
          content.getChildren().clear();
          iconWindow(event);
        });
  }

  void addIcon(Location location) {
    if (requestSystem.getLocation(location.getNodeID()) == null) {
      MapController.getController().addIcon(location.getIcon());
      clearPopupForm();
    }
  }

  void deleteIcon(Location location) throws SQLException, IOException {
    System.out.println("Deleted");
    for (ServiceRequest request : location.getIcon().getRequestsArr()) {
      RequestSystem.getSystem().removeServiceRequest(request);
    }
    mapController.deleteIcon(location.getIcon());
    clearPopupForm();
  }

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
        Label idLabel = new Label("Employee: " + request.getEmployee().getEmployeeID());
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
    content.getChildren().addAll(fields[0], fields[1], fields[2], fields[3]);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    // Form
    submitIcon.setText("Add Location");
    fields[0].setPromptText("Node ID");
    fields[1].setPromptText("Node Type");
    fields[2].setPromptText("Short Name");
    fields[3].setPromptText("Long Name");
    submitIcon.setOnAction(
        event1 -> {
          RequestSystem.getSystem()
              .getLocationDao()
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
        });
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
    fields[0].setPromptText("Old Node ID");
    fields[1].setPromptText("Node ID");
    fields[2].setPromptText("X-Coordinate");
    fields[3].setPromptText("Y-Coordinate");
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
            deleteIcon(fields[0].getText());

            Location newLocation =
                new Location(
                    fields[1].getText(),
                    Double.parseDouble(fields[2].getText()),
                    Double.parseDouble(fields[3].getText()),
                    comboBox1.getValue().toString(),
                    fields[4].getText(),
                    fields[5].getText(),
                    fields[6].getText(),
                    fields[7].getText());

            addIcon(newLocation);
            locationForm(event, newLocation.getIcon());
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
      content
          .getChildren()
          .addAll(
              fields[1], fields[2], fields[3], fields[4], comboBox1, fields[5], fields[6],
              fields[7]);
      fields[0].setText(icon.getLocation().getNodeID());
      submitIcon.setOnAction(
          event1 -> { // If user doesn't fill in information, assume old information is retained
            System.out.println(icon.getLocation().toString());
            if (!(fields[1].getText().equals(icon.getLocation().getNodeID())
                && fields[2].getText().equals(String.valueOf(icon.getLocation().getXCoord()))
                && fields[3].getText().equals(String.valueOf(icon.getLocation().getYCoord()))
                && comboBox1.getValue().equals(icon.getLocation().getFloor())
                && fields[4].getText().equals(icon.getLocation().getBuilding())
                && fields[5].getText().equals(icon.getLocation().getNodeType())
                && fields[6].getText().equals(icon.getLocation().getLongName())
                && fields[7].getText().equals(icon.getLocation().getShortName()))) {
              Location newLocation = ifFilterEmpty(icon);
              try {
                deleteIcon(icon.getLocation());
              } catch (SQLException | IOException e) {
                e.printStackTrace();
              }

              System.out.println(newLocation.toString());
              addIcon(newLocation);
              clearPopupForm();
              MapController.getController().checkFilter();

            } else {
              missingFields.setText("No information has been modified. Please input corrections");
              content.getChildren().add(missingFields);
            }
          });
    }
    stage.setTitle("Modify Existing Location");
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
      location.setFloor(comboBox1.getValue());
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
    title.setText("Delete A Location");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    submitIcon.setText("Delete Location");
    fields[0].setPromptText("Old Node ID");
    if (icon == null) {
      buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
      content.getChildren().addAll(fields[0]);
    } else {
      buttonBox.getChildren().addAll(returnButton, submitIcon, closeButton);
      fields[0].setText(icon.getLocation().getNodeID());
    }
    submitIcon.setOnAction(
        event1 -> {
          if (icon != null) {
            try {
              deleteIcon(icon.getLocation());
            } catch (SQLException | IOException e) {
              e.printStackTrace();
            }
          } else {
            String nodeID = fields[0].getText();
            deleteIcon(nodeID);
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
    for (TextField field : fields) {
      field = new TextField();
    }
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
    fields[0].setPromptText("Employee ID");
    comboBox3 =
        new JFXComboBox<>(FXCollections.observableArrayList("Not Started", "Processing", "Done"));
    comboBox3.setPromptText("Not Started");
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    content.getChildren().addAll(comboBox1, fields[0]);

    switch (serviceRequest) {
      case "Lab Request":
        fields[1].setPromptText("Patient ID");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood Sample", "Urine Sample", "X-Ray", "CAT", "MRI"));
        content.getChildren().addAll(fields[1], comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              LabRequest request =
                  new LabRequest(
                      Integer.parseInt(fields[0].getText()),
                      Integer.parseInt(fields[1].getText()),
                      icon.getLocation().getNodeID(),
                      comboBox2.getValue(),
                      comboBox3.getValue());
              addRequest(event, icon, request);
              closePopUp();
            });
        break;
      case "Equipment Delivery":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Quantity");
        fields[3].setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Bed", "Portable X-Ray", "Blood Infusion Machine", "Patient Recliner"));
        content.getChildren().addAll(fields[1], fields[2], fields[3], comboBox2);
        submitIcon.setOnAction(
            event1 -> {
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
            });
        break;
      case "Medicine Delivery":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Dosage");
        fields[3].setPromptText("Request Details");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Tylenol", "Morphine", "Epinephrine", "Adderall", "Cyclosporine"));
        content.getChildren().addAll(fields[1], fields[2], fields[3], comboBox2);
        submitIcon.setOnAction(
            event1 -> {
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
            });
        break;
      case "Internal Patient Transport":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Notes");
        content.getChildren().addAll(fields[1], fields[2]);
        submitIcon.setOnAction(
            event1 -> {
              InternalPatientTransportation request =
                  new InternalPatientTransportation(
                      icon.getLocation().getNodeID(),
                      Integer.parseInt(fields[1].getText()),
                      Integer.parseInt(fields[0].getText()),
                      fields[2].getText());
              addRequest(event, icon, request);
              closePopUp();
            });
        break;
      case "Laundry Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Notes");
        content.getChildren().addAll(fields[1], fields[2]);
        submitIcon.setOnAction(
            event1 -> {
              LaundryRequest request =
                  new LaundryRequest(
                      Integer.parseInt(fields[0].getText()),
                      Integer.parseInt(fields[1].getText()),
                      icon.getLocation().getNodeID(),
                      fields[2].getText(),
                      comboBox3.getValue());
              addRequest(event, icon, request);
              closePopUp();
            });
        break;
      case "Meal Delivery":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Allergy");
        fields[3].setPromptText("Request Details");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Cheeseburger", "Chicken Nuggies", "Hot Dog", "Fries", "Crackers"));
        content.getChildren().addAll(fields[1], fields[2], fields[3], comboBox2);
        submitIcon.setOnAction(
            event1 -> {
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
            });
        break;
      case "Sanitation Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Notes");
        comboBox2 =
            new JFXComboBox<>(
                FXCollections.observableArrayList(
                    "Blood",
                    "Vomit",
                    "Other Bodily Fluids",
                    "Broken Glass",
                    "No Hazard/General Mess"));
        content.getChildren().addAll(fields[1], fields[2], comboBox2);
        submitIcon.setOnAction(
            event1 -> {
              SanitationRequest request =
                  new SanitationRequest(
                      Integer.parseInt(fields[0].getText()),
                      Integer.parseInt(fields[1].getText()),
                      icon.getLocation().getNodeID(),
                      comboBox2.getValue(),
                      fields[2].getText());

              addRequest(event, icon, request);
              closePopUp();
            });
        break;
      case "Religious Request":
        fields[1].setPromptText("Patient ID");
        fields[2].setPromptText("Religion");
        fields[3].setPromptText("Special Requests");

        content.getChildren().addAll(fields[1]);

        submitIcon.setOnAction(
            event1 -> {
              ReligiousRequest request =
                  new ReligiousRequest(
                      Integer.parseInt(fields[1].getText()),
                      Integer.parseInt(fields[0].getText()),
                      icon.getLocation().getNodeID(),
                      fields[2].getText(),
                      fields[3].getText());
              addRequest(event, icon, request);
              closePopUp();
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
              "Lab Request",
              "LabRequest" + fields[1].getText() + fields[0].getText() + comboBox2.getValue(),
              "LabRequest" + fields[1].getText());
      MapController.getController().currFloor.getIconList().add(new LocationIcon(location));
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

  @FXML
  public void equipmentAdditionForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Add Equipment");
    submitIcon.setText("Add Equipment");
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);

    fields[0].setPromptText("Equipment ID");
    fields[1].setPromptText("Type of Equipment");
    fields[2].setPromptText("Notes");
    comboBox1.setValue("Status");
    comboBox1 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    content.getChildren().clear();
    content.getChildren().addAll(fields[0], fields[1], fields[2], comboBox1);

    submitIcon.setOnAction(
        event1 -> {
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
            closePopUp();
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
    RequestSystem.getSystem().getEquipmentDao().addEquipment(equipment);
    RequestSystem.getSystem().getEquipmentDao().saveToCSV();
    MapManager.getManager().setUpFloors();
    MapController.getController().mapPane.getChildren().clear();
    MapController.getController().setFloor(MapController.getController().currFloor.getFloorName());
  }

  /** deletes equipment icon */
  public void deleteEquipmentIcon(Equipment equipment) {
    RequestSystem.getSystem().getEquipmentDao().removeEquipment(equipment);
    RequestSystem.getSystem().getEquipmentDao().saveToCSV();
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

  /** Displays the equipment modification form */
  @FXML
  public void equipmentModifyForm(MouseEvent event, EquipmentIcon icon) {
    title.setText("Modify Equipment");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    clearPopupForm();
    submitIcon.setText("Modify Equipment");
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    fields[0].setPromptText("Old Equipment ID");
    fields[1].setPromptText("Equipment ID");
    fields[2].setPromptText("X-Coordinate");
    fields[3].setPromptText("Y-Coordinate");
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
    fields[4].setPromptText("Building");
    fields[5].setPromptText("Type");
    fields[6].setPromptText("Description");
    comboBox2.setValue("Status");
    comboBox2 = new JFXComboBox<>(FXCollections.observableArrayList("Clean", "Dirty"));
    content
        .getChildren()
        .addAll(
            fields[0], fields[1], fields[2], fields[3], fields[4], comboBox1, fields[5], fields[6],
            comboBox2);
    submitIcon.setOnAction(
        event1 -> {
          deleteEquipmentIcon(RequestSystem.getSystem().getEquipment(fields[0].getText()));

          Equipment equipment =
              new Equipment(
                  fields[1].getText(),
                  fields[5].getText(),
                  comboBox1.getValue().toString(),
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
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    content.getChildren().addAll(fields[0]);
    submitIcon.setOnAction(
        event1 -> {
          deleteEquipmentIcon(RequestSystem.getSystem().getEquipment(fields[0].getText()));
          closePopUp();
        });
  }

  /** Makes sure the location fields aren't empty */
  public boolean checkLocationFields() {
    return !fields[0].getText().isEmpty()
        && !fields[1].getText().isEmpty()
        && !fields[2].getText().isEmpty()
        && !fields[3].getText().isEmpty();
  }

  /** Gets a new location based on the field info and mouse position */
  public Location getLocation(double xPos, double yPos, String floor) {
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

  /** Clears the pop up form fields */
  @FXML
  void clearPopupForm() {
    for (TextField field : fields) {
      field.setText("");
    }
    comboBox1.setValue("Status");
    comboBox2.setValue("Status");
    comboBox3.setValue("Status");
  }
}
