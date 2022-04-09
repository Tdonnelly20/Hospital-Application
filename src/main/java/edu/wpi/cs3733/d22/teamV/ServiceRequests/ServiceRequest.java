package edu.wpi.cs3733.d22.teamV.ServiceRequests;

import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.objects.HospitalEmployee;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ServiceRequest {
  protected Location location;
  private int serviceID;
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
