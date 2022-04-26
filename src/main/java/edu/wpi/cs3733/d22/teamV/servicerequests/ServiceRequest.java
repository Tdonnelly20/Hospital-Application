package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.map.Floor;
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
  protected Floor floor;
  protected int serviceID;
  public Patient patient;
  protected Employee employee;
  public boolean toBeDeleted = false;
  protected Timestamp timeMade; // when this was made
  protected String type;
  protected RequestSystem.Dao dao;
  protected String details;
  protected String status = "Not Started";
  protected String notes;
  protected Icon icon;
  public Image image;
  private String nodeID;

  public String getTimeString() {
    return timeMade.toString().substring(0, timeMade.toString().indexOf(".") - 3);
  }

  public String getFloorName() {
    return floor.getFloorName();
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public int getEmployeeID() {
    return employee.getEmployeeID();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

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

  public Timestamp getTimeMade() {
    return timeMade;
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
    String detailString;
    String typeInfo = type;
    if (details == null || details.equals("null")) {
      detailString = "N/A";
    } else {
      detailString = details;
    }
    if (notes != null) {
      typeInfo.concat(" - " + notes);
    }
    if (patient == null) {
      return "Location: "
          + location.getNodeID()
          + "\nX: "
          + getLocation().getXCoord()
          + "    Y: "
          + getLocation().getYCoord()
          + "\nEmployee: "
          + employee.getLastName()
          + ", "
          + employee.getFirstName()
          + " (ID: "
          + employee.getEmployeeID()
          + ")\nService Request: "
          + typeInfo
          + " (ID: "
          + serviceID
          + ")\nDetails: "
          + detailString;
    }
    return "Location: "
        + location.getNodeID()
        + "\nX: "
        + getLocation().getXCoord()
        + "    Y: "
        + getLocation().getYCoord()
        + "\nEmployee: "
        + employee.getLastName()
        + ", "
        + employee.getFirstName()
        + " (ID: "
        + employee.getEmployeeID()
        + ")\nPatient: "
        + patient.getLastName()
        + ", "
        + patient.getFirstName()
        + " (ID: "
        + patient.getPatientID()
        + ")\nService Request: "
        + typeInfo
        + " (ID: "
        + serviceID
        + ")\nDetails: "
        + detailString;
  }

  public int getServiceID() {
    return serviceID;
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
