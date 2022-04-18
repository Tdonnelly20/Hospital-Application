package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;

public class InternalPatientTransportation extends ServiceRequest {
  private Patient patient;
  private Employee employee;
  private String nodeID, requestDetails;
  private int employeeID;

  /**
   * @param patientID
   * @param employeeID
   * @param nodeID
   * @param requestDetails
   */
  public InternalPatientTransportation(
      String nodeID, int patientID, int employeeID, String requestDetails) {
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.nodeID = nodeID;
    this.employeeID = employeeID;
    this.requestDetails = requestDetails;
    this.type = "Internal Patient Transportation Request";
    this.status = "Not Started";
    this.dao = RequestSystem.Dao.InternalPatientTransportation;
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
    return employee.getEmployeeID();
  }

  public String getNodeID() {
    return nodeID;
  }

  public String getRequestDetails() {
    return requestDetails;
  }
}
