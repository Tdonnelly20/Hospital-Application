package edu.wpi.cs3733.d22.teamV.objects;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee {
  private int hospitalID;
  private String firstName;
  private String lastName;
  private List<String> degrees;
  private List<Patient> patientList;
  private List<Employee> employeeList;

  public Employee(
      int hospitalID,
      String firstName,
      String lastName,
      List<String> degrees,
      List<Patient> patientList) {
    this.hospitalID = hospitalID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.degrees = degrees;
    this.patientList = patientList;
  }

  public Employee(int hospitalID) {
    this.hospitalID = hospitalID;
  }

  public Employee() {}

  public void addPatient(Patient patient) {
    patientList.add(patient);
  }

  public void removePatient(Patient patient) {
    patientList.remove(patient);
  }

  public int getPatientNum() {
    return patientList.size();
  }
}
