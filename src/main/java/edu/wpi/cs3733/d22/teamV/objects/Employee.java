package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.dao.EmployeeDao;
import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.observer.DirectionalAssoc;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee extends DirectionalAssoc {
  private int employeeID;
  private String firstName;
  private String lastName;
  private String employeePosition = "Doctor";
  private ArrayList<String> specialties;
  private ArrayList<Integer> patientIDs;
  private ArrayList<Integer> serviceRequestIDs;
  private boolean isAdmin;
  private EmployeeDao employeeDao = Vdb.requestSystem.getEmployeeDao();

  public Employee(
      int employeeID,
      String firstName,
      String lastName,
      String employeePosition,
      ArrayList<String> specialties,
      ArrayList<Integer> patientIDs,
      ArrayList<Integer> serviceRequestIDs,
      boolean isAdmin) {
    this.employeeID = employeeID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.specialties = specialties;
    this.patientIDs = patientIDs;
    this.employeePosition = employeePosition;
    this.serviceRequestIDs = serviceRequestIDs;
  }

  public Employee(int employeeID) {
    this.employeeID = employeeID;
  }

  public Employee() {}

  public int getPatientListSize() {
    return patientIDs.size();
  }

  public ArrayList<Patient> getPatientList() {
    ArrayList<Patient> patients = new ArrayList<>();
    for (int patientID : patientIDs) {
      for (Patient patient : Vdb.requestSystem.getPatients()) {
        if (patient.getPatientID() == patientID) {
          patients.add(patient);
        }
      }
    }
    return patients;
  }

  public int getServiceRequestListSize() {
    return serviceRequestIDs.size();
  }

  public void addServiceRequest(ServiceRequest request) {
    serviceRequestIDs.add(request.getServiceID());
  }

  public void addServiceRequest(int serviceID) {
    serviceRequestIDs.add(serviceID);
  }

  public void removeServiceRequest(ServiceRequest request) {
    serviceRequestIDs.remove(request.getServiceID());
  }

  public void removeServiceRequest(int serviceID) {
    serviceRequestIDs.remove(serviceID);
  }

  public ArrayList<ServiceRequest> getServiceRequestList() {
    ArrayList<ServiceRequest> serviceRequests = new ArrayList<>();
    for (int serviceID : serviceRequestIDs) {
      for (ServiceRequest request : Vdb.requestSystem.getEveryServiceRequest()) {
        if (request.getServiceID() == serviceID) {
          serviceRequests.add(request);
        }
      }
    }
    return serviceRequests;
  }

  // Used to update info in the observer
  @Override
  public void update(DirectionalAssoc directionalAssoc) {
    // Check to see what updated and its type
    System.out.println("running employee update!");
    if (directionalAssoc instanceof Patient) {
      Patient patient = (Patient) directionalAssoc;
      boolean employeeContains = patientIDs.contains(patient.getPatientID());
      boolean patientContains = patient.getEmployeeIDs().contains(getEmployeeID());

      // Check to see if the patient has a state change relevant to the employee containing it
      if (employeeContains && !patientContains) {
        patientIDs.removeIf(currID -> currID == patient.getPatientID());

      } else if (!employeeContains && patientContains) {
        patientIDs.add(patient.getPatientID());
      }

    } else if (directionalAssoc instanceof ServiceRequest) {
      ServiceRequest serviceRequest = (ServiceRequest) directionalAssoc;
      boolean employeeContains = serviceRequestIDs.contains(serviceRequest.getServiceID());
      boolean serviceRequestContains =
          serviceRequest.getEmployee().getEmployeeID() == getEmployeeID();

      if (employeeContains && !serviceRequestContains) {
        serviceRequestIDs.removeIf(currID -> currID == serviceRequest.getServiceID());
      } else if (!employeeContains && serviceRequestContains) {
        serviceRequestIDs.add(serviceRequest.getServiceID());
      }
    } else {

    }
    System.out.println("Test 0!" + employeeDao);
    // Update the Dao
    employeeDao.updateEmployee(this, getEmployeeID());
  }
}
