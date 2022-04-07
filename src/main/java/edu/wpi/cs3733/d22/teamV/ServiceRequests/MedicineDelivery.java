package edu.wpi.cs3733.d22.teamV.ServiceRequests;

import edu.wpi.cs3733.d22.teamV.objects.HospitalEmployee;
import edu.wpi.cs3733.d22.teamV.objects.Patient;

public class MedicineDelivery extends ServiceRequest {
  private Patient patient;
  private HospitalEmployee employee;
  private String medicineName, dosage, roomNumber, requestDetails;
  private int userID;
  private int patientID;
  /**
   * @param patientFirstName
   * @param patientLastName
   * @param patientID
   * @param hospitalID
   * @param medicineName
   * @param dosage
   * @param requestDetails
   */
  public MedicineDelivery(
      String patientFirstName,
      String patientLastName,
      String roomNumber,
      int patientID,
      int hospitalID,
      String medicineName,
      String dosage,
      String requestDetails) {
    this.dosage = dosage;
    this.requestDetails = requestDetails;
    this.roomNumber = roomNumber;
    this.userID = patientID;
    patient = new Patient(patientID, patientFirstName, patientLastName);
    employee = new HospitalEmployee(hospitalID);
    this.medicineName = medicineName;
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

  public int getEmployeeID() {
    return employee.getHospitalID();
  }

  public String getMedicineName() {
    return medicineName;
  }

  public String getDosage() {
    return dosage;
  }

  public String getRequestDetails() {
    return requestDetails;
  }

  public String getRoomNumber() {
    return roomNumber;
  }
}
