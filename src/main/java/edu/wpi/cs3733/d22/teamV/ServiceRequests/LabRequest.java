package edu.wpi.cs3733.d22.teamV.ServiceRequests;

import edu.wpi.cs3733.d22.teamV.manager.EmployeeManager;
import edu.wpi.cs3733.d22.teamV.objects.HospitalEmployee;
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

  EmployeeManager employeeManager = EmployeeManager.getManager();

  public LabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    // this.location = location;
    this.patient = new Patient(patientID, firstName, lastName);
    // System.out.println(patient.getFirstName() + " " + patient.getLastName());
    this.hospitalEmployee = new HospitalEmployee(userID);
    this.patient.addHospitalEmployee(EmployeeManager.getManager().getEmployee(userID));

    this.lab = lab;
    this.status = status;
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userID = userID;
    this.desc = "Lab Request";
  }
}
