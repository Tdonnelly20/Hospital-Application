package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.ServiceRequests.ServiceRequest;
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

  public Location() {
    nodeID = null;
    xCoord = 0;
    yCoord = 0;
    floor = null;
    building = null;
    nodeType = null;
    longName = null;
    shortName = null;
  }

  public Location(double xCoord, double yCoord, String floor) {
    nodeID = null;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    building = null;
    nodeType = null;
    longName = null;
    shortName = null;
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
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public Location(String nodeID) {
    this.nodeID = nodeID;
  }

  public String getNodeID() {
    return nodeID;
  }
}
