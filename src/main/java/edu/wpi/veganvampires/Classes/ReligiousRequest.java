package edu.wpi.veganvampires.Classes;

public class ReligiousRequest {
  private final String firstName, lastName;
  private final int patientID;
  private int userID = 0;
  private boolean Christian, Jewish, Protestant, Islam, Muslim, Buddhist, Hindu, Other;
  private String specialRequests;

  /**
   * @param firstName
   * @param lastName
   * @param userID
   * @param patientID
   * @param christian
   * @param jewish
   * @param protestant
   * @param islam
   * @param muslim
   * @param buddhist
   * @param hindu
   * @param other
   * @param specialRequests
   */
  public ReligiousRequest(
      String firstName,
      String lastName,
      int patientID,
      int userID,
      boolean christian,
      boolean jewish,
      boolean protestant,
      boolean islam,
      boolean muslim,
      boolean buddhist,
      boolean hindu,
      boolean other,
      String specialRequests) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.patientID = userID;
    this.userID = patientID;
    this.Christian = christian;
    this.Jewish = jewish;
    this.Protestant = protestant;
    this.Islam = islam;
    this.Muslim = muslim;
    this.Buddhist = buddhist;
    this.Hindu = hindu;
    this.Other = other;
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
}
