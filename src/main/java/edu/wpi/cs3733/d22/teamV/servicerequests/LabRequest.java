package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabRequest extends ServiceRequest {
  private final Patient patient;
  private final String lab;
  private String status;
  private int userID;
  private int patientID;
  private String firstName;
  private String lastName;

  public LabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    // this.location = location;
    this.patient = Vdb.requestSystem.getPatients().get(patientID);
    // System.out.println(patient.getFirstName() + " " + patient.getLastName());
    this.employee = new Employee(userID);

    this.lab = lab;
    this.status = status;
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userID = userID;
    this.type = "Lab Request";
  }

  public LabRequest(int userID, int patientID, String nodeID, String lab, String status) {
    this.location = RequestSystem.getSystem().getLocationDao().getLocation(nodeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatient(patientID);
    // System.out.println(patient.getFirstName() + " " + patient.getLastName());
    this.employee = RequestSystem.getSystem().getEmployeeDao().getEmployee(userID);

    this.lab = lab;
    this.status = status;
    this.patientID = patientID;
    this.userID = userID;
    this.type = "Lab Request";
  }

  public int getEmployeeID() {
    return employee.getEmployeeID();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public String getFirstName() {
    return patient.getFirstName();
  }

  public String getLastName() {
    return patient.getLastName();
  }

  public String getNodeID() {
    return location.getNodeID();
  }

  public void setServiceID(int serviceID) {
    super.setServiceID(serviceID);
    DirectionalAssoc.link(employee, patient, this);
    updateAllObservers();
  }
}
