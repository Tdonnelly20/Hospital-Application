package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import java.util.ArrayList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Floor {
  private String floorName;
  private Image map;
  private ArrayList<Icon> iconList = new ArrayList<Icon>();

  public Floor(String floorName, Image map) {
    this.floorName = floorName;
    this.map = map;
  }

  public void addIcon(Icon icon) {
    iconList.add(icon);
  }

  public void removeIcon(Icon icon) {
    if (iconList.contains(icon)) {
      iconList.remove(icon);
    }
  }

  public String getFloorName() {
    return floorName;
  }

  public boolean containsIcon(Location location) {
    for (Icon icon : iconList) {
      if (icon.getLocation().equals(location)) {
        return true;
      }
    }
    return false;
  }

  public Icon getIcon(Location location) {
    for (Icon icon : iconList) {
      if (icon.getLocation().equals(location)) {
        return icon;
      }
    }
    return null;
  }

  public ArrayList<EquipmentIcon> getEquipmentIcons() {
    ArrayList<EquipmentIcon> equipmentIcons = new ArrayList<>();
    for (Icon icon : iconList) {
      if (icon.iconType.equals("Equipment")) {
        equipmentIcons.add((EquipmentIcon) icon);
      }
    }
    return equipmentIcons;
  }

  public boolean hasEquipmentIconAt(double x, double y) {
    for (EquipmentIcon icon : getEquipmentIcons()) {
      if ((icon.xCoord == x) && (icon.yCoord == y)) {
        return true;
      }
    }
    return false;
  }

  public void addToEquipmentIcon(double x, double y, Equipment equipment) {
    for (EquipmentIcon icon : getEquipmentIcons()) {
      if ((icon.xCoord == x) && (icon.yCoord == y)) {
        icon.addToEquipmentList(equipment);
        break;
      }
    }
  }
}
