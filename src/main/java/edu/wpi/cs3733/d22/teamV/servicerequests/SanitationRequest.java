package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
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
      String requestDetails,
      String status,
      int serviceID,
      String date) {
    if (date != "") {
      this.timeMade = Timestamp.valueOf(date);

    } else {
      this.timeMade = Timestamp.from(Instant.now());
    }
    this.requestDetails = requestDetails;
    this.location = Vdb.requestSystem.getLocation(roomLocation);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    this.employee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patientID = patientID;
    this.employeeID = employeeID;
    this.hazardName = hazardName;
    this.roomLocation = roomLocation;
    this.type = "Sanitation Request";
    this.status = status;
    setServiceID(RequestSystem.getServiceID());
    if (serviceID < 0) { // calls system to set id
      // setServiceID(RequestSystem.getServiceID());
    } else {
      // setServiceID(serviceID);
    }
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
}
