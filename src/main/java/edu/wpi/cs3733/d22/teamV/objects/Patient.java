package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient {
  private int patientID;
  private String firstName;
  private String lastName;
  private ArrayList<Employee> employeeList = new ArrayList<>();
  private ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();

  public Patient(int patientID, String firstName, String lastName) {
    this.patientID = patientID;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void addEmployee(Employee employee) {
    employeeList.add(employee);
  }

  public void removeEmployee(Employee employee) {
    employeeList.remove(employee);
  }

  public int getEmployeeListSize() {
    return employeeList.size();
  }
}
