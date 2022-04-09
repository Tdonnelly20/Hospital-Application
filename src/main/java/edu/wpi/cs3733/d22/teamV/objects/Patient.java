package edu.wpi.cs3733.d22.teamV.objects;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient {
  private int patientID;
  private String firstName;
  private String lastName;
  private static List<Employee> hospitalEmployeeList = new ArrayList<Employee>();

  public Patient(int patientID, String firstName, String lastName) {
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void addHospitalEmployee(Employee employee) {
    hospitalEmployeeList.add(employee);
  }

  public void removeHospitalEmployee(Employee employee) {
    hospitalEmployeeList.remove(employee);
  }

  public int getHospitalEmployeeNum() {
    return hospitalEmployeeList.size();
  }
}
