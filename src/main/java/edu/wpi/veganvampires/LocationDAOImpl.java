package edu.wpi.veganvampires;

import java.sql.*;
import edu.wpi.veganvampires.Interfaces.LocationDAO;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements LocationDAO {

  List<Location> locationList;
  Connection connection = Vdb.Connect();

  public LocationDAOImpl() {
    locationList = new ArrayList<>();
  }

  public LocationDAOImpl(List<Location> list) {
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
          /*st.execute(
          "UPDATE LOCATIONS SET xCoord = location.getXCoord(),"
              + "yCoord = location.getYCoord(),"
              + "nodeType = location.getNodeType(),"
              + "floor =location.getFloor(),"
              + "building = location.getBuilding(),"
              + "shortName = location.getShortName(),"
              + "longName = location.getLongName()"
              + "WHERE nodeID = location.getNodeID()");*/
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
          /* st.execute("DELETE FROM Locations WHERE nodeID = " + location.getNodeID());*/
        } catch (SQLException e) {
          System.out.println("Connection failed. Check output console.");
          e.printStackTrace();
          return;
        }
      }
    }
  }
}
