package edu.wpi.cs3733.d22.teamV.ServiceRequests;

public class SanitationRequest extends ServiceRequest {
  private final String patientFirstName, patientLastName, roomLocation, hazardName, requestDetails;
  private final int patientID, hospitalID, serviceID;

  /**
   * Creates a basic data structure for holding medicine delivery request
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param roomLocation
   * @param hazardName
   * @param requestDetails
   */
  public SanitationRequest(
      String patientFirstName,
      String patientLastName,
      int patientID,
      int hospitalID,
      String roomLocation,
      String hazardName,
      String requestDetails,
      int serviceID) {

    this.requestDetails = requestDetails;
    this.patientFirstName = patientFirstName;
    this.patientLastName = patientLastName;
    this.patientID = patientID;
    this.hospitalID = hospitalID;
    this.hazardName = hazardName;
    this.roomLocation = roomLocation;
    this.serviceID = serviceID;
  }

  public String getPatientFirstName() {
    return patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
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
    return roomLocation;
  }
}
