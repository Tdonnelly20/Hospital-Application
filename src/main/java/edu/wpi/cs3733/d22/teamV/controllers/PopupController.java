package edu.wpi.cs3733.d22.teamV.controllers;

import static edu.wpi.cs3733.d22.teamV.main.Vdb.locationDao;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.map.RequestIcon;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import edu.wpi.cs3733.d22.teamV.servicerequests.LabRequest;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
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
import javafx.scene.text.TextAlignment;
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
          buttonBox.getChildren().clear();
          locationForm(event, null);
        });
    equipmentButton.setOnAction(
        event2 -> {
          buttonBox.getChildren().clear();
          equipmentForm(event);
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
          Label idLabel =
              new Label("Employee: " /*+ request.getHospitalEmployee().getHospitalID()*/);
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
        title.setText("Location");
        buttonBox.getChildren().clear();
        buttonBox.getChildren().addAll(addButton, modifyButton, closeButton);
        addButton.setText("Add a Service Request Here");
        modifyButton.setText("Modify Location");
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
  public void locationForm(MouseEvent event, LocationIcon icon) {
    buttonBox.getChildren().clear();
    content.getChildren().clear();
    modifyButton.setText("Modify Location");
    removeButton.setText("Remove Location");
    if (icon == null) {
      title.setText("Location");
      stage.setTitle("Location");
      buttonBox
          .getChildren()
          .addAll(returnButton, addButton, modifyButton, removeButton, closeButton);
      addButton.setOnAction(
          event1 -> {
            locationAdditionForm(event, icon);
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
          Label idLabel =
              new Label("Employee: " /*+ request.getHospitalEmployee().getHospitalID()*/);
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

  @FXML
  public void locationAdditionForm(MouseEvent event, LocationIcon icon) {
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

    submitIcon.setOnAction(
        event1 -> {
          locationDao.addLocation(
              new Location(
                  field1.getText(),
                  event.getX(),
                  event.getY(),
                  MapController.getController().getFloor(),
                  "Tower",
                  field2.getText(),
                  field3.getText(),
                  field4.getText()));
          clearPopupForm();
          closePopUp();
          // MapController.getController().populateFloorIconArr();
        });

    // Scene and Stage
    stage.setTitle("Add New Location");
    showPopUp();
  }

  @FXML
  public void locationModifyForm(MouseEvent event, LocationIcon icon) {
    title.setText("Modify A Location");
    content.getChildren().clear();
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    submitIcon.setText("Modify Location");
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
    content
        .getChildren()
        .addAll(field1, field2, field3, field4, field5, comboBox1, field6, field7, field8);
    submitIcon.setOnAction(
        event1 -> {
          try {
            locationDao.deleteLocation(field1.getText());
          } catch (SQLException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
          locationDao.addLocation( // TODO: fix modify location error
              new Location(
                  field2.getText(),
                  Double.parseDouble(field3.getText()),
                  Double.parseDouble(field4.getText()),
                  MapManager.getManager().getFloor(comboBox1.getValue().toString()).getFloorName(),
                  field5.getText(),
                  field6.getText(),
                  field7.getText(),
                  field8.getText()));
          clearPopupForm();
          locationForm(event, icon);
        });

    stage.setTitle("Modify Existing Location");
    showPopUp();
  }

  @FXML
  public void locationRemoveForm(MouseEvent event, LocationIcon icon) {
    title.setText("Delete A Location");
    content.getChildren().clear();
    content.getChildren().addAll(field1);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(returnButton, submitIcon, clearResponse, closeButton);
    submitIcon.setText("Delete Location");
    if (icon == null) {
      field1.setPromptText("Old Node ID");
      submitIcon.setOnAction(
          event1 -> {
            try {
              locationDao.deleteLocation(field1.getText());
            } catch (SQLException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
            MapController.getController().populateFloorIconArr();
            closePopUp();
          });
    } else {
      submitIcon.setOnAction(
          event1 -> {
            try {
              locationDao.deleteLocation(icon.getLocation().getNodeID());
            } catch (SQLException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
            MapController.getController().populateFloorIconArr();
            closePopUp();
            // TODO: Make code go brrrrrrrrr and delete without needing to input nodeID
          });
    }
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
  public void equipmentForm(MouseEvent event) {
    title.setText("Equipment");
    content.getChildren().clear();
    formSetup(event);
    buttonBox.getChildren().addAll(returnButton, addButton, modifyButton, removeButton);
    addButton.setOnAction(
        event1 -> {
          equipmentAdditionForm(event);
        });
    modifyButton.setOnAction(
        event1 -> {
          equipmentModifyForm(event);
        });
    removeButton.setOnAction(
        event1 -> {
          equipmentRemoveForm(event);
        });
    stage.setTitle("Equipment");
    if (!stage.isShowing()) {
      showPopUp();
    }
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

  @FXML
  public void equipmentModifyForm(MouseEvent event) {}

  @FXML
  public void equipmentRemoveForm(MouseEvent event) {}

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
