package edu.wpi.cs3733.d22.teamV.objects;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient {
  private int patientID;
  private String firstName;
  private String lastName;
  private ArrayList<Integer> employeeIDs = new ArrayList<>();
  private ArrayList<Integer> serviceIDs = new ArrayList<>();

  public Patient(
      String firstName,
      String lastName,
      ArrayList<Integer> employeeIDs,
      ArrayList<Integer> serviceIDs) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.employeeIDs = employeeIDs;
    this.serviceIDs = serviceIDs;
  }

  public Patient(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void setPatientID(int patientID) {
    this.patientID = patientID;
  }

  public void addEmployee(Employee employee) {
    employeeIDs.add(employee.getEmployeeID());
  }

  public void removeEmployee(Employee employee) {
    employeeIDs.remove(employee.getEmployeeID());
  }

  public int getEmployeeListSize() {
    return employeeIDs.size();
  }

  public int getServiceRequestListSize() {
    return serviceIDs.size();
  }

  public ArrayList<Integer> getEmployeeIDs() {
    return employeeIDs;
  }

  public ArrayList<Integer> getServiceRequestIDs() {
    return serviceIDs;
  }
}
