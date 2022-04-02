package edu.wpi.veganvampires.manager;

import edu.wpi.veganvampires.main.Vdb;
import edu.wpi.veganvampires.objects.Floor;
import edu.wpi.veganvampires.objects.Icon;
import edu.wpi.veganvampires.objects.Location;
import java.util.ArrayList;

// Singleton
public class MapManager {
  private ArrayList<Floor> floorList;

  private MapManager() {
    floorList = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      floorList.add(new Floor());
    }
    ArrayList<Location> locations = Vdb.locations;
    for (Location l : locations) {
      Icon icon = new Icon(l);
      switch (l.getFloor()) {
        case "G":
          floorList.get(0).addIcon(icon);
          break;
        case "L1":
          floorList.get(1).addIcon(icon);
          break;
        case "L2":
          floorList.get(2).addIcon(icon);
          break;
        case "1":
          floorList.get(3).addIcon(icon);
          break;
        case "2":
          floorList.get(4).addIcon(icon);
          break;
        case "3":
          floorList.get(5).addIcon(icon);
          break;
      }
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
      case "G":
        return floorList.get(0);
      case "L1":
        return floorList.get(1);
      case "L2":
        return floorList.get(2);
      case "1":
        return floorList.get(3);
      case "2":
        return floorList.get(4);
      case "3":
        return floorList.get(5);
    }
    return null;
  }
}
