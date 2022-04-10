package edu.wpi.cs3733.d22.teamV.manager;

import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.map.*;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.serviceRequest.ServiceRequest;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapManager {
  private ArrayList<Floor> floorList;
  @FXML private ImageView tempIcon = new ImageView("icon.png");
  List<? extends ServiceRequest> serviceRequests = new ArrayList<>();
  RequestSystem requestSystem = new RequestSystem();

  private MapManager() {
    serviceRequests = requestSystem.getEveryServiceRequest();
    PopupController.getController().setUpPopup();
    setUpFloors();
  }

  private static class SingletonHelper {
    private static final MapManager manager = new MapManager();
  }

  public static MapManager getManager() {
    return MapManager.SingletonHelper.manager;
  }

  /** Sets up floor elements */
  private void setUpFloors() {
    floorList = new ArrayList<>();

    Floor l1 = new Floor("L1");
    Floor l2 = new Floor("L2");
    Floor f1 = new Floor("1");
    Floor f2 = new Floor("2");
    Floor f3 = new Floor("3");
    Floor f4 = new Floor("4");
    Floor f5 = new Floor("5");

    floorList.add(l1);
    floorList.add(l2);
    floorList.add(f1);
    floorList.add(f2);
    floorList.add(f3);
    floorList.add(f4);
    floorList.add(f5);
    // System.out.println("SIZE: " + floorList.size());

    loadRequests();
    for (Location l : requestSystem.getLocations()) {
      switch (l.getFloor()) {
        case "L1":
          loadLocations(0, l);
          break;
        case "L2":
          loadLocations(1, l);
          break;
        case "1":
          loadLocations(2, l);
          break;
        case "2":
          loadLocations(3, l);
          break;
        case "3":
          loadLocations(4, l);
          break;
        case "4":
          loadLocations(5, l);
          break;
        case "5":
          loadLocations(6, l);
          break;
      }
    }

    for (Equipment e : requestSystem.getEquipment()) {
      Location l = new Location(e.getX(), e.getY(), e.getFloor());
      EquipmentIcon equipmentIcon = new EquipmentIcon(l);
      equipmentIcon.addToEquipmentList(e);
      equipmentIcon.setImage();
      switch (e.getFloor()) {
        case "L1":
          floorList.get(0).addIcon(equipmentIcon);
          break;
        case "L2":
          floorList.get(1).addIcon(equipmentIcon);
          break;
        case "1":
          floorList.get(2).addIcon(equipmentIcon);
          break;
        case "2":
          floorList.get(3).addIcon(equipmentIcon);
          break;
        case "3":
          floorList.get(4).addIcon(equipmentIcon);
          break;
        case "4":
          floorList.get(5).addIcon(equipmentIcon);
          break;
        case "5":
          floorList.get(6).addIcon(equipmentIcon);
          break;
      }
    }
  }

  public void loadLocations(int i, Location l) {
    if (floorList.size() > 0) {
      if (floorList.get(i).containsIcon(l)) {
        if (floorList.get(i).getIcon(l).iconType.equals("Location")) {
          if (l.getRequests().size() > 0) {
            floorList.get(i).getIconList().remove(floorList.get(i).getIcon(l));
            floorList.get(i).getIconList().add(new RequestIcon(l));
          }
        }
      } else {
        floorList.get(i).addIcon(new LocationIcon(l));
      }
    } else {
      floorList.get(i).addIcon(new LocationIcon(l));
    }
  }

  public void loadRequests() {
    if (serviceRequests.size() > 0) {
      for (ServiceRequest serviceRequest : serviceRequests) {
        for (Location location : requestSystem.getLocations()) {
          if (!location.getRequests().contains(serviceRequest)) {
            location.getRequests().add(serviceRequest);
          }
        }
      }
    }
  }

  /**
   * @param str
   * @return Floor that corresponds to str
   */
  public Floor getFloor(String str) {
    switch (str) {
      case "L1":
        return floorList.get(0);
      case "L2":
        return floorList.get(1);
      case "1":
        return floorList.get(2);
      case "2":
        return floorList.get(3);
      case "3":
        return floorList.get(4);
      case "4":
        return floorList.get(5);
      case "5":
        return floorList.get(6);
    }
    return null;
  }

  public void isTempIconVisible(boolean isVisible) {
    tempIcon.setVisible(isVisible);
  }

  public void placeTempIcon(double xPos, double yPos) {
    // Place Icon
    tempIcon.setX(xPos);
    tempIcon.setY(yPos);
    if (!MapController.getController().getMapPane().getChildren().contains(tempIcon)) {
      // System.out.println("X:" + xPos + " Y:" + yPos);
      tempIcon.setFitWidth(30);
      tempIcon.setFitHeight(30);
      MapController.getController().getMapPane().getChildren().add(tempIcon);
    }
  }
  /*


  @FXML
  private void setUpPopupButtons(MouseEvent event, double xPos, double yPos) {
    MapManager mapManager = MapManager.getManager();
    buttonBox.getChildren().addAll(locationButton, equipmentButton, closeButton);
    buttonBox.setAlignment(Pos.CENTER);
    locationButton.setOnAction(
        event1 -> {
          mapManager.getTempIcon().setImage(new Image("icon.png"));
          addLocationForm(event, xPos, yPos, false);
        });
    equipmentButton.setOnAction(
        event1 -> {
          mapManager.getTempIcon().setImage(new Image("Equipment.png"));
          addLocationForm(event, xPos, yPos, true);
        });
    closeButton.setOnAction(
        event1 -> {
          mapManager.closePopUp();
          mapManager.getTempIcon().setVisible(false);
          clearPopupForm();
        });
  }


  @FXML
  private void setUpPopup(MouseEvent event) {
    MapManager mapManager = MapManager.getManager();
    mapManager.getContent().getChildren().clear();
    // Setup
    double xPos = event.getX() - 15;
    double yPos = event.getY() - 25;
    setUpPopupButtons(event, xPos, yPos);
    setUpPopupText();
    mapManager.getContent().getChildren().addAll(titleBox, buttonBox);
    // Place Icon
    mapManager.getTempIcon().setVisible(true);
    mapManager.getTempIcon().setX(xPos);
    mapManager.getTempIcon().setY(yPos);
    if (!mapPane.getChildren().contains(tempIcon)) {
      System.out.println("X:" + xPos + " Y:" + yPos);
      mapManager.getTempIcon().setFitWidth(30);
      mapManager.getTempIcon().setFitHeight(30);
      mapPane.getChildren().add(tempIcon);
    }
  }*/
  /*
  @FXML
  public void openIconFormWindow(MouseEvent event) {
    MapManager mapManager = MapManager.getManager();
    System.out.printf(event.getTarget().getClass().getTypeName());
    if (!event.getTarget().getClass().getTypeName().equals("javafx.scene.image.ImageView")) {
      setUpPopup(event);
      // Scene and Stage
      mapManager.getStage().setTitle("Add New Location");
      mapManager.showPopUp();
    }
  }

  @FXML
  private void addLocationForm(MouseEvent event, double xPos, double yPos, boolean isEquipment) {
    MapManager mapManager = MapManager.getManager();
    clearPopupForm();
    // Form
    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    mapManager.getContent().getChildren().addAll(field1, field2, field3, field4);
    // Place Icon
    if (isEquipment) {
      mapManager.getStage().setTitle("Add Equipment");
    } else {
      mapManager.getStage().setTitle("Add New Location");
    }

    if (isEquipment) {
      submitButton = new Button("Continue");
    } else {
      submitButton = new Button("Add icon");
    }
    submitButton.setOnAction(
            event1 -> {
              if (!field1.getText().isEmpty()
                      && !field2.getText().isEmpty()
                      && !field3.getText().isEmpty()
                      && !field4.getText().isEmpty()) {
                Location location =
                        new Location(
                                field1.getText(),
                                xPos,
                                yPos,
                                getFloor(),
                                "Tower",
                                field2.getText(),
                                field4.getText(),
                                field3.getText());
                if (isEquipment) {
                  addEquipmentForm(location);
                } else {
                  addIcon(location, isEquipment);
                }
              } else {
                mapManager.getContent().getChildren().add(missingFields);
              }
            });

    title.setText("Please add a location");
    titleBox.getChildren().add(title);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(submitButton, clearForm, closeButton);
    mapManager.getContent().getChildren().addAll(titleBox, buttonBox);

    mapPane.getChildren().add(tempIcon);
    mapManager.showPopUp();
  }

  @FXML
  private void addEquipmentForm(Location location) {
    MapManager mapManager = MapManager.getManager();
    clearPopupForm();
    // Form
    field1.setPromptText("Node ID");
    field2.setPromptText("Node Type");
    field3.setPromptText("Short Name");
    field4.setPromptText("Long Name");

    mapManager.getContent().getChildren().addAll(field1, field2, field3, field4);
    // Place Icon
    mapManager.getStage().setTitle("Add Equipment");
    submitButton = new Button("Add icon");

    submitButton.setOnAction(
            event1 -> {
              if (!field1.getText().isEmpty()
                      && !field2.getText().isEmpty()
                      && !field3.getText().isEmpty()
                      && !field4.getText().isEmpty()) {
                addIcon(location, true);
              } else {
                mapManager.getContent().getChildren().add(missingFields);
              }
            });

    title.setText("Please add equipment");
    titleBox.getChildren().add(title);
    buttonBox.getChildren().clear();
    buttonBox.getChildren().addAll(submitButton, clearForm, closeButton);
    mapManager.getContent().getChildren().addAll(titleBox, buttonBox);

    mapPane.getChildren().add(tempIcon);
    mapManager.showPopUp();
  }

  // Adds icon to map
  private void addIcon(Location location, Boolean isEquipment) {
    MapManager.getManager().closePopUp();
    mapPane.getChildren().remove(tempIcon);
    MapManager.getManager().getFloor(getFloor()).addIcon(new Icon(location, isEquipment));
    tempIcon.setVisible(false);
    checkDropDown();
  }
   */
}
