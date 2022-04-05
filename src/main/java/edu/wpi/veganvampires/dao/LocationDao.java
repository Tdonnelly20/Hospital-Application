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
    for(int i = 0; i < allLocations.size(); i++)
    {
      if(allLocations.get(i).getNodeID().equals(nodeID))
      {
        allLocations.remove(i);
      }
    }
    /*try {
      System.out.println("Sending to database...");
      Connection connection = Vdb.Connect();
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
    }*/
  }

  public void setAllLocations(ArrayList<Location> locations) {
    allLocations = locations;
    try {
      Connection connect = Vdb.Connect();
      Statement st = connect.createStatement();
      for (int i = 0; i < locations.size(); i++) {
        st.execute(
            "INSERT INTO LOCATIONS VALUES(locations.get(i).getNodeID(), locations.get(i).getXCoord(),"
                + "locations.get(i).getYCoord(), locations.get(i).getFloor(), locations.get(i).getBuilding(), locations.get(i).getNodeType(),"
                + "locations.get(i).getLongName(), locations.get(i).getShortName())");
      }
      Vdb.saveToFile(Vdb.Database.Location);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
