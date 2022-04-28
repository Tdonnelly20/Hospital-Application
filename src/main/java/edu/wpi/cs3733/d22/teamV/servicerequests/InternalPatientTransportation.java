package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

public class InternalPatientTransportation extends ServiceRequest {

  /**
   * @param patientID
   * @param employeeID
   * @param nodeID
   * @param requestDetails
   */
  public InternalPatientTransportation(
      String nodeID,
      int patientID,
      int employeeID,
      String requestDetails,
      String status,
      String date) {
    if (date != "") {
      this.timeMade = Timestamp.valueOf(date);

    } else {
      this.timeMade = Timestamp.from(Instant.now());
    }
    this.location = RequestSystem.getSystem().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    System.out.println(employee.getEmployeeID());
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.details = requestDetails;
    this.type = "Internal Patient Transportation Request";
    this.status = status;
  }

  public InternalPatientTransportation(
      String nodeID, int patientID, int employeeID, String requestDetails, String status) {
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    System.out.println(employee.getEmployeeID());
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.details = requestDetails;
    this.type = "Internal Patient Transportation Request";
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  public String getRequestDetails() {
    return details;
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    super.update(directionalAssoc);
    Vdb.requestSystem
        .getDao(RequestSystem.Dao.InternalPatientTransportation)
        .updateServiceRequest(this, getServiceID());
  }
}
