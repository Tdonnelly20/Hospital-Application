package edu.wpi.cs3733.d22.teamV.serviceRequest;

public class ReligiousRequest extends ServiceRequest {
  private final String firstName, lastName;
  private final int patientID;
  private int userID = 0;
  private boolean Christian, Jewish, Protestant, Islam, Muslim, Buddhist, Hindu, Other;
  private String specialRequests, religion;
  private int serviceID;

  /**
   * @param firstName
   * @param lastName
   * @param userID
   * @param patientID
   * @param religion
   * @param specialRequests
   */
  public ReligiousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      String religion,
      String specialRequests,
      int serviceID) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.patientID = userID;
    this.userID = patientID;
    this.religion = religion;
    this.serviceID = serviceID;
    this.specialRequests = specialRequests;
  }

  public String getPatientFirstName() {
    return firstName;
  }

  public String getPatientLastName() {
    return lastName;
  }

  public int getPatientID() {
    return patientID;
  }

  public int getServiceID() {
    return serviceID;
  }

  public int getEmpID() {
    return userID;
  }

  public String getReligion() {
    return religion;
  }

  public String getSpecialRequests() {
    return specialRequests;
  }
}
