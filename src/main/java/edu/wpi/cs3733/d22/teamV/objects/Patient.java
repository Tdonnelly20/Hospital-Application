package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.dao.PatientDao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Patient extends DirectionalAssoc {
  private int patientID;
  private String firstName;
  private String lastName;
  private ArrayList<Integer> employeeIDs = new ArrayList<>();
  private ArrayList<Integer> serviceIDs = new ArrayList<>();
  private static PatientDao patientDao = Vdb.requestSystem.getPatientDao();

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

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    // Check to see what updated and its type
    System.out.println("running patient update!");
    if (directionalAssoc instanceof Employee) {
      Employee employee = (Employee) directionalAssoc;
      boolean patientContains = employeeIDs.contains(employee.getEmployeeID());
      boolean employeeContains = employee.getPatientIDs().contains(getPatientID());

      // Check to see if the patient has a state change relevant to the employee containing it
      if (patientContains && !employeeContains) {
        employeeIDs.removeIf(currID -> currID == employee.getEmployeeID());

      } else if (!patientContains && employeeContains) {
        employeeIDs.add(employee.getEmployeeID());
      }

    } else if (directionalAssoc instanceof ServiceRequest) {
      ServiceRequest serviceRequest = (ServiceRequest) directionalAssoc;
      boolean patientContains = serviceIDs.contains(serviceRequest.getServiceID());
      boolean serviceRequestContains = serviceRequest.getPatient().getPatientID() == patientID;

      if (patientContains && !serviceRequestContains) {
        serviceIDs.removeIf(currID -> currID == serviceRequest.getServiceID());
      } else if (!patientContains && serviceRequestContains) {
        serviceIDs.add(serviceRequest.getServiceID());
      }
    }

    // Update the Dao
    // patientDao.updatePatient(this, getPatientID());
  }
}
