package edu.wpi.veganvampires.objects;


public class EquipmentDelivery extends ServiceRequest{
  private final Location location;
  private final String equipment, notes;
  private final int quantity;

  public EquipmentDelivery(Location location, String equipment, String notes, int quantity) {
    this.location = location;
    this.equipment = equipment;
    this.notes = notes;
    this.quantity = quantity;
  }

  public Location getLocation() {
    return location;
  }

  public String getEquipment() {
    return equipment;
  }

  public String getNotes() {
    return notes;
  }

  public int getQuantity() {
    return quantity;
  }
}
