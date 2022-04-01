package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.Location;
import java.sql.SQLException;
import java.util.List;

public interface LocationImpl {
  List<Location> getAllLocations();

  void addLocation(Location location) throws SQLException;

  void deleteLocation(Location location) throws SQLException;
}
