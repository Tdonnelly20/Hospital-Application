package edu.wpi.veganvampires.objects;

import edu.wpi.veganvampires.manager.EmployeeManager;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabRequest extends ServiceRequest {
  private final Patient patient;
  private final String lab;
  EmployeeManager employeeManager = EmployeeManager.getManager();

  public LabRequest(
      int userID, int patientID, String firstName, String lastName, String lab, String status) {
    this.patient = new Patient(patientID, firstName, lastName);
    this.patient.addHospitalEmployee(EmployeeManager.getManager().getEmployee(userID));
    this.lab = lab;
    this.status = status;
  }
}
