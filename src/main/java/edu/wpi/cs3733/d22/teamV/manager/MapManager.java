package edu.wpi.cs3733.d22.teamV.manager;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.map.*;
import edu.wpi.cs3733.d22.teamV.objects.Equipment;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapManager {
  private ArrayList<Floor> floorList;
  List<? extends ServiceRequest> serviceRequests = new ArrayList<>();
  RequestSystem requestSystem = Vdb.requestSystem;
  public Image cleanEquipment = new Image("cleanEquipment.png");
  public Image dirtyEquipment = new Image("dirtyEquipment.png");
  public Image locationMarker = new Image("locationMarker.png");
  public Image requestMarker = new Image("requestMarker.png");

  /** Gets every service request and sets up the floors */
  public void init() {
    serviceRequests = requestSystem.getEveryServiceRequest();
    setUpFloors();
  }

  private static class SingletonHelper {
    private static final MapManager manager = new MapManager();
  }

  public static MapManager getManager() {
    return SingletonHelper.manager;
  }

  /** Sets up floor elements */
  public void setUpFloors() {
    floorList = new ArrayList<>();

    Floor l1 = new Floor("L1", new Image("Lower Level 1.png"));
    Floor l2 = new Floor("L2", new Image("Lower Level 2.png"));
    Floor f1 = new Floor("1", new Image("1st Floor.png"));
    Floor f2 = new Floor("2", new Image("2nd Floor.png"));
    Floor f3 = new Floor("3", new Image("3rd Floor.png"));
    Floor f4 = new Floor("4", new Image("4th Floor.png"));
    Floor f5 = new Floor("5", new Image("5th Floor.png"));

    floorList.add(l1);
    floorList.add(l2);
    floorList.add(f1);
    floorList.add(f2);
    floorList.add(f3);
    floorList.add(f4);
    floorList.add(f5);

    loadEquipment();
    setUpLocations();
    loadRequests();
  }

  private void setUpLocations() {
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
  }

  public void loadEquipment() {
    for (Equipment e : requestSystem.getEquipment()) {
      if (getFloor(e.getFloor()).hasEquipmentIconAt(e.getX(), e.getY())) {
        getFloor(e.getFloor()).addToEquipmentIcon(e.getX(), e.getY(), e);
      } else {
        e.setIcon(new EquipmentIcon(new Location(e.getX(), e.getY(), e.getFloor())));
        getFloor(e.getFloor()).addIcon(e.getIcon());
      }
    }
  }

  /** Loads every location and gives each the correct icon image */
  public void loadLocations(int i, Location l) {
    if (floorList.size() > 0) {
      if (floorList.get(i).containsIcon(l, Icon.IconType.Location)) {
        if (floorList.get(i).getIcon(l).iconType.equals(Icon.IconType.Location)) {
          if (l.getIcon().hasActiveRequests()) {
            l.getIcon().changeImages();
          }
        }
      } else {
        LocationIcon locationIcon = new LocationIcon(l);
        l.setIcon(locationIcon);
        floorList.get(i).addIcon(locationIcon);
      }
    } else {
      LocationIcon locationIcon = new LocationIcon(l);
      l.setIcon(locationIcon);
      floorList.get(i).addIcon(locationIcon);
    }
  }

  /** Loads each request into it's corresponding location's list */
  public void loadRequests() {
    if (serviceRequests.size() > 0) {
      for (ServiceRequest serviceRequest : requestSystem.getEveryServiceRequest()) {
        for (Floor floor : floorList) {
          for (Icon icon : floor.getIconList()) {
            if (serviceRequest.getLocation() != null) {
              if (serviceRequest.getLocation().getNodeID() != null) {
                if (icon.iconType.equals(Icon.IconType.Location)
                    && icon.getLocation()
                        .getNodeID()
                        .equals(serviceRequest.getLocation().getNodeID())) {
                  icon.getLocation().getRequests().add(serviceRequest);
                  ((LocationIcon) icon).addToRequests(serviceRequest);
                }
              }
            }
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
}
