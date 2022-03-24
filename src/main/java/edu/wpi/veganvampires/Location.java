package edu.wpi.veganvampires;

public class Location {
  protected final int ID = 0; // location class that stores info about each location
  int nodeID;
  int xCoord;
  int yCoord;
  String floor;
  String building;
  String nodeType;
  String longName;
  String shortName;

  public Location() {
    nodeID = 0;
    xCoord = 0;
    yCoord = 0;
    floor = null;
    building = null;
    nodeType = null;
    longName = null;
    shortName = null;
  }

  public Location(
      int nodeID,
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

  public Location(int nodeID) {
    this.nodeID = nodeID;
  }
}
