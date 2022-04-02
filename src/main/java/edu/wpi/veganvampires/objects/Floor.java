package edu.wpi.veganvampires.objects;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Floor {
  private static String floorName;
  private ArrayList<Icon> iconList;

  public Floor(String floorName) {
    this.floorName = floorName;
    iconList = new ArrayList<Icon>();
  }

  public void addIcon(Icon icon) {
    iconList.add(icon);
  }

  public static String getFloorName() {
    return floorName;
  }
}
