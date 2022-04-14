package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;

public class LaundryRequest extends ServiceRequest {
  String details;

  public LaundryRequest(int employeeID, int patientID, String nodeID, String details) {
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.hospitalEmployee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.details = details;
    this.type = "Laundry Request";
  }

  public LaundryRequest(
      int employeeID, int patientID, String nodeID, String details, String status) {
    this.location = RequestSystem.getSystem().getLocationDao().getLocation(nodeID);
    this.hospitalEmployee = new Employee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.details = details;
    this.status = status;
  }

  public String getDetails() {
    return details;
  }

  public int getEmployeeID() {
    return hospitalEmployee.getEmployeeID();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public String getFirstName() {
    return patient.getFirstName();
  }

  public String getLastName() {
    return patient.getFirstName();
  }

  public String getLocationID() {
    return location.getNodeID();
  }
}
