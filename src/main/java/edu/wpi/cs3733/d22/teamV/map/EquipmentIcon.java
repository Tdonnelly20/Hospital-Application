package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.MapDashboardController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentIcon extends Icon {

  ArrayList<Equipment> equipmentList;

  public EquipmentIcon(Location location) {
    super(location);
    this.iconType = IconType.Equipment;
    equipmentList = new ArrayList<>();
    image.setFitWidth(20);
    image.setFitHeight(20);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            PopupController.getController().equipmentForm(event, this);
          }
        });
    image.setOnMouseReleased(
        event -> {
          location.setXCoord(location.getXCoord() + event.getX());
          location.setYCoord(location.getYCoord() + event.getY());
          checkBounds();
        });
    image.setOnMouseExited(
        event -> {
          image.setFitWidth(15);
          image.setFitHeight(15);
        });
  }

  public void addToEquipmentList(Equipment equipment) {
    if (equipment.getIsDirty()) {
      equipmentList.add(equipment);
      if (equipmentList.size() == 1) {
        image.setImage(MapManager.getManager().dirtyEquipment);
      }
    } else {
      image.setImage(MapManager.getManager().cleanEquipment);
      equipmentList.add(0, equipment);
    }
    setImage();
    alertSixBeds();
  }

  public void removeEquipment(Equipment equipment) {
    RequestSystem.getSystem().deleteEquipment(equipment);
    equipmentList.remove(equipment);
    if (equipmentList.size() == 0) {
      setImage();
    }
    alertSixBeds();
  }

  public void setImage() {
    if (hasCleanEquipment()) {
      image.setImage(MapManager.getManager().cleanEquipment);
    } else {
      image.setImage(MapManager.getManager().dirtyEquipment);
    }
  }

  public boolean hasCleanEquipment() {
    for (Equipment equipment : equipmentList) {
      if (!equipment.getIsDirty()) {
        return true;
      }
    }
    return false;
  }

  public void checkBounds() {
    for (EquipmentIcon icon :
        MapManager.getManager().getFloor(location.getFloor()).getEquipmentIcons()) {
      if (icon != this && iconType.equals(IconType.Equipment)) {
        if (icon.getImage().getBoundsInParent().intersects(this.image.getBoundsInParent())) {
          System.out.println("Intersection");
          equipmentList.addAll(icon.getEquipmentList());
          updateLocation();
          icon.getEquipmentList().clear();
          MapController.getController().deleteIcon(icon);
          setImage();
        }
      }
    }
  }

  public void updateLocation() {
    for (Equipment equipment : equipmentList) {
      equipment.setX(location.getXCoord());
      equipment.setY(location.getYCoord());
    }
  }

  // it's called everytime there's an update.
  // it checks equipment list, sends info to checkAlertSixBeds in MapDashboardController
  // if the equipment is a bed and is dirty
  public void alertSixBeds() {
    int alertCounter = 0;
    boolean alert = false;

    for (Equipment equipment : equipmentList) {
      for (Equipment equipmentTwo : equipmentList) {
        if (equipment.getFloor() == equipmentTwo.getFloor())
          if (equipment.getX() == equipment.getX()) {
            boolean d = equipment.getIsDirty();
            int i =
                MapDashboardController.getController().checkAlertSixBeds(equipment.getName(), d);
            alertCounter += i;
          }
      }
    }
    if (alertCounter > 5) {
      alert = true;
    }
    MapDashboardController.getController().addBedAlertToArray(alert);
  }
}
