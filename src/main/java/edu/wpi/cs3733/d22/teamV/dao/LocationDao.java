package edu.wpi.cs3733.d22.teamV.dao;

import edu.wpi.cs3733.d22.teamV.interfaces.LocationImpl;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Location;
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
        System.out.println("found node!" + l.getNodeID());
        return l;
      }
    }
    System.out.print("unable to find!");
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
            "INSERT INTO LOCATIONS VALUES ('"
                + location.getNodeID()
                + "',"
                + location.getXCoord()
                + ","
                + location.getYCoord()
                + ",'"
                + location.getFloor()
                + "','"
                + location.getBuilding()
                + "','"
                + location.getNodeType()
                + "','"
                + location.getLongName()
                + "','"
                + location.getShortName()
                + "')");
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
      Connection connection = Vdb.Connect();
      Statement exampleStatement = connection.createStatement();

      allLocations.remove(getLocation(nodeID));
      Vdb.saveToFile(Vdb.Database.Location);

      for (Location l : allLocations) {
        // exampleStatement.execute("DELETE FROM LOCATIONS WHERE nodeID = " + l.getNodeID());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setAllLocations(ArrayList<Location> locations) {
    allLocations = locations;
  }
}
