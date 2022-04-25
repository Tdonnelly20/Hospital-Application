package edu.wpi.cs3733.d22.teamV.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.MapDashboardController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import java.sql.Timestamp;
import java.time.Instant;
import edu.wpi.cs3733.d22.teamV.servicerequests.EquipmentDelivery;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentIcon extends Icon {

  ArrayList<Equipment> equipmentList; // All the equipment at the xy coordinates
  private double xCoord;
  private double yCoord;
  private int cleanPumps = 0;
  private int dirtyPumps = 0;
  private int cleanBeds = 0;
  private int dirtyBeds = 0;

  /** Icon for equipment with the same x and y coordinates */
  public EquipmentIcon(Location location) {
    super();
    xCoord = location.getXCoord();
    yCoord = location.getYCoord();
    floor = MapManager.getManager().getFloor(location.getFloor());
    this.iconType = IconType.Equipment;
    equipmentList = new ArrayList<>();
    image.setFitWidth(20);
    image.setFitHeight(20);
    image.setTranslateX(xCoord - image.getFitWidth() / 2);
    image.setTranslateY((yCoord) - image.getFitHeight() / 2);
    image.setOnMouseClicked(
        event -> {
          // Opens Popup when clicked twice
          if (event.getClickCount() == 2) {
            PopupController.getController().equipmentForm(event, this);
          }
        });
    image.setOnMouseReleased(
        event -> {
          // Updates xy and checks if it is touching another icon when it is released from drag
          if (isDrag) {
            isDrag = false;
            Point2D offset = (Point2D) image.getUserData();
            xCoord += event.getX() - offset.getX() - 15;
            yCoord += event.getY() - offset.getY() - 20;
            RequestSystem.getSystem().updateLocations(this);
            checkBounds();
            MapManager.getManager().setUpFloors();
          }
        });
  }

  /** Returns a VBox which displays information about each piece of equipment */
  @Override
  public VBox compileList() {
    if (equipmentList.size() > 0) {
      ObservableList<String> statusStrings = FXCollections.observableArrayList("Clean", "Dirty");
      VBox vBox = new VBox();
      for (Equipment equipment : equipmentList) {
        Label idLabel = new Label("ID: " + equipment.getID());
        Button deleteEquipment = new Button("Delete");
        deleteEquipment.setStyle("-fx-background-color: #5C7B9F; -fx-text-fill: white;");
        Button modifyEquipment = new Button("Modify");
        modifyEquipment.setStyle("-fx-background-color: #5C7B9F; -fx-text-fill: white;");
        modifyEquipment.setOnAction(
            event -> PopupController.getController().equipmentModifyForm(equipment));
        deleteEquipment.setOnAction(
            event -> {
              removeEquipment(equipment);
              if (getEquipmentList().size() == 0) {
                RequestSystem.getSystem().removeEquipment(equipment);
                MapManager.getManager().setUpFloors();
                MapController.getController()
                    .setFloor(MapController.getController().getFloorName());
              }
            });
        Label locationLabel = new Label("X: " + xCoord + " Y: " + yCoord);

        JFXComboBox<String> updateStatus = new JFXComboBox<>(statusStrings);
        updateStatus.setPromptText(equipment.getIsDirtyString());
        updateStatus.setValue(equipment.getIsDirtyString());
        updateStatus.setOnAction(
            event1 -> {
              System.out.println("'" + updateStatus.getValue() + "'");
              equipment.setIsDirty(updateStatus.getValue().equals("Dirty"));
              RequestSystem.getSystem()
                  .getEquipmentDao()
                  .updateEquipment(equipment, equipment.getID());
              MapManager.getManager().setUpFloors();
              MapController.getController().setFloor(MapController.getController().getFloorName());
            });
        HBox hbox = new HBox(15, updateStatus, modifyEquipment, deleteEquipment);
        Label description = new Label("Description: " + equipment.getDescription());
        Accordion accordion =
            new Accordion(
                new TitledPane(
                    equipment.getName()
                        + " ("
                        + equipment.getIsDirtyString()
                        + "): "
                        + equipment.getID(),
                    new VBox(15, idLabel, description, hbox)));

        // accordion.width
        vBox.getChildren().add(accordion);
      }
      return vBox;
    }
    return null;
  }

  /** Adds equipment to the list and updates icon image */
  public void addToEquipmentList(Equipment equipment) {
    if (equipment.getIsDirty()) {
      if (equipment.getName().equals("Infusion Pump")) dirtyPumps++;
      else if (equipment.getName().equals("Bed")) dirtyBeds++;
      equipmentList.add(equipment);
    } else {
      equipmentList.add(0, equipment);
      if (equipment.getName().equals("Infusion Pump")) cleanPumps++;
    }
    setImage();
    alertSixBeds(equipment, true);
    MapDashboardController.getController().updateCounts();
    pumpAlert();
  }

  /** Removes equipment and calls alerts */
  public void removeEquipment(Equipment equipment) {
    equipmentList.remove(equipment);
    RequestSystem.getSystem().removeEquipment(equipment);
    pumpAlert();
    if (equipment.getName().equals("Infusion Pump")) {
      if (equipment.getIsDirty()) dirtyPumps--;
      else cleanPumps--;
    } else if (equipment.getName().equals("Bed")) {
      dirtyBeds--;
      if (equipment.getName().equals("Infusion Pump")) {
        if (equipment.getIsDirty()) dirtyPumps--;
        else cleanPumps--;
      }
      alertSixBeds(equipment, false);
      PopupController.getController().closePopUp();
    }
  }

  /** Sets the icon image depending on if it has clean equipment */
  public void setImage() {
    if (hasCleanEquipment()) {
      image.setImage(MapManager.getManager().cleanEquipment);
    } else {
      image.setImage(MapManager.getManager().dirtyEquipment);
    }
  }

  /** Determines if the icon has clean equipment */
  public boolean hasCleanEquipment() {
    for (Equipment equipment : equipmentList) {
      if (!equipment.getIsDirty()) {
        return true;
      }
    }
    return false;
  }

  /**
   * If this icon touches another icon then 1. The other icon's equipment is transferred to the
   * first icon 2. The x and y coordinates are changed to the second icon and the equipment
   * locations are updated 3. The map is refreshed
   */
  public void checkBounds() {
    ArrayList<EquipmentIcon> floorEquipmentIcons =
        MapManager.getManager().getFloor(floor.getFloorName()).getEquipmentIcons();
    if (floorEquipmentIcons.size() > 1) {
      for (EquipmentIcon icon : floorEquipmentIcons) {
        if (icon != this && iconType.equals(IconType.Equipment)) {
          // If icon touches another icon
          if (icon.getImage().getBoundsInParent().intersects(this.image.getBoundsInParent())) {
            // Transferring equipment to this icon
            ArrayList<Equipment> tempEquipmentList = new ArrayList<>(icon.getEquipmentList());
            tempEquipmentList.addAll(equipmentList);
            equipmentList.clear();
            equipmentList.addAll(tempEquipmentList);
            // Updates the xy coordinates for equipment
            this.xCoord = icon.xCoord;
            this.yCoord = icon.yCoord;
            RequestSystem.getSystem().updateLocations(this);
            // Updating Map
            MapManager.getManager().setUpFloors();
            MapController.getController().setFloor(floor.getFloorName());
            break;
          }
        }
      }
    }
  }

  public void alertSixBeds() {

    int alertCounter = 0;
    boolean alert = false;

    ArrayList<Equipment> equip = new ArrayList<Equipment>();
    equip = this.getEquipmentList();
    ArrayList<String> dirtyBedsFloor = new ArrayList<String>();

    for (int i = 0; equip.size() > i; i++) {
      if (equip.get(i).getName().equals("Bed") && equip.get(i).getIsDirty()) {
        dirtyBedsFloor.add(String.valueOf(equip.get(i).getFloor()));
        alertCounter = +1;
      }
    }
    if (alertCounter > 5) {
      alert = true;
    }

    MapDashboardController.getController().addBedAlertToArray(alert, dirtyBedsFloor);
  }

  // checks if isAdding is true, if so finds beds that are dirty in the same place.
  // when counter > 5, dirtyBeds increases by 1 and RequestSystem is called (EquipmentDelivery).
  // else, dirtyBeds decreases by 1.
  //      int employeeID,
  //      int patientID,
  //      String patientFirstName,
  //      String patientLastName,
  //      String nodeID,
  //      String equipment,
  //      String notes,
  //      int quantity,
  //      String status,
  //      int serviceID,      String date) {
  public void alertSixBeds(Equipment e, boolean isAdding) {
    if (isAdding) {
      if (e.getIsDirty() && e.getName() == "Bed") {
        dirtyBeds += 1;
      }
      if (dirtyBeds > 5) {
        EquipmentDelivery request =
            new EquipmentDelivery(
                -1,
                -1,
                "OR",
                "LN",
                e.getID(),
                e.getID().toString(),
                "Notes",
                1,
                "Not Started",
                RequestSystem.getServiceID(),
                Timestamp.from(Instant.now()).toString());

        RequestSystem.getSystem().addServiceRequest(request);
      }
    } else {
      dirtyBeds--;
    }
  }

  public int[] pumpAlert() {
    int clean = 0;
    int dirty = 0;
    for (Equipment equipment : equipmentList) {
      // System.out.println(equipment.getName());
      if (equipment.getName().equals("Infusion Pump")) {
        if (equipment.getIsDirty()) dirty++;
        else clean++;
      }
    }
    return new int[] {clean, dirty};
  }
}
