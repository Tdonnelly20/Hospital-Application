package edu.wpi.veganvampires.interfaces;

import edu.wpi.veganvampires.objects.Location;
import java.util.List;

public interface LocationImpl {

  List<Location> getAllLocations();

  void addLocation(Location new_Location);

  void removeLocation();

  void updateLocationsDB(Location new_location);
}
