package edu.wpi.veganvampires.objects;

import edu.wpi.veganvampires.main.Vdb;

public class EquipmentDelivery extends ServiceRequest {
  private final String equipment, notes;
  private final int quantity;

  public EquipmentDelivery(
      int userID, String nodeID, String equipment, String notes, int quantity, String status) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(userID);
    this.patient = null;
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
    this.quantity = quantity;
    this.status = status;
  }

  public EquipmentDelivery(
      int userID,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status,
      int pID) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(userID);
    // this.patient = ;//needs to find patient
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
    this.quantity = quantity;
    this.status = status;
  }

  public EquipmentDelivery(
      int userID,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status,
      int pID,
      String fname,
      String lname) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(userID);
    this.patient = new Patient(pID, fname, lname);
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
    this.quantity = quantity;
    this.status = status;
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

  public String getStatus() {
    return status;
  }
}
