package edu.wpi.veganvampires.Dao;

import edu.wpi.veganvampires.Interfaces.LocationImpl;
import edu.wpi.veganvampires.Location;
import edu.wpi.veganvampires.Vdb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDao implements LocationImpl {
  private static ArrayList<Location> locationList;

  public LocationDao() {
    locationList = new ArrayList<Location>();
    try {
      Vdb.CreateDB();
      locationList = Vdb.locations;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public LocationDao(ArrayList<Location> locations) {
    locationList = locations;
  }

  @Override
  public List<Location> getAllLocations() {
    return locationList;
  }

  @Override
  public void addLocation(Location location) throws SQLException {
    locationList.add(location);
  }

  @Override
  public void deleteLocation(Location location) throws SQLException {
    locationList.remove(location);
    updateLocationDB(location.getNodeID());
  }

  private void updateLocationDB(String location) throws SQLException {
    try {
      System.out.println("Sending to database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (Location l : locationList)
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE location.equals(l.getNodeID())");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateLocationDB(Location newlocation) throws SQLException {
    try {
      System.out.println("Sending to database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (Location l : locationList)
        exampleStatement.execute(
            "INSERT INTO LOCATIONS VALUES (newlocation.getNodeID, newlocation.getXCoord(), newlocation.getYCoord(), newlocation.getFloor(), newlocation.getBuilding(), newlocation.getNodeType(), newlocation.getLongName(), newlocation.getShortName())");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  /*
  List<Location> locationList;
  Connection connection = Vdb.Connect();

  public LocationDao() {
    locationList = Vdb.locations;
  }

  public LocationDao(List<Location> list) {
    locationList = list;
  }

  @Override
  public List<Location> getAllLocations() {
    return locationList;
  }

  @Override
  public Location getLocation(Location location) {
    for (int i = 0; i < locationList.size(); i++) {
      if (locationList.get(i).getNodeID().equals(location.getNodeID())) {
        return locationList.get(i);
      }
    }
    System.out.println("Location not found");
    return null;
  }

  @Override
  public void updateLocation(Location location) {

    for (int i = 0; i < locationList.size(); i++) {
      if (locationList.get(i).getNodeID().equals(location.getNodeID())) {
        locationList.get(i).setXCoord(location.getXCoord());
        locationList.get(i).setYCoord(location.getYCoord());
        locationList.get(i).setNodeID(location.getNodeID());
        locationList.get(i).setNodeType(location.getNodeType());
        locationList.get(i).setFloor(location.getFloor());
        locationList.get(i).setBuilding(location.getBuilding());
        locationList.get(i).setShortName(location.getShortName());
        locationList.get(i).setLongName(location.getLongName());
        try {
          Statement st = connection.createStatement();
          st.execute(
              "UPDATE LOCATIONS SET xCoord = location.getXCoord(),"
                  + "yCoord = location.getYCoord(),"
                  + "nodeType = location.getNodeType(),"
                  + "floor =location.getFloor(),"
                  + "building = location.getBuilding(),"
                  + "shortName = location.getShortName(),"
                  + "longName = location.getLongName()"
                  + "WHERE nodeID = location.getNodeID()");
        } catch (SQLException e) {
          System.out.println("Connection failed. Check output console.");
          e.printStackTrace();
          return;
        }
      }
    }
  }

  @Override
  public void deleteLocation(Location location) {
    for (int i = 0; i < locationList.size(); i++) {
      if (locationList.get(i).getNodeID().equals(location.getNodeID())) {
        locationList.remove(i);
        try {
          Statement st = connection.createStatement();
          st.execute("DELETE FROM Locations WHERE nodeID = " + location.getNodeID());
        } catch (SQLException e) {
          System.out.println("Connection failed. Check output console.");
          e.printStackTrace();
          return;
        }
      }
    }
  }*/
}
