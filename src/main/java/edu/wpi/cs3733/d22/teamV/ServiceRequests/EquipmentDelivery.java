package edu.wpi.cs3733.d22.teamV.ServiceRequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.HospitalEmployee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import javafx.scene.image.Image;

public class EquipmentDelivery extends ServiceRequest {
  private final String equipment, notes;
  private final int quantity;
  private String patientFirstName, patientLastName, locationName;
  private int employeeID, patientID;

  public EquipmentDelivery(
      int userID, String nodeID, String equipment, String notes, int quantity, String status) {
    this.location = Vdb.locationDao.getLocation(nodeID);
    this.hospitalEmployee = new HospitalEmployee(userID);
    this.patient = new Patient(123, "James", "Jameson");
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
    this.quantity = quantity;
    this.status = status;
    this.patientFirstName = patient.getFirstName();
    this.patientLastName = patient.getLastName();
    this.employeeID = hospitalEmployee.getHospitalID();
    this.patientID = patient.getPatientID();
    this.locationName = location.getShortName();
    this.image = new Image("Equipment.png");
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
    this.equipment = equipment;
    this.notes = notes;
    this.desc = "Equipment Delivery (" + equipment + ")";
    this.status = status;
    this.quantity = quantity;
    this.status = status;
    patientFirstName = patient.getFirstName();
    patientLastName = patient.getLastName();
    employeeID = hospitalEmployee.getHospitalID();
    patientID = patient.getPatientID();
    locationName = location.getShortName();
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
    patientFirstName = fname;
    patientLastName = lname;
    employeeID = hospitalEmployee.getHospitalID();
    patientID = pID;
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
