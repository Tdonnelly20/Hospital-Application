package edu.wpi.veganvampires.objects;

public class Equipment {
  private int ID;
  private double x;
  private double y;
  private String name;
  private String description;
  private int count;
  private boolean isDirty;

  public Equipment(int ID, double x, double y, String name, String description, int count, Boolean isDirty) {
    this.ID=ID;
    this.x = x;
    this.y = y;
    this.name = name;
    this.description = description;
    this.count = count;
    this.isDirty = isDirty;
  }
}
