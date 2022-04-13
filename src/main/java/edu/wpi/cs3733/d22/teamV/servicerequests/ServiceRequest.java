package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import java.sql.Timestamp;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

// https://db.apache.org/derby/docs/10.0/manuals/reference/sqlj143.html
@Setter
@Getter
public abstract class ServiceRequest {
  protected Location location;
  private int serviceID;
  public Patient patient;
  protected Employee hospitalEmployee;
  protected Timestamp timestamp;
  protected String type;
  protected String notes;
  protected String status;
  protected Icon icon;
  public Image image;

  public String getRequestName() {
    if (patient != null) {
      System.out.println(patient.getLastName() + ", " + patient.getFirstName());
      return type + ": " + patient.getLastName() + ", " + patient.getFirstName();
    }
    return type;
  }

  public Patient getPatient() {
    return patient;
  }

  public String toString() {
    if (patient == null || hospitalEmployee == null) {
      return "Location: " + location.getNodeID() + " Service ID: " + serviceID;
    } else {
      return "Location: "
          + location.getNodeID()
          + " Service ID: "
          + serviceID
          + " Patient ID: "
          + patient.getPatientID()
          + " Employee ID: "
          + hospitalEmployee.getEmployeeID();
    }
  }
}
