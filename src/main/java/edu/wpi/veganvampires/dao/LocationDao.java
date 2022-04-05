package edu.wpi.veganvampires.dao;

import edu.wpi.veganvampires.interfaces.LocationImpl;
import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Location;
import java.sql.*;
import java.util.ArrayList;

public class LocationDao implements LocationImpl {
  private static ArrayList<Location> allLocations;

  public LocationDao() {
    allLocations = new ArrayList<>();
  }

  public static ArrayList<Location> getAllLocations() {
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
      Vdb.saveToFile(Vdb.Database.Location);
      Vdb.addToLocationsTable(
          location.getNodeID(),
          location.getXCoord(),
          location.getYCoord(),
          location.getFloor(),
          location.getBuilding(),
          location.getNodeType(),
          location.getLongName(),
          location.getShortName());

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteLocation(String nodeID) {
    for (int i = 0; i < allLocations.size(); i++) {
      if (allLocations.get(i).getNodeID().equals(nodeID)) {
        allLocations.remove(i);
      }
    }
  }

  public void setAllLocations(ArrayList<Location> locations) {
    allLocations = locations;

    try {
      Vdb.saveToFile(Vdb.Database.Location);
    } catch (Exception e) {

    }
  }
}
