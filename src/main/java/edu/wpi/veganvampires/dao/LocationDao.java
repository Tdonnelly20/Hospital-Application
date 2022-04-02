package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.LocationImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Location;
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

  public Location getLocation(String nodeID) {
    for (Location l : locationList) {
      if (l.getNodeID().equals(nodeID)) {
        return l;
      }
    }
    return null;
  }

  @Override
  public void addLocation(Location location) {
    locationList.add(location);
    try {
      updateLocationDB(location);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteLocation(String nodeID) {
    try {
      updateLocationDB(nodeID);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateLocationDB(String nodeID) throws SQLException {
    try {
      System.out.println("Sending to database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (Location l : locationList) {
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE nodeID.equals(l.getNodeID())");
      }
      locationList.remove(getLocation(nodeID));

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
}
