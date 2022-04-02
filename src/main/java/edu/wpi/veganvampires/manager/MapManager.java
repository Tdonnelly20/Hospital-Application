package edu.wpi.veganvampires.manager;

import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Floor;
import edu.wpi.veganvampires.objects.Location;
import java.util.ArrayList;

// Singleton
public class MapManager {
  private static MapManager manager;
  private ArrayList<Floor> floorList;

  private MapManager() {
    for (int i = 0; i < 6; i++) {
      floorList.add(new Floor());
    }
    ArrayList<Location> locations = (ArrayList<Location>) Vdb.locations;
    for (Location l : locations) {

    }

    // TODO add locations for floors and name floors
  }

  private static class SingletonHelper {
    private static final MapManager manager = new MapManager();
  }

  public static MapManager getManager() {
    return SingletonHelper.manager;
  }

  public Floor getFloor(String str) {
    switch (str) {
      case "Ground Floor":
        return floorList.get(0);
      case "Lower Level 1":
        return floorList.get(1);
      case "Lower Level 2":
        return floorList.get(2);
      case "Floor 1":
        return floorList.get(3);
      case "Floor 2":
        return floorList.get(4);
      case "Floor 3":
        return floorList.get(5);
    }
    return null;
  }
}
