package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import java.time.Instant;

public class SanitationRequest extends ServiceRequest {
  private String roomLocation, hazardName, requestDetails;
  private int patientID, employeeID, serviceID;

  /**
   * Creates a basic data structure for holding medicine delivery request
   *
   * @param patientID
   * @param employeeID
   * @param roomLocation
   * @param hazardName
   * @param requestDetails
   */
  public SanitationRequest(
      int patientID,
      int employeeID,
      String roomLocation,
      String hazardName,
      String requestDetails) {
    this.timeMade = Timestamp.from(Instant.now());
    this.requestDetails = requestDetails;
    this.location = Vdb.requestSystem.getLocation(roomLocation);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patientID = patientID;
    this.employeeID = employeeID;
    this.hazardName = hazardName;
    this.roomLocation = roomLocation;
    this.type = "Sanitation Request";
    status = "Not Started";
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public int getPatientID() {
    return patientID;
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public String getHazardName() {
    return hazardName;
  }

  public String getRequestDetails() {
    return requestDetails;
  }

  public String getRoomLocation() {
    return roomLocation;
  }
}
