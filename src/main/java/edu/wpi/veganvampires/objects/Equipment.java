package edu.wpi.veganvampires.objects;

public class Equipment {
  private int ID;
  private double x;
  private double y;
  private String floor;
  private String name;
  private String description;
  private boolean isDirty;

  public Equipment(
      int ID, String name, String floor, double x, double y, String description, Boolean isDirty) {
    this.ID = ID;
    this.floor=floor;
    this.x = x;
    this.y = y;
    this.name = name;
    this.description = description;
    this.isDirty = isDirty;
  }

  public String getFloor(){
    return floor;
  }

  public int getID(){
    return ID;
  }
  public String getName(){
    return name;
  }
  public double getX(){
    return x;
  }
  public double getY(){
    return y;
  }
  public String getDescription(){
    return description;
  }
  public boolean getisDirty(){
    return isDirty;
  }
}
