package edu.wpi.cs3733.d22.teamV.objects;

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

  public void removePatient(Patient patient) {
    patientIDs.remove(patient);
  }

  public int getPatientListSize() {
    return patientIDs.size();
  }

  public int getServiceRequestListSize() {
    return serviceRequestIDs.size();
  }

  public void addServiceRequest(ServiceRequest request) {
    serviceRequestIDs.add(request.getServiceID());
  }

  public void removeServiceRequest(ServiceRequest request) {
    serviceRequestIDs.removeIf(requestID -> requestID == request.getServiceID());
  }
}
