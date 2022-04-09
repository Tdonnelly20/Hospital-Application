package edu.wpi.cs3733.d22.teamV.servicerequests;

import edu.wpi.cs3733.d22.teamV.objects.Employee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;

public class InternalPatientTransportation extends ServiceRequest {
  private Patient patient;
  private Employee employee;
  private String roomNumber, requestDetails;

  /**
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param roomNumber
   * @param requestDetails
   */
  public InternalPatientTransportation(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String requestDetails) {
    patient = new Patient(patientID, patientFirstName, patientLastName);
    employee = new Employee(hospitalID);
    this.roomNumber = roomNumber;
    this.requestDetails = requestDetails;
  }

  public String getPatientFirstName() {
    return patient.getFirstName();
  }

  public String getPatientLastName() {
    return patient.getLastName();
  }

  public int getPatientID() {
    return patient.getPatientID();
  }

  public int getHospitalID() {
    return employee.getEmployeeID();
  }

  public String getRoomNumber() {
    return roomNumber;
  }

  public String getRequestDetails() {
    return requestDetails;
  }
}
