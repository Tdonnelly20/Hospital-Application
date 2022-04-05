package edu.wpi.veganvampires.objects;

public class EquipmentDelivery {
  private Patient patient;
  private HospitalEmployee employee;
  private String location, equipment, notes, status;
  private int quantity;

  public EquipmentDelivery(String location, String equipment, String notes, int quantity) {
    this.patient = new Patient(0, "john", "Johnson");
    this.employee = new HospitalEmployee(0);
    this.status = "";
    this.location = location;
    this.equipment = equipment;
    this.notes = notes;
    this.quantity = quantity;
  }

  public EquipmentDelivery(
      int employeeID,
      int patientID,
      String patientFirstName,
      String patientLastName,
      String location,
      String equipment,
      String notes,
      int quantity,
      String status) {
    employee = new HospitalEmployee(employeeID);
    patient = new Patient(patientID, patientFirstName, patientLastName);
    this.location = location;
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
