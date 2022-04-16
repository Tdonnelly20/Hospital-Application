package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;

public class EquipmentDelivery extends ServiceRequest {
  private final String equipment, notes;
  private final int quantity;

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
    this.location = RequestSystem.getSystem().getLocationDao().getLocation(nodeID);
    this.employee = new Employee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.equipment = equipment;
    this.notes = notes;
    this.type = "Equipment Delivery";
    this.status = status;
    setServiceID(serviceID);
    this.quantity = quantity;
    this.status = status;
  }

  public EquipmentDelivery(
      int employeeID,
      int patientID,
      String nodeID,
      String equipment,
      String notes,
      int quantity,
      String status) {
    System.out.println(Vdb.requestSystem);
    this.location = RequestSystem.getSystem().getLocationDao().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.equipment = equipment;
    this.notes = notes;
    this.type = "Equipment Delivery";
    this.quantity = quantity;
    this.status = status;
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
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public String getLocationName() {
    return location.getNodeID();
  }

  public int getEmployeeID() {
    return super.getEmployee().getEmployeeID();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }
}
