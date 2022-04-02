package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.LocationImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDao implements LocationImpl {
  private static ArrayList<Location> allLocations;

  public LocationDao() {
    allLocations = new ArrayList<Location>();
  }

  public LocationDao(ArrayList<Location> locations) {
    allLocations = locations;
  }

  @Override
  public List<Location> getAllLocations() {
    return allLocations;
  }

  public Location getLocation(String nodeID) {
    for (Location l : allLocations) {
      if (l.getNodeID().equals(nodeID)) {
        return l;
      }
    }
    return null;
  }

  @Override
  public void addLocation(Location location) {
    allLocations.add(location);
    try {
      System.out.println("Sending to database...");
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();
      for (Location l : allLocations)
        exampleStatement.execute(
            "INSERT INTO LOCATIONS VALUES (newlocation.getNodeID, newlocation.getXCoord(), newlocation.getYCoord(), newlocation.getFloor(), newlocation.getBuilding(), newlocation.getNodeType(), newlocation.getLongName(), newlocation.getShortName())");
      Vdb.saveToFile(Vdb.Database.Location);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteLocation(String nodeID) {

    try {
      System.out.println("Sending to database...");
      Connection connection;
      connection = DriverManager.getConnection("jdbc:derby:VDB;create=true", "admin", "admin");
      Statement exampleStatement = connection.createStatement();
      for (Location l : allLocations) {
        exampleStatement.execute("DELETE FROM LOCATIONS WHERE nodeID.equals(l.getNodeID())");
      }

      allLocations.remove(getLocation(nodeID));
      Vdb.saveToFile(Vdb.Database.Location);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateLocationDB(String nodeID) throws SQLException {}

  public void setAllLocations(ArrayList<Location> locations) {
    allLocations = locations;
  }
}
