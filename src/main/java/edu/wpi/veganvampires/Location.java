package edu.wpi.veganvampires;

public class Location {
    int nodeID;
    int xCoord;
    int yCoord;
    int floor;
public class Location {//location class that stores info about each location
    String nodeID;
    int xcoord;
    int ycoord;
    int floorNum;
    String building;
    String nodeType;
    String longName;
    String shortName;

    public Location()
    {
        nodeID = -1;
        xCoord = 0;
        yCoord = 0;
        floor = 0;
        building = null;
        nodeType = null;
        longName = null;
        shortName = null;
    }

    public Location(int nodeID, int xCoord, int yCoord, int floor, String building, String nodeType, String longName, String shortName)
    {
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
    }

    public Location(int nodeID)
    {
        this.nodeID = nodeID;
    }
}
