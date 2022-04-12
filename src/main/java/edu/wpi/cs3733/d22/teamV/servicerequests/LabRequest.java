package edu.wpi.cs3733.d22.teamV.servicerequests;

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

  public LabRequest(int employeeID, int patientID, String nodeID, String lab, String status) {
    this.location = Vdb.requestSystem.getLocationDao().getLocation(nodeID);
    this.hospitalEmployee = Vdb.requestSystem.getEmployeeDao().getEmployee(employeeID);
    this.patient = Vdb.requestSystem.getPatientDao().getPatientFromID(patientID);
    this.lab = lab;
    this.status = status;
    this.patientID = patientID;
    this.userID = employeeID;
    this.type = "Lab Request";
  }
}
