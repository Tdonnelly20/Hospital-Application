package edu.wpi.veganvampires.Features;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
  protected final int ID = 0; // location class that stores info about each location
  public String nodeID;
  public int xCoord;
  public int yCoord;
  public String floor;
  public String building;
  public String nodeType;
  public String longName;
  public String shortName;

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

  public Location(
      String nodeID,
      int xCoord,
      int yCoord,
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
}
