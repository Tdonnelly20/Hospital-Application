package edu.wpi.veganvampires.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ServiceRequest {
  protected Location location;
  protected Patient patient;
  protected int userID;
  protected String status;
  protected Icon icon;

  public String getRequestName() {
    if (patient != null) {
      return patient.getLastName() + ", " + patient.getFirstName();
    }
    return "" + userID;
  }
}
