package edu.wpi.veganvampires.objects;

import edu.wpi.veganvampires.main.Vdb;

public class EquipmentDelivery extends ServiceRequest {
  int employeeID;
  int patientID;
  String patientFirstName;
  String patientLastName;
  private final String equipment, notes;
  private final int quantity;

  public EquipmentDelivery(
      String nodeID, int userID, String equipment, String notes, int quantity, String status) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(userID);
    this.patient = new Patient(123, "James", "Jameson");
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
    this.quantity = quantity;
    employeeID = hospitalEmployee.getHospitalID();
    patientID = patient.getPatientID();
    patientFirstName = patient.getFirstName();
    patientLastName = patient.getLastName();
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public int getPatientID() {
    return patientID;
  }

  public String getPatientFirstName() {
    return patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
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
