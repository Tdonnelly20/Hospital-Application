package edu.wpi.veganvampires.objects;

public class LaundryRequest {
  String userID, patientID, firstName, lastName, details;
  int roomNumber;

  public LaundryRequest(
      String userID,
      String patientID,
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

  public String getUserID() {
    return getUserID();
  }

  public String getPatientID() {
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
