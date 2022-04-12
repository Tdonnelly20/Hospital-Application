package edu.wpi.cs3733.d22.teamV.ServiceRequests;

public class ReligiousRequest extends ServiceRequest {
  private String firstName = "";
  private String lastName = "";
  private int patientID;
  private int userID = 0;
  private String specialRequests;
  private int serviceID;

  /**
   * @param datum
   * @param patientID
   * @param userID
   * @param i
   * @param specialRequests
   * @param s
   * @param parseInt
   */
  public ReligiousRequest(
      String datum,
      int patientID,
      int userID,
      int i,
      String specialRequests,
      String s,
      int serviceID,
      int parseInt) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.patientID = userID;
    this.userID = patientID;
    this.serviceID = serviceID;
    this.specialRequests = specialRequests;
  }

  public ReligiousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      String x,
      String y,
      int specialRequests) {
    super();
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

  public String getSpecialRequests() {
    return specialRequests;
  }
}
