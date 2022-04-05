package edu.wpi.veganvampires.objects;

import edu.wpi.veganvampires.main.Vdb;

public class EquipmentDelivery extends ServiceRequest {
  private final String equipment, notes;
  private final int quantity;

  public EquipmentDelivery(
      String nodeID, int userID, String equipment, String notes, int quantity, String status) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(userID);
    this.patient = null;
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
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
