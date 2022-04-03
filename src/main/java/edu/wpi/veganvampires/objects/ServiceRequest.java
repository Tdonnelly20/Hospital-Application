package edu.wpi.veganvampires.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ServiceRequest {
  private Location location;
  private Patient patient;
  private int userID;
  private String status;
  private Icon icon;

  public String getRequestName() {
    if (patient != null) {
      return patient.getLastName() + ", " + patient.getFirstName();
    }
    return "" + userID;
  }
}
