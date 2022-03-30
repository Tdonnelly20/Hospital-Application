package edu.wpi.veganvampires.Dao;

import edu.wpi.veganvampires.Features.Location;
import edu.wpi.veganvampires.Interfaces.LocationImpl;
import edu.wpi.veganvampires.Vdb;
import java.util.ArrayList;
import java.util.List;

public class LocationDao implements LocationImpl {
  private static ArrayList<Location> allLocations;

  /** Initialize the array list */
  public LocationDao() {
    allLocations = new ArrayList<>();
    // TODO: Add info from the database to the local arraylist
  }

  @Override
  public List<Location> getAllLocations() {
    return allLocations;
  }

  @Override
  public void removeLocation() {}

  @Override
  public void addLocation(Location new_location) {
    System.out.println("Adding to local arraylist...");
    allLocations.add(new_location);
    updateLocationsDB(new_location);
  }

  @Override
  public void updateLocationsDB(Location new_location) {
    System.out.println("Sending to database...");
    Vdb.addLocations(new_location);
  }
}
