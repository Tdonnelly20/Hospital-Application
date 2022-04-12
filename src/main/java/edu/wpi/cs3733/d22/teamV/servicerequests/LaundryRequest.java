package edu.wpi.cs3733.d22.teamV.servicerequests;

public class LaundryRequest extends ServiceRequest {
  String firstName, lastName, details;
  int roomNumber, userID, patientID;

  public LaundryRequest(
      int userID,
      int patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details) {
    this.userID = userID;
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roomNumber = roomNumber;
    this.details = details;
  }

  public int getUserID() {
    return getUserID();
  }

  public int getPatientID() {
    return patientID;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public int getRoomNumber() {
    return roomNumber;
  }

  public String getDetails() {
    return details;
  }
}
