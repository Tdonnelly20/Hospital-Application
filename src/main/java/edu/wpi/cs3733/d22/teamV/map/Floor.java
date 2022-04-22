package edu.wpi.cs3733.d22.teamV.map;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
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

  public Floor(String floorName, Image map) {
    this.floorName = floorName;
    this.map = map;
    this.dirtyEquipmentCount = 0;
    this.activeRequestCount = 0;
  }

  public void addIcon(Icon icon) {
    if (icon.iconType.equals(Icon.IconType.Location)) {
      locationIcons.add((LocationIcon) icon);
    } else {
      equipmentIcons.add((EquipmentIcon) icon);
    }
  }

  public ArrayList<Icon> getIconList() {
    ArrayList<Icon> iconList = new ArrayList<>();
    iconList.addAll(equipmentIcons);
    iconList.addAll(locationIcons);
    return iconList;
  }

  public void removeIcon(Icon icon) {
    if (icon.iconType.equals(Icon.IconType.Location)) {
      locationIcons.remove((LocationIcon) icon);
      RequestSystem.getSystem().deleteLocation(icon.getLocation().getNodeID());
    } else {
      equipmentIcons.remove((EquipmentIcon) icon);
      RequestSystem.getSystem().removeEquipment((EquipmentIcon) icon);
    }
  }

  public String getFloorName() {
    return floorName;
  }

  public boolean containsIcon(Location location, Icon.IconType type) {
    if (type.equals(Icon.IconType.Location)) {
      for (Icon icon : locationIcons) {
        if (icon.getLocation().equals(location)) {
          return true;
        }
      }
    } else {
      for (Icon icon : equipmentIcons) {
        if (icon.getLocation().equals(location)) {
          return true;
        }
      }
    }
    return false;
  }

  public Icon getIcon(Location location) {
    for (Icon icon : getIconList()) {
      if (icon.getLocation().equals(location)) {
        return icon;
      }
    }
    return null;
  }

  public Icon getIcon(Location location, Icon.IconType type) {
    if (type.equals(Icon.IconType.Location)) {
      for (Icon icon : locationIcons) {
        if (icon.getLocation().equals(location)) {
          return icon;
        }
      }
    } else {
      for (Icon icon : equipmentIcons) {
        if (icon.getLocation().equals(location)) {
          return icon;
        }
      }
    }
    return null;
  }

  public boolean hasEquipmentIconAt(double x, double y) {
    for (EquipmentIcon icon : equipmentIcons) {
      if ((icon.xCoord == x) && (icon.yCoord == y)) {
        return true;
      }
    }
    return false;
  }

  public void addToEquipmentIcon(double x, double y, Equipment equipment) {
    for (EquipmentIcon icon : equipmentIcons) {
      if ((icon.xCoord == x) && (icon.yCoord == y)) {
        icon.addToEquipmentList(equipment);
        icon.setImage();
        break;
      }
    }
  }

  public void addRequest(ServiceRequest request) {
    for (LocationIcon icon : locationIcons) {
      if (icon.getLocation().equals(request.getLocation())) {
        icon.getRequestsArr().add(request);
        RequestSystem.Dao t = RequestSystem.Dao.Equipment;
        switch (request.getType()) {
          case "Equipment":
            t = RequestSystem.Dao.Equipment;
          case "InternalPatientTransportation":
            t = RequestSystem.Dao.InternalPatientTransportation;
          case "LabRequest":
            t = RequestSystem.Dao.LabRequest;
          case "LaundryRequest":
            t = RequestSystem.Dao.LaundryRequest;
          case "MealRequest":
            t = RequestSystem.Dao.MealRequest;
          case "MedicineDelivery":
            t = RequestSystem.Dao.MedicineDelivery;
          case "ReligiousRequest":
            t = RequestSystem.Dao.ReligiousRequest;
          case "RobotRequest":
            t = RequestSystem.Dao.RobotRequest;
          case "SanitationRequest":
            t = RequestSystem.Dao.SanitationRequest;
        }
        RequestSystem.getSystem().addServiceRequest(request, t);
      }
    }
  }
}
