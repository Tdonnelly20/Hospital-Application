package edu.wpi.cs3733.d22.teamV.serviceRequest;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.HospitalEmployee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;

public class EquipmentDelivery extends ServiceRequest {
  private final String equipment, notes;
  private final int quantity;
  private String patientFirstName, patientLastName, locationName;
  private int employeeID, patientID;

  public EquipmentDelivery(
      int employeeID,
      int patientID,
      String patientFirstName,
      String patientLastName,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status,
      int serviceID) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(employeeID);
    this.patient = new Patient(patientID, patientFirstName, patientLastName);
    this.equipment = equipment;
    this.notes = notes;
    this.type = "Equipment Delivery";
    this.status = status;
    setServiceID(serviceID);
    this.quantity = quantity;
    this.status = status;
    this.patientFirstName = patientFirstName;
    this.patientLastName = patientLastName;
    this.employeeID = employeeID;
    this.patientID = patientID;
    locationName = location.getShortName();
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
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(employeeID);
    this.patient = new Patient(patientID, patientFirstName, patientLastName);
    this.equipment = equipment;
    this.notes = notes;
    this.type = "Equipment Delivery";
    this.status = status;
    this.quantity = quantity;
    this.status = status;
    this.patientFirstName = patientFirstName;
    this.patientLastName = patientLastName;
    this.employeeID = employeeID;
    this.patientID = patientID;
    locationName = location.getShortName();
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

  public String getPatientFirstName() {
    return patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
  }

  public String getLocationName() {
    return locationName;
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public int getPatientID() {
    return patientID;
  }
}
