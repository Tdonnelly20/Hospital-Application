package edu.wpi.veganvampires.Classes;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient {
  private int patientID;
  private int insurenceID;
  private String firstName;
  private String lastName;
  private List<HospitalEmployee> hospitalEmployeeList;

  public Patient(
      int patientID,
      int insurenceID,
      String firstName,
      String lastName,
      List<HospitalEmployee> hospitalEmployeeList) {
    this.patientID = patientID;
    this.insurenceID = insurenceID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.hospitalEmployeeList = hospitalEmployeeList;
  }

  public void addHospitalEmployee(HospitalEmployee employee) {
    hospitalEmployeeList.add(employee);
  }

  public void removeHospitalEmployee(HospitalEmployee employee) {
    hospitalEmployeeList.remove(employee);
  }

  public int getHospitalEmployeeNum() {
    return hospitalEmployeeList.size();
  }
}
