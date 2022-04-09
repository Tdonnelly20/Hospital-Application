package edu.wpi.cs3733.d22.teamV.objects;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee {
  private int employeeID;
  private String firstName;
  private String lastName;
  private List<String> specialties;
  private List<Patient> patientList;
  private List<Employee> employeeList;

  public Employee(
      int employeeID,
      String firstName,
      String lastName,
      List<String> specialties,
      List<Patient> patientList) {
    this.employeeID = employeeID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.specialties = specialties;
    this.patientList = patientList;
  }

  public Employee(int employeeID) {
    this.employeeID = employeeID;
  }

  public Employee() {}

  public void addPatient(Patient patient) {
    patientList.add(patient);
  }

  public void removePatient(Patient patient) {
    patientList.remove(patient);
  }

  public int getPatientListSize() {
    return patientList.size();
  }
}
