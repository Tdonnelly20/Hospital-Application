package edu.wpi.veganvampires;

import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements LocationDAO {

  List<Location> locationList;

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
  public Location getLocation(int ID) {
    return locationList.get(ID);
  }

  @Override
  public void updateLocation(Location location) {
    locationList.get(location.ID).setXCoord(location.xCoord);
    locationList.get(location.ID).setYCoord(location.yCoord);
    locationList.get(location.ID).setNodeID(location.nodeID);
    locationList.get(location.ID).setNodeType(location.nodeType);
    locationList.get(location.ID).setFloor(location.floor);
    locationList.get(location.ID).setBuilding(location.building);
    locationList.get(location.ID).setShortName(location.shortName);
    locationList.get(location.ID).setLongName(location.longName);
  }

  @Override
  public void deleteLocation(Location location) {
    locationList.remove(location.ID);
  }
}
