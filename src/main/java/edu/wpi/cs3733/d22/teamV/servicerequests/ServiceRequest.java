package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.map.Icon;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Location;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import java.sql.Timestamp;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

// https://db.apache.org/derby/docs/10.0/manuals/reference/sqlj143.html
@Setter
@Getter
public abstract class ServiceRequest extends DirectionalAssoc {
  protected Location location;
  private int serviceID;
  public Patient patient;
  protected Employee employee;
  public boolean toBeDeleted = false;
  protected Timestamp timestamp;
  protected String type;
  protected String notes;
  protected String status;
  protected Icon icon;
  public Image image;
  private String nodeID;

  public String getRequestName() {
    if (patient != null) {
      System.out.println(patient.getLastName() + ", " + patient.getFirstName());
      return type + ": " + patient.getLastName() + ", " + patient.getFirstName();
    }
    return type;
  }

  public String getNodeID() {
    return location.getNodeID();
  }

  public void detachAll() {
    toBeDeleted = true;
    updateAllObservers();
    releaseAll();
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public String toString() {
    if (patient == null || employee == null) {
      return "Location: " + location.getNodeID() + " Service ID: " + serviceID;
    } else {
      return "Location: "
          + location.getNodeID()
          + " Service ID: "
          + serviceID
          + " Patient ID: "
          + patient.getPatientID()
          + " Employee ID: "
          + employee.getEmployeeID();
    }
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    // Check to see what updated and its type
    System.out.println("running service request update!");
    if (directionalAssoc instanceof Employee) {
      Employee employee = (Employee) directionalAssoc;
      boolean serviceRequestContains = getEmployee().getEmployeeID() == employee.getEmployeeID();
      boolean employeeContains = employee.getServiceRequestIDs().contains(serviceID);

      // Check to see if the patient has a state change relevant to the employee containing it
      if (serviceRequestContains && !employeeContains) {
        setEmployee(null);

      } else if (!serviceRequestContains && employeeContains) {
        setEmployee(employee);
      }

    } else if (directionalAssoc instanceof Patient) {
      Patient patient = (Patient) directionalAssoc;
      boolean serviceRequestContains = getPatient().getPatientID() == patient.getPatientID();
      boolean patientContains = patient.getPatientID() == getPatient().getPatientID();

      if (serviceRequestContains && !patientContains) {
        setPatient(null);

      } else if (!serviceRequestContains && patientContains) {
        setPatient(patient);
      }
    }

    // Updated in individual classes
  }
}
