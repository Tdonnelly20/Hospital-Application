package edu.wpi.veganvampires.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabRequest {
  private final int userID;
  private final int patientID;
  private final String firstName;
  private final String lastName;
  private final String lab;

  public LabRequest(int userID, int patientID, String firstName, String lastName, String lab) {
    this.userID = userID;
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.lab = lab;
  }
}
