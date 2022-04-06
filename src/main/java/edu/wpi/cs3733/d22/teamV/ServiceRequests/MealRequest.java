package edu.wpi.cs3733.d22.teamV.ServiceRequests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MealRequest {
  private final int userID;
  private final int patientID;
  private final String firstName;
  private final String lastName;
  private final String meal;

  public MealRequest(int userID, int patientID, String firstName, String lastName, String meal) {
    this.userID = userID;
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.meal = meal;
  }
}
