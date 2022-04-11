package edu.wpi.cs3733.d22.teamV.objects;

import edu.wpi.cs3733.d22.teamV.main.Vdb;
import edu.wpi.cs3733.d22.teamV.servicerequests.ServiceRequest;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee {
  private int employeeID;
  private String firstName;
  private String lastName;
  private String employeePosition;
  private ArrayList<String> specialties;
  private ArrayList<Integer> patientIDs;
  private ArrayList<Integer> serviceRequestIDs;

  public Employee(
      int employeeID,
      String firstName,
      String lastName,
      String employeePosition,
      ArrayList<String> specialties,
      ArrayList<Integer> patientIDs,
      ArrayList<Integer> serviceRequestIDs) {
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

  public void addPatient(Patient patient) {
    patientIDs.add(patient.getPatientID());
  }

  public void addPatient(int patientID) {
    patientIDs.add(patientID);
  }

  public void removePatient(Patient patient) {
    patientIDs.remove(patient);
  }

  public void removePatient(int patientID) {
    patientIDs.remove(patientID);
  }

  public int getPatientListSize() {
    return patientIDs.size();
  }

  public ArrayList<Patient> getPatientList() {
    ArrayList<Patient> patients = new ArrayList<>();
    for (int patientID : patientIDs) {
      patients.add(Vdb.requestSystem.getPatients().get(patientID));
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
      // No method to implement this yet...
    }
    return null;
  }
}
