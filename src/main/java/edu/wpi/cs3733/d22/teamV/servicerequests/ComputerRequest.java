package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

public class ComputerRequest extends ServiceRequest {
  private String typeName;

  /**
   * Creates a basic data structure for holding computer request
   *
   * @param patientID
   * @param employeeID
   * @param roomLocation
   * @param typeName
   * @param requestDetails
   */
  public ComputerRequest(
      int patientID,
      int employeeID,
      String roomLocation,
      String typeName,
      String requestDetails,
      String status,
      String date) {
    if (date != "") {
      this.timeMade = Timestamp.valueOf(date);

    } else {
      this.timeMade = Timestamp.from(Instant.now());
    }
    this.details = requestDetails;
    this.location = Vdb.requestSystem.getLocation(roomLocation);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.typeName = typeName;
    this.type = "Computer Request";
    this.status = status;
  }

  public ComputerRequest(
      int patientID,
      int employeeID,
      String roomLocation,
      String typeName,
      String requestDetails,
      String status) {
    this.timeMade = Timestamp.from(Instant.now());
    this.details = requestDetails;
    this.location = Vdb.requestSystem.getLocation(roomLocation);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.typeName = typeName;
    this.type = "Computer Request";
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  public String getTypeName() {
    return typeName;
  }

  public String getRequestDetails() {
    return details;
  }
}
