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
    return SingletonHelper.manager;
  }

  /** Sets up floor elements */
  public void setUpFloors() {
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
    tempIcon.setVisible(true);
    tempIcon.setTranslateX(xPos);
    tempIcon.setTranslateX(yPos);
    if (!MapController.getController().getMapPane().getChildren().contains(tempIcon)) {
      // System.out.println("X:" + xPos + " Y:" + yPos);
      tempIcon.setFitWidth(30);
      tempIcon.setFitHeight(30);
      MapController.getController().getMapPane().getChildren().add(tempIcon);
    }
  }
}
