package edu.wpi.veganvampires.ServiceRequests;

import edu.wpi.veganvampires.objects.HospitalEmployee;
import edu.wpi.veganvampires.objects.Icon;
import edu.wpi.veganvampires.objects.Location;
import edu.wpi.veganvampires.objects.Patient;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ServiceRequest {
  protected Location location;
  public Patient patient;
  protected HospitalEmployee hospitalEmployee;
  protected String date;
  protected String desc;
  protected String status;
  protected Icon icon;
  public Image image;

  public String getRequestName() {
    if (patient != null) {
      System.out.println(patient.getLastName() + ", " + patient.getFirstName());
      return desc + ": " + patient.getLastName() + ", " + patient.getFirstName();
    }
    return desc;
  }

  public HospitalEmployee getEmp() {
    return hospitalEmployee;
  }

  public Patient getPatient() {
    return patient;
  }
}
