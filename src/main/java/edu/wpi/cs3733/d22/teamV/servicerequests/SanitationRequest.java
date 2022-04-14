package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Location;

public class SanitationRequest extends ServiceRequest {
  private String roomLocation, hazardName, requestDetails;
  private int patientID, hospitalID, serviceID;

  /**
   * Creates a basic data structure for holding medicine delivery request
   *
   * @param patientID
   * @param hospitalID
   * @param nodeID
   * @param hazardName
   * @param requestDetails
   */
  public SanitationRequest(
      int patientID, int hospitalID, String nodeID, String hazardName, String requestDetails) {

    this.requestDetails = requestDetails;
    if (RequestSystem.getSystem().getLocation(nodeID) == null) {
      this.location = new Location(nodeID);
    } else {
      this.location = Vdb.requestSystem.getLocation(nodeID);
    }
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.hospitalEmployee = Vdb.requestSystem.getEmployeeDao().getEmployee(hospitalID);
    this.patientID = patientID;
    this.hospitalID = hospitalID;
    this.hazardName = hazardName;
    this.roomLocation = nodeID;
    this.type = "Sanitation Request";
    this.status = "Not Started";
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

  public int getHospitalID() {
    return hospitalID;
  }

  public String getHazardName() {
    return hazardName;
  }

  public String getRequestDetails() {
    return requestDetails;
  }

  public String getRoomLocation() {
    return location.getNodeID();
  }
}
