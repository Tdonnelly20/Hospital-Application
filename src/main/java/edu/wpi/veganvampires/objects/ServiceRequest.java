package edu.wpi.veganvampires.objects;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ServiceRequest {
  protected Location location;
  protected Patient patient;
  protected HospitalEmployee hospitalEmployee;
  protected String status;
  protected Icon icon;

  public String getRequestName() {
    if (patient != null) {
      System.out.println(patient.getLastName() + ", " + patient.getFirstName());
      return patient.getLastName() + ", " + patient.getFirstName();
    }
    return "" + hospitalEmployee.getHospitalID();
  }
}
