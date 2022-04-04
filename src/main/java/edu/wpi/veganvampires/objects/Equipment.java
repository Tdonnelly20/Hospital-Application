package edu.wpi.veganvampires.objects;

public class Equipment {
  private int ID;
  private Location location;
  private String name;
  private String description;
  private int count;
  private boolean isDirty;

  public Equipment(int ID, Location location, String name, String description, int count, Boolean isDirty) {
    this.location = location;
    this.name = name;
    this.description = description;
    this.count = count;
    this.isDirty = isDirty;
  }
}
