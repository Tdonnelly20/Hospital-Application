package edu.wpi.veganvampires;

public class EquipmentDelivery {
  private final String location, equipment, notes;
  private final int quantity;

  public EquipmentDelivery(String location, String equipment, String notes, int quantity) {
    this.location = location;
    this.equipment = equipment;
    this.notes = notes;
    this.quantity = quantity;
  }

  public String getLocation() {
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
