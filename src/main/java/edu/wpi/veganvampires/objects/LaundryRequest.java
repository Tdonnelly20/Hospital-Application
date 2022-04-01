package edu.wpi.veganvampires.objects;

import edu.wpi.veganvampires.interfaces.LaundryRequestImpl;
import java.util.List;

public class LaundryRequest implements LaundryRequestImpl {
  private final String employeeID, patientID, firstName, lastName, details;
  private final int roomNumber;

  public LaundryRequest(
      String employeeID,
      String patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details) {
    this.employeeID = employeeID;
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roomNumber = roomNumber;
    this.details = details;
  }

  public String getEmployeeID() {
    return employeeID;
  }

  public String getPatientID() {
    return patientID;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getRoomNumber() {
    return roomNumber;
  }

  public String getDetails() {
    return details;
  }

  @Override
  public List<LaundryRequest> getAllLaundryRequests() {
    return null;
  }

  @Override
  public void addLaundryRequest(
      String employeeID,
      String patientID,
      String firstName,
      String lastName,
      int roomNumber,
      String details) {}

  @Override
  public void removeLaundryRequest() {}
}
