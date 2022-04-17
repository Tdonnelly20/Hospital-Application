package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.controllers.MapController;
import edu.wpi.cs3733.d22.teamV.controllers.PopupController;
import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.manager.MapManager;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentIcon extends Icon {

  ArrayList<Equipment> equipmentList;

  public EquipmentIcon(Location location) {
    super(location);
    setImage();
    equipmentList = new ArrayList<>();
    this.iconType = "Equipment";
    image.setFitWidth(30);
    image.setFitHeight(30);
    image.setTranslateX((xCoord) - 25);
    image.setTranslateY((yCoord) - 15);
    image.setOnMouseClicked(
        event -> {
          if (event.getClickCount() == 2) {
            System.out.println(equipmentList.size());
            PopupController.getController().equipmentForm(event, this);
          }
        });
    image.setOnMouseReleased(
        event -> {
          location.setXCoord(location.getXCoord() + event.getX());
          location.setYCoord(location.getYCoord() + event.getY());
          checkBounds();
        });
  }

  public void addToEquipmentList(Equipment equipment) {
    if (equipment.getIsDirty()) {
      if (equipmentList.size() == 0) {
        image.setImage(MapManager.getManager().dirtyEquipment);
      }
      equipmentList.add(equipment);
    } else {
      image.setImage(MapManager.getManager().cleanEquipment);
      equipmentList.add(0, equipment);
    }
    setImage();
  }

  public void removeEquipment(Equipment equipment) {
    RequestSystem.getSystem().deleteEquipment(equipment);
    equipmentList.remove(equipment);
    if (equipmentList.size() == 0) {
      setImage();
      MapManager.getManager().getFloor(equipment.getFloor()).removeIcon(this);
    }
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
      if (icon != this) {
        if (icon.getImage().getBoundsInParent().intersects(this.image.getBoundsInParent())) {
          System.out.println("Intersection");
          for (Equipment equipment : icon.getEquipmentList()) {
            equipment.setX(getXCoord());
            equipment.setY(getYCoord());
            equipment.setIcon(this);
            addToEquipmentList(equipment);
          }
          MapManager.getManager().getFloor(getLocation().getFloor()).removeIcon(icon);
          MapController.getController().setFloor(icon.location.getFloor());
        }
      }
    }
  }
}
