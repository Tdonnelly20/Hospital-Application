package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.map.EquipmentIcon;
import lombok.Setter;

@Setter
public class Equipment {
  private String ID;
  private double x;
  private double y;
  private String floor;
  private String name;
  private String description;
  private boolean isDirty;
  private EquipmentIcon icon;

  public Equipment(
      String ID,
      String name,
      String floor,
      double x,
      double y,
      String description,
      Boolean isDirty) {
    this.ID = ID;
    this.floor = floor;
    this.x = x;
    this.y = y;
    this.name = name;
    this.description = description;
    this.isDirty = isDirty;
  }

  public EquipmentIcon getIcon() {
    return icon;
  }

  public Equipment getFloor() {
    return floor;
  }

  public String getID() {
    return ID;
  }

  public String getName() {
    return name;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public String getDescription() {
    return description;
  }

  public boolean getIsDirty() {
    return isDirty;
  }

  public void setIsDirty(boolean bool) {
    isDirty = bool;
  }

  public String setIsDirtyString(String str) {
    if (str.equals("Dirty")) {
      isDirty = true;
    }
    return "Clean";
  }

  public void setIcon(EquipmentIcon icon) {
    this.icon = icon;
  }

  public String getIsDirtyString() {
    if (isDirty) return "Dirty";
    return "Clean";
  }

  public void updateLocation(double x, double y) {
    setX(x);
    setY(y);
  }
}
