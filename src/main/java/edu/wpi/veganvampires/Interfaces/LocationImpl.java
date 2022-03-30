package edu.wpi.veganvampires.Interfaces;

import edu.wpi.veganvampires.Features.Location;
import java.util.List;

public interface LocationImpl {

  List<Location> getAllLocations();

  void addLocation(Location new_Location);

  void removeLocation();

  void updateLocationsDB(Location new_location);
}
