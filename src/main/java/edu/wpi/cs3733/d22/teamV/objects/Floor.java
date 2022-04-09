package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.map.Icon;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Floor {
  private String floorName;
  private ArrayList<Icon> iconList;

  public Floor(String floorName) {
    this.floorName = floorName;
    iconList = new ArrayList<Icon>();
  }

  public void addIcon(Icon icon) {
    iconList.add(icon);
  }

  public String getFloorName() {
    return floorName;
  }

  public boolean hasIconAt(Location location) {
    for (Icon icon : iconList) {
      if (icon.getLocation().equals(location)) {
        return true;
      }
    }
    return false;
  }

  public Icon getIcon(Location location) {
    for (Icon icon : iconList) {
      if (icon.getLocation().equals(location)) {
        return icon;
      }
    }
    return null;
  }
}
