package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;

public class LaundryRequest extends ServiceRequest {
  String firstName, lastName, details, nodeID;
  int employeeID, patientID;

  public LaundryRequest(int employeeID, int patientID, String nodeID, String details) {
    this.employeeID = employeeID;
    this.patientID = patientID;
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.hospitalEmployee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.nodeID = nodeID;
    this.details = details;
  }

  public int getEmployeeID() {
    return getEmployeeID();
  }

  public int getPatientID() {
    return patientID;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getNodeID() {
    return nodeID;
  }

  public String getDetails() {
    return details;
  }
}
