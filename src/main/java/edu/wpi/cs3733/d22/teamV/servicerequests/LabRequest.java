package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.main.RequestSystem;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.objects.Patient;
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

  public LabRequest(int userID, int patientID, String nodeID, String lab, String status) {
    this.location = RequestSystem.getSystem().getLocationDao().getLocation(nodeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    // System.out.println(patient.getFirstName() + " " + patient.getLastName());
    this.hospitalEmployee = RequestSystem.getSystem().getEmployeeDao().getEmployee(userID);

    this.lab = lab;
    this.status = status;
    this.patientID = patientID;
    this.userID = userID;
    this.type = "Lab Request";
    this.dao = RequestSystem.Dao.LabRequest;
  }

  public int getEmployeeID() {
    return hospitalEmployee.getEmployeeID();
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
}
