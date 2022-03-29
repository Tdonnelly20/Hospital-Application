package edu.wpi.veganvampires.Features;

public class SanitationRequest {
  private final String patientFirstName, patientLastName, hazardName, requestDetails;
  private final int patientID;

  /**
   * Creates a basic data structure for holding medicine delivery request
   *
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hazardName
   * @param requestDetails
   */
  public SanitationRequest(
      String patientFirstName,
      String patientLastName,
      int patientID,
      String hazardName,
      String requestDetails) {

    this.requestDetails = requestDetails;
    this.patientFirstName = patientFirstName;
    this.patientLastName = patientLastName;
    this.patientID = patientID;
    this.hazardName = hazardName;
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

  public String getHazardName() {
    return hazardName;
  }

  public String getRequestDetails() {
    return requestDetails;
  }
}
