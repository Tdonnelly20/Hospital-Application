package edu.wpi.veganvampires.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ServiceRequest {
  protected Location location;
  protected Patient patient;
  protected int userID;
  protected String desc;//Description/type of request ex. Lab Request (Blood)
  protected String status;
  protected Icon icon;

  public String getRequestName() {
    if (patient != null) {
      System.out.println(patient.getLastName() + ", " + patient.getFirstName());
      return desc + ": " + patient.getLastName() + ", " + patient.getFirstName();
    }
    return desc;
  }
}
