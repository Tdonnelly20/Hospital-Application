package edu.wpi.veganvampires.objects;

import edu.wpi.veganvampires.main.Vdb;

public class EquipmentDelivery extends ServiceRequest {
  private Patient patient;
  private HospitalEmployee employee;
  private String equipment, notes, status;
  private int quantity;

  public EquipmentDelivery(
      String nodeID, int employeeID, String equipment, String notes, int quantity, String status) {
    this.patient = new Patient(0, "john", "Johnson");
    this.employee = new HospitalEmployee(employeeID);
    this.status = "";
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.equipment = equipment;
    this.notes = notes;
    this.quantity = quantity;
    this.status = status;
  }

  public EquipmentDelivery(
      int employeeID,
      int patientID,
      String patientFirstName,
      String patientLastName,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status) {
    employee = new HospitalEmployee(employeeID);
    patient = new Patient(patientID, patientFirstName, patientLastName);
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.equipment = equipment;
    this.notes = notes;
    this.quantity = quantity;
    this.status = status;
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public int getEmployeeID() {
    return employee.getHospitalID();
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

  public Patient getPatient() {
    return patient;
  }

  public HospitalEmployee getEmployee() {
    return employee;
  }

  public String getStatus() {
    return status;
  }
}
