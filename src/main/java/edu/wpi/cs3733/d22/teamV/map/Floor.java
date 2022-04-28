package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import java.util.ArrayList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Floor {
  private String floorName;
  private Image map;
  private int dirtyEquipmentCount;
  private int activeRequestCount;
  private ArrayList<EquipmentIcon> equipmentIcons = new ArrayList<>();
  private ArrayList<LocationIcon> locationIcons = new ArrayList<>();
  private ArrayList<EquipmentIcon> pumpAlertIcons = new ArrayList<>();
  private ArrayList<EquipmentIcon> bedAlertIcons = new ArrayList<>();
  private ArrayList<EquipmentIcon> dirtyLocations = new ArrayList<>();
  private ArrayList<EquipmentIcon> cleanLocations = new ArrayList<>();
  private int numIcons;

  public Floor(String floorName, Image map) {
    this.floorName = floorName;
    this.map = map;
    this.dirtyEquipmentCount = 0;
    this.activeRequestCount = 0;
  }

  public void addIcon(Icon icon) {
    if (icon.iconType.equals(Icon.IconType.Location)) {
      locationIcons.add((LocationIcon) icon);
    }
    else {
      equipmentIcons.add((EquipmentIcon) icon);
      if ((((EquipmentIcon) icon).getCleanPumps() < 5)
          && !pumpAlertIcons.contains((EquipmentIcon) icon)) {
        pumpAlertIcons.add((EquipmentIcon) icon);
      }
      else if ((((EquipmentIcon) icon).getDirtyPumps() > 9)
          && !pumpAlertIcons.contains((EquipmentIcon) icon)) {
        pumpAlertIcons.add((EquipmentIcon) icon);
      }
      else if ((((EquipmentIcon) icon).getDirtyBeds() > 5)
          && !bedAlertIcons.contains((EquipmentIcon) icon)) {
        bedAlertIcons.add((EquipmentIcon) icon);
      }
      else System.out.println("uh oh");
    }
  }

  public ArrayList<Icon> getIconList() {
    ArrayList<Icon> iconList = new ArrayList<>();
    iconList.addAll(equipmentIcons);
    iconList.addAll(locationIcons);
    return iconList;
  }

  public String getFloorName() {
    return floorName;
  }

  /** Returns true if there's a EquipmentIcon at the given xy coords */
  public boolean containsIcon(double x, double y) {
    for (EquipmentIcon icon : equipmentIcons) {
      if ((icon.getXCoord() == x) && (icon.getYCoord() == y)) {
        return true;
      }
    }
    return false;
  }

  /** Adds equipment to the icon at the given xy coords */
  public void addToEquipmentIcon(double x, double y, Equipment equipment) {
    for (EquipmentIcon icon : equipmentIcons) {
      if ((icon.getXCoord() == x) && (icon.getYCoord() == y)) {
        icon.addToEquipmentList(equipment);
        icon.setImage();
        break;
      }
    }
  }
}
