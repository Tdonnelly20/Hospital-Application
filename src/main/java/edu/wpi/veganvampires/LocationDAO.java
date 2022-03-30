package edu.wpi.veganvampires;

import java.util.List;

public interface LocationDAO {
  public List<Location> getAllLocations();

  public Location getLocation(Location location);

  public void updateLocation(Location location);

  public void deleteLocation(Location location);
}
