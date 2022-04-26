package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.map.LocationIcon;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
  protected final int ID = 0; // location class that stores info about each location
  private String nodeID;
  private double xCoord;
  private double yCoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;
  private ArrayList<ServiceRequest> requests = new ArrayList<ServiceRequest>();
  private LocationIcon icon;

  public Location() {
    nodeID = null;
    xCoord = 0;
    yCoord = 0;
    floor = null;
    building = null;
    nodeType = "";
    longName = null;
    shortName = null;
    icon = new LocationIcon(this);
    // icon.setFloor(MapManager.getManager().getFloor("1"));
  }

  public Location(double xCoord, double yCoord, String floor) {
    nodeID = null;
    this.xCoord = Math.round(xCoord * 100.0) / 100.0;
    this.yCoord = Math.round(yCoord * 100.0) / 100.0;
    this.floor = floor;
    building = null;
    nodeType = "";
    longName = null;
    shortName = null;
    icon = new LocationIcon(this);
    // icon.setFloor(MapManager.getManager().getFloor(floor));
  }

  public Location(
      String nodeID,
      double xCoord,
      double yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xCoord = Math.round(xCoord * 100.0) / 100.0;
    this.yCoord = Math.round(yCoord * 100.0) / 100.0;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
    icon = new LocationIcon(this);
    // icon.setFloor(MapManager.getManager().getFloor(floor));
  }

  public Location(String nodeID) {
    this.nodeID = nodeID;
    xCoord = 0;
    yCoord = 0;
    floor = null;
    building = null;
    nodeType = "";
    longName = null;
    shortName = null;
    icon = new LocationIcon(this);
    // icon.setFloor(MapManager.getManager().getFloor("1"));
  }

  public String getNodeID() {
    return nodeID;
  }

  public String toString() {
    return "Node ID: "
        + nodeID
        + " X: "
        + xCoord
        + " Y: "
        + yCoord
        + " Floor: "
        + floor
        + " Building: "
        + building
        + " Node Type: "
        + nodeType
        + " Long Name: "
        + longName
        + " Short Name: "
        + shortName;
  }
}
