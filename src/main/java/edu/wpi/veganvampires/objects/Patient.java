package edu.wpi.veganvampires.objects;

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
  private static List<HospitalEmployee> hospitalEmployeeList = new ArrayList<HospitalEmployee>();

  public Patient(int patientID, String firstName, String lastName) {
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
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
