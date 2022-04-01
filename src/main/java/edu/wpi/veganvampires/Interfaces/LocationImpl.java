package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Location;
import java.sql.SQLException;
import java.util.List;

public interface LocationImpl {
  List<Location> getAllLocations();

  void addLocation(Location location) throws SQLException;

  void deleteLocation(Location location) throws SQLException;
}
