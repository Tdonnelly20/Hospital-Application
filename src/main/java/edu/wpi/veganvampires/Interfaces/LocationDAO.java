package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Location;

import java.util.List;

public interface LocationDAO {
  public List<Location> getAllLocations();

  public Location getLocation(int ID);

  public void updateLocation(Location location);

  public void deleteLocation(Location location);
}
