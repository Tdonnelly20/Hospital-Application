package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;

import java.sql.Timestamp;
import java.time.Instant;

public class InternalPatientTransportation extends ServiceRequest {
  private String nodeID, requestDetails;

  /**
   * @param patientID
   * @param employeeID
   * @param nodeID
   * @param requestDetails
   */
  public InternalPatientTransportation(
      String nodeID, int patientID, int employeeID, String requestDetails) {
    this.timeMade= Timestamp.from(Instant.now());
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    System.out.println(employee.getEmployeeID());
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.nodeID = nodeID;
    this.requestDetails = requestDetails;
    this.type = "Internal Patient Transportation Request";
    this.status = "Not Started";
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

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  public String getRequestDetails() {
    return requestDetails;
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    super.update(directionalAssoc);
    Vdb.requestSystem
        .getDao(RequestSystem.Dao.InternalPatientTransportation)
        .updateServiceRequest(this, getServiceID());
  }
}
