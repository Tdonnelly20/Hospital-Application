package edu.wpi.veganvampires.objects;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Floor {
  private static String floorName;
  private ArrayList<Icon> iconList;

  public Floor() {
    floorName = "";
    iconList = new ArrayList<Icon>();
  }

  public void addIcon(Icon icon){
    iconList.add(icon);
  }
}
