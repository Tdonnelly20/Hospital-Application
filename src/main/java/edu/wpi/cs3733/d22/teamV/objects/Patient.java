package edu.wpi.cs3733.d22.teamV.objects;

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

  public int getPatientID() {
    return patientID;
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
    ArrayList<Integer> employeeIDList = new ArrayList<>();

    for (Employee employee : getEmployeeList()) {
      employeeIDList.add(employee.getEmployeeID());
    }

    return employeeIDList;
  }

  public ArrayList<Integer> getServiceRequestIDs() {
    return serviceIDs;
  }

  public ArrayList<Employee> getEmployeeList() {
    ArrayList<Employee> employees = new ArrayList<>();
    for (int employeeID : employeeIDs) {
      for (Employee employee : Vdb.requestSystem.getEmployees()) {
        if (employee.getEmployeeID() == employeeID) {
          employees.add(employee);
        }
      }
    }
    return employees;
  }

  public ArrayList<ServiceRequest> getServiceRequestList() {
    ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();
    for (int serviceID : serviceIDs) {
      for (ServiceRequest request : Vdb.requestSystem.getEveryServiceRequest()) {
        if (request.getServiceID() == serviceID) {
          serviceRequests.add(request);
        }
      }
    }
    return serviceRequests;
  }

  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    // Check to see what updated and its type
    if (directionalAssoc instanceof Employee) {
      Employee employee = (Employee) directionalAssoc;
      int employeeID = employee.getEmployeeID();

      boolean patientContains = employeeIDs.contains(employeeID);
      boolean employeeContains = employee.getPatientIDs().contains(getPatientID());
      // Check to see if the patient has a state change relevant to the employee containing it
      if (patientContains && !employeeContains) {
        employeeIDs.removeIf(currID -> currID == employeeID);

      } else if (!patientContains && employeeContains) {
        employeeIDs.add(employeeID);
      }

    } else if (directionalAssoc instanceof ServiceRequest) {
      ServiceRequest serviceRequest = (ServiceRequest) directionalAssoc;
      // IDs from the subject
      int employeeID = serviceRequest.getEmployee().getEmployeeID();
      int serviceID = serviceRequest.getServiceID();
      int patientID = serviceRequest.getPatient().getPatientID();

      boolean patientContains = serviceIDs.contains(serviceID);
      boolean serviceRequestContains = patientID == getPatientID();

      if (patientContains && !serviceRequestContains || serviceRequest.toBeDeleted) {
        serviceIDs.removeIf(currID -> currID == serviceID);
        employeeIDs.removeIf(currID -> currID == employeeID);

      } else if (!patientContains && serviceRequestContains) {
        serviceIDs.add(serviceRequest.getServiceID());

        if (!employeeIDs.contains(employeeID)) employeeIDs.add(employeeID);
      }
    }

    Vdb.requestSystem.getPatientDao().updatePatient(this, getPatientID());
  }
}
